package com.zen.model.manage.bean;

/**
 * @Author: xiongjun
 * @Date: 2020/6/11 15:12
 * @description
 * @reviewer
 */
public class Feature {
    private String field;
    private String dataType;
    private String value;

    public Feature() {
    }

    public Feature(String field, String dataType, String value) {
        this.field = field;
        this.dataType = dataType;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "field='" + field + '\'' +
                ", dataType='" + dataType + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
