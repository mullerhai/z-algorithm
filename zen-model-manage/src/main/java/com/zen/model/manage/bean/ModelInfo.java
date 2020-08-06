package com.zen.model.manage.bean;



import com.zen.model.manage.constant.ModelType;
import com.zen.model.manage.constant.Symbol;
import com.zen.model.manage.util.ToolClass;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: xiongjun
 * @Date: 2020/6/11 11:45
 * @description 模型信息类
 * @reviewer
 */
public class ModelInfo implements Serializable {
    public ModelInfo() {}

    public ModelInfo(String token, String hdfsPath, String modelTypes) {
        String _hdfsPath = hdfsPath;
        // token 为空时，则认为是一个新的模型，按照 token 生成规则：hdfsPath 的 16 位 MD5 加密值，生成 token 值
        if (token == null || !token.isEmpty()) {
            if (hdfsPath.isEmpty()) {
                throw new IllegalArgumentException("hdfsPath 属性不能为空");
            }
            if (modelTypes.equals(ModelType.MODEL_TYPE_TF) && hdfsPath.contains(":")) {
                hdfsPath = hdfsPath.split(":")[0];
            }
            this.token = ToolClass.MD5(hdfsPath).substring(8, 24);
        } else {
            this.token = token;
        }
        this.hdfsPath = _hdfsPath;
        this.modelType = modelTypes;
    }

    /**
     * 主键 id
     */
    private Long id;
    /**
     * token 对应一个对外可用模型（该模型可能有多个版本）
     */
    private String token;
    /**
     * 模型存放的 HDFS 路径，多个模型，路径用逗号分隔
     */
    private String hdfsPath;
    /**
     * 模型类型，支持 spark, pmml, mleap,tensorflow 等
     */
    private String modelType;
    /**
     * 模型创建时间
     */
    private Timestamp createTime;
    /**
     * 模型更新时间
     */
    private Timestamp updateTime;
    /**
     * 模型版本号，从 1 开始递增
     */
    private Integer version;
    /**
     * 创建人,英文名
     */
    private String creator;
    /**
     * 来源脚本自动上传，或手动
     */
    private String source;
    /**
     * 模型描述信息，使用场景等描述
     */
    private String description;
    /**
     * 是否被线上使用，如果是，则会加载进模型池。该参数主要与 version 配合使用，代表当前使用模型哪个版本。
     * 注意：一个模型（同 token）只能有一个 version 会被用到（online = true）
     */
    private Boolean online;
    /**
     * 模型池大小，为 0 时使用单例
     * 有的模型是非线程安全的，这类模型需要使用到模型池
     */
    private Integer poolSize;

    private String warmUpFeature;

    public String getWarmUpFeature() {
        return warmUpFeature;
    }

    public void setWarmUpFeature(String warmUpFeature) {
        this.warmUpFeature = warmUpFeature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public String getHdfsPath() {
        return hdfsPath;
    }

    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelTypes) {
        this.modelType = modelTypes;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Boolean isSingleton() {
        return this.getPoolSize() == null || this.getPoolSize() == 0;
    }

    @Override
    public String toString() {
        return "ModelsInfo{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", hdfsPath='" + hdfsPath + '\'' +
                ", modelTypes='" + modelType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", version='" + version + '\'' +
                ", creator='" + creator + '\'' +
                ", source='" + source + '\'' +
                ", description='" + description + '\'' +
                ", online=" + online + '\'' +
                ", poolSize=" + poolSize +
                '}';
    }

    @Override
    public int hashCode() {
        int result = 17;
        if(token != null) {
            for(String path : hdfsPath.split(Symbol.COMMA.value())) {
                result = result * 31 + path.hashCode();
            }
        }
        int elementHash = (version ^ (version >>> 16));
        result = 31 * result + elementHash + Arrays.hashCode(modelType.split(Symbol.COLON.value()));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof ModelInfo) {
            ModelInfo obj = (ModelInfo) o;
            // 如果持有同样的 token，则认为是同一个模型
            return token.equals(obj.getToken());
        }
        return false;
    }
}
