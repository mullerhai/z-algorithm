package com.zen.model.manage.common.ResponseEncoder;





public class Response {
    private int errorCode;
    private String errorMsg;
    private String traceId;
    private Object data;

    public Response() {
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Response(ResponseStatus responseStatus) {
        this(responseStatus.getCode(), responseStatus.getStandardMessage(), null);
    }

    public Response(int errorCode, String errorMsg, Object data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }
    public void setCodeAndMsg(ResponseStatus responseStatus)
    {
        this.errorCode = responseStatus.getCode();
        this.errorMsg = responseStatus.getStandardMessage();
    }
}
