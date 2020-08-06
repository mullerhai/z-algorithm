package com.zen.model.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.zen.model.manage.dto.ModelInfoDto;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * @Author: morris
 * @Date: 2020/6/11 15:43
 * @description
 * @reviewer
 */
public interface ManageService {
    String createModel(ModelInfoDto modelInfoDto);

    /**
     * 更新模型
     * @param modelInfoDto 模型信息
     * @return
     * @throws ValidationException
     */
    String updateModel(ModelInfoDto modelInfoDto) throws ValidationException;

    String offlineModel(String token);

    JSONObject rollbackModel(String token);

    String deleteModel(String token, Integer version);

    /**
     * 上传实体模型，并在 DB 添加模型记录，默认关闭模型
     * @param path
     * @param file
     * @param token
     * @return
     * @Exception
     */
    String uploadModelFile(String path, MultipartFile file, String token) throws Exception;

    /**
     * 获取状态下所有模型信息
     * @param status
     * @return
     */
    List<ModelInfoDto> getModelList(Boolean status);

    String uploadModelFile(String path, MultipartFile file) throws Exception;
}
