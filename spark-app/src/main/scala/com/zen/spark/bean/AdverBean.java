package com.zen.spark.bean;

import java.io.Serializable;

public class AdverBean implements Serializable {

    String ad_id;
    String advertiser_id;
    String appname;
    String material_id;

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAdvertiser_id() {
        return advertiser_id;
    }

    public void setAdvertiser_id(String advertiser_id) {
        this.advertiser_id = advertiser_id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    @Override
    public String toString() {
        return "AdverInfo{" +
                "ad_id='" + ad_id + '\'' +
                ", advertiser_id='" + advertiser_id + '\'' +
                ", appname='" + appname + '\'' +
                ", material_id='" + material_id + '\'' +
                '}';
    }


}