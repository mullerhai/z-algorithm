package com.zen.model.manage.service.impl;


import brave.Tracer;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zen.model.manage.bean.Feature;
import com.zen.model.manage.bean.ModelInfo;
import com.zen.model.manage.common.ResponseEncoder.ControllerResponseEncoder;
import com.zen.model.manage.common.ResponseEncoder.ResponseStatus;
import com.zen.model.manage.common.exception.ValidateException;
import com.zen.model.manage.constant.CommonCns;
import com.zen.model.manage.dto.ModelInfoDto;
import com.zen.model.manage.mapper.ModelInfoMapper;
import com.zen.model.manage.service.ManageService;
import com.zen.model.manage.util.FileUtil;
import com.zen.model.manage.util.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: morris
 * @Date: 2020/6/11 15:43
 * @description
 * @reviewer
 */
@Service
@Slf4j
public class ManageServiceImpl implements ManageService {
    public static final String WARM_UP_FEATURE = "warmUpFeature";
    private static Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);

    @Autowired
    private ModelInfoMapper modelInfoMapper;

    @Autowired
    Tracer tracer;

    @Override
    public String createModel(ModelInfoDto modelInfoDto) {
        //校验参数
        ModelInfo modelInfo = null;
        if (!StringUtils.isEmpty(modelInfoDto.getHdfsPath())
                && !StringUtils.isEmpty(modelInfoDto.getModelType())) {
            modelInfo = new ModelInfo(null, modelInfoDto.getHdfsPath(), modelInfoDto.getModelType());
            PropertyUtils.copyProperties(modelInfoDto, modelInfo, "token", "warmUpFeature");
            //校验是否已经存在
            if (isExist(modelInfo)) {
                throw new ValidateException(ResponseStatus.MODEL_INSERT_ERROR);
            }
            //特征列表转json
            setWarmUpFeature(modelInfoDto, modelInfo);
            //设置version
            modelInfo.setVersion(1);
            //开启上线
            modelInfo.setOnline(true);
            modelInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));

            System.out.println(modelInfo);
            modelInfoMapper.insert(modelInfo);
        }
        logger.info("create model successed：{} traceId:{}", modelInfo.getToken(), tracer.currentSpan().context().traceIdString());
        return modelInfo.getToken();
    }

    @Override
    public String updateModel(ModelInfoDto modelInfoDto) throws ValidationException {
        //updateModel online default true
        if (modelInfoDto.getOnline() == null) {
            modelInfoDto.setOnline(true);
        }
        return updateModel(modelInfoDto, true);
    }

    private Boolean isExist(ModelInfo modelInfo) {
        List<ModelInfo> modelByToken = modelInfoMapper.findModelByToken(modelInfo.getToken());
        if (modelByToken != null && !modelByToken.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * 新增已有模型记录，并根据 online 判断是否上线
     *
     * @param modelInfoDto 模型信息
     * @param online       模型是否开启
     * @return
     * @throws ValidationException
     */
    @Transactional
    protected String updateModel(ModelInfoDto modelInfoDto, Boolean online) throws ValidationException {
        String token = modelInfoDto.getToken();
        if (token == null || StringUtils.isEmpty(token)) {
            logger.error("模型更新异常-更新时token不能为空");
            throw new ValidationException("50001", "更新时token不能为空");
        }
        ModelInfo modelInfo = new ModelInfo();
        PropertyUtils.copyProperties(modelInfoDto, modelInfo, "warmUpFeature");
        //get last model info
        ModelInfo lastModelByToken = modelInfoMapper.findLastOnlineModelByToken(token);
        if (lastModelByToken == null) {
            throw new ValidateException(ResponseStatus.MODEL_UPDATE_ERROR);
        }

        if (online) {
            // if online must taken off last model
            modelInfoMapper.offlineModel(token);
            modelInfo.setOnline(true);
        } else {
            modelInfo.setOnline(false);
        }
        //设置最新版本参数
        modelInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        modelInfo.setVersion(lastModelByToken.getVersion() + 1);
        //设置特征
        setWarmUpFeature(modelInfoDto, modelInfo);
        int i = modelInfoMapper.insert(modelInfo);
        return dmlResProcessor(i);
    }

    private void setWarmUpFeature(ModelInfoDto modelInfoDto, ModelInfo modelInfo) {
        List<Feature> warmUpFeature = modelInfoDto.getWarmUpFeature();
        modelInfo.setWarmUpFeature(JSONObject.toJSON(warmUpFeature).toString());
    }

    @Override
    public String offlineModel(String token) {
        log.info("create model  traceId:{}" + tracer.currentSpan().context().traceIdString());

        int i = modelInfoMapper.offlineModel(token);
        return dmlResProcessor(i);
    }

    @Override
    @Transactional
    public JSONObject rollbackModel(String token) {
        //获取online最新版本的模型信息
        ModelInfo lastModelByToken = modelInfoMapper.findLastOnlineModelByToken(token);
        if (null == lastModelByToken) {
            logger.error("模型回滚版本异常-找不到该token的线上模型,token：{}", token);
            throw new ValidateException(ResponseStatus.MODEL_ROLLBACK_MISS_ERROR);
        }
        ModelInfo modelByTokeAndVersion = modelInfoMapper.findModelByTokeAndVersion(token, lastModelByToken.getVersion() - 1);
        if (null == modelByTokeAndVersion) {
            logger.error("模型回滚版本异常-找不到上一版本,当前版本：{}", lastModelByToken.getVersion());
            throw new ValidateException(ResponseStatus.MODEL_ROLLBACK_VERSION_ERROR);
        }
        //回滚到上一版本模型
        modelInfoMapper.onlineModel(token, modelByTokeAndVersion.getVersion());
        modelInfoMapper.offlineModelByTokenAndVersion(token, lastModelByToken.getVersion());
        JSONObject res = new JSONObject();
        res.put("msg", "Succeed");
        res.put("version", modelByTokeAndVersion.getVersion());
        return res;
    }

    @Override
    public String deleteModel(String token, Integer version) {
        ModelInfo modelByTokeAndVersion = modelInfoMapper.findModelByTokeAndVersion(token, version);
        if (modelByTokeAndVersion == null || modelByTokeAndVersion.getOnline()) {
            throw new ValidateException(ResponseStatus.MODEL_DELETE_ERROR);
        }
        int i = modelInfoMapper.deleteModelById(modelByTokeAndVersion.getId());
        return dmlResProcessor(i);
    }

    @Override
    public String uploadModelFile(String path, MultipartFile file, String token) throws Exception {
        if (path == null){
            path = CommonCns.MODEL_UPLOAD_PATH;
        }
        //upload file to server
        String filePath = FileUtil.uploadModel(file, path);
        System.out.println(filePath);
        //get last model_info by token
        ModelInfo lastOnlineModelByToken = modelInfoMapper.findLastOnlineModelByToken(token);
        ModelInfoDto modelInfoDto = new ModelInfoDto();

        PropertyUtils.copyProperties(lastOnlineModelByToken, modelInfoDto,WARM_UP_FEATURE);
        List<Feature> featureList = JSONArray.parseArray(lastOnlineModelByToken.getWarmUpFeature(), Feature.class);
        modelInfoDto.setHdfsPath(filePath);
        modelInfoDto.setWarmUpFeature(featureList);
        //更新模型
        String res = updateModel(modelInfoDto);

        return res;
    }

    @Override
    public List<ModelInfoDto> getModelList(Boolean online) {
        List<ModelInfo> models = modelInfoMapper.getModelsByStatus(online);

        List<ModelInfoDto> modelInfoDtos = new ArrayList<>();
        models.stream().forEach(modelInfo -> {
            ModelInfoDto modelInfoDto = new ModelInfoDto();
            PropertyUtils.copyProperties(modelInfo,modelInfoDto,WARM_UP_FEATURE);
            List<Feature> featureList = JSONArray.parseArray(modelInfo.getWarmUpFeature(), Feature.class);
            modelInfoDto.setWarmUpFeature(featureList);
            modelInfoDtos.add(modelInfoDto);
        });
        return modelInfoDtos;

    }

    @Override
    public String uploadModelFile(String path, MultipartFile file) throws Exception {
        if (path == null){
            path = CommonCns.MODEL_UPLOAD_PATH;
        }
        //upload file to server
        String filePath = FileUtil.uploadModel(file, path);
        return filePath;

    }

    /**
     * DML sql结果处理器：
     * i >= 1为处理成功
     *
     * @param num DML 更新条数
     * @return
     */
    private String dmlResProcessor(int num) {
        return num >= 1 ? "Succeed" : "Failed";
    }
}
