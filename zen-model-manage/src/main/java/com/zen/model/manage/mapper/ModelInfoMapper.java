package com.zen.model.manage.mapper;

import com.zen.model.manage.bean.ModelInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author: morris
 * @Date: 2020/6/11 15:46
 * @description
 * @reviewer
 */
@Repository
public interface ModelInfoMapper {
    int insert(ModelInfo modelInfo);

    /**
     * 获取最新上线的模型
     * @param token
     * @return
     */
    ModelInfo findLastOnlineModelByToken(String token);


    int updateLastModel( Long id);

    /**
     * 将该token所有版本模型下线
     * @param token
     * @return
     */
    int offlineModel(String token);

    /**
     * 指定下线某版本的 token 模型
     * @param token
     * @param version
     * @return
     */
    int offlineModelByTokenAndVersion(String token, Integer version);

    ModelInfo findModelByTokeAndVersion(String token, int version);

    int onlineModel(String token, Integer version);

    int deleteModelById(Long id);

    List<ModelInfo> findModelByToken(String token);

    List<ModelInfo> getModelsByStatus(Boolean online);
}
