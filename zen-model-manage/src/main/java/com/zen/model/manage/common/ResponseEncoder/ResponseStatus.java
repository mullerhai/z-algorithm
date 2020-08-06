package com.zen.model.manage.common.ResponseEncoder;

/***
 * 返回状态枚举
 * @author luolei
 * @create 2018-04-26
 */
public enum ResponseStatus {
        /** response status info */
        SUCCESS(0, "OK"),
        BAD_REQUEST(400, "Bad Request"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Unknown Internal Error"),
        NOT_VALID_PARAM(40005, "Not valid Params"),
        NOT_SUPPORTED_OPERATION(40006, "Operation not supported"),
        PARAMETER_ERROR(40007, "输入的参数错误"),
        ID_NULL(40008, "id不能为空"),
        VALIDATIO_NEXCEPTION(40009, "系统验证异常"),
        SIGN_DATA_ERROR(40012, "MD5 SignData Error"),

        //模型平台相关
        MODEL_ROLLBACK_VERSION_ERROR(40013, "模型回滚版本异常-找不到上一版本"),
        MODEL_ROLLBACK_MISS_ERROR(40014, "模型回滚版本异常-找不到该token的线上模型"),
        MODEL_DELETE_ERROR(40015, "模型删除版本异常-找不到该token && version的模型 或 当前模型正在线上使用"),
        MODEL_UPDATE_ERROR(40016, "模型更新版本异常-找不到该token的模型"),
        MODEL_INSERT_ERROR(40016, "模型创建异常-已存在该token的模型"),




        NOT_LOGIN(50000, "Not Login"),
        SYSTEM_NEXCEPTION(50001, "系统异常");

        private int code;
        private String standardMessage;

        ResponseStatus(int code, String standardMessage) {
            this.code = code;
            this.standardMessage = standardMessage;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandardMessage() {
            return standardMessage;
        }

        public void setStandardMessage(String standardMessage) {
            this.standardMessage = standardMessage;
        }
    }