package com.zen.model.manage.constant;

/**
 * @Author: xiongjun
 * @Date: 2020/6/11 12:16
 * @description
 * @reviewer
 */
public enum Symbol {
    /**
     * 标点符号
     */
    COMMA(","),COLON(":"),SEMICOLON(";");

    private String value;

    private Symbol(String value) {
        this.value = value;
    }

    public String value(){
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
