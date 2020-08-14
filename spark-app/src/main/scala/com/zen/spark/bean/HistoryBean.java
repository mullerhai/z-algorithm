package com.zen.spark.bean;

import java.io.Serializable;

public class HistoryBean implements Serializable {

    String request_id;
    Long timestamp;
    String scene_id;
    String ad_id;
    String material_id;
    String ad_type;
    int connect_type;
    String device_id;
    int label;
    String cur_app_id;
    String channel;

    public String getCur_app_id() {
        return cur_app_id;
    }

    public void setCur_app_id(String cur_app_id) {
        this.cur_app_id = cur_app_id;
    }

    @Override
    public String toString() {
        return "HistoryBean{" +
                "request_id='" + request_id + '\'' +
                ", timestamp=" + timestamp +
                ", scene_id='" + scene_id + '\'' +
                ", ad_id='" + ad_id + '\'' +
                ", material_id='" + material_id + '\'' +
                ", ad_type='" + ad_type + '\'' +
                ", connect_type=" + connect_type +
                ", device_id='" + device_id + '\'' +
                ", label=" + label +
                ", cur_app_id='" + cur_app_id + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getScene_id() {
        return scene_id;
    }

    public void setScene_id(String scene_id) {
        this.scene_id = scene_id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public int getConnect_type() {
        return connect_type;
    }

    public void setConnect_type(int connect_type) {
        this.connect_type = connect_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }


}