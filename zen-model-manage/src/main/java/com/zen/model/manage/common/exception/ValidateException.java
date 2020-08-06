package com.zen.model.manage.common.exception;


import com.zen.model.manage.common.ResponseEncoder.ResponseStatus;

/**
 * @Author: morris
 * @Date: 2020/6/11 15:41
 * @description
 * @reviewer
 */
public class ValidateException extends RuntimeException{
    private int code;

    public ValidateException(int code, String msg){
        super(msg);
        this.code = code;
    }
    public ValidateException(ResponseStatus responseStatus){
        super(responseStatus.getStandardMessage());
        this.code = responseStatus.getCode();
    }
    public int getCode() {
        return code;
    }
}
