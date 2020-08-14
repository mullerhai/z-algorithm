package com.zen.spark.bean;

import java.io.Serializable;

public class UserBaseBean implements Serializable {

    String user_id;
    int province;
    int city;
    int device;
    int os;
    String register_date;
    String device_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    @Override
    public String toString() {
        return "UserBaseInfo{" +
                "user_id='" + user_id + '\'' +
                ", province=" + province +
                ", city=" + city +
                ", device=" + device +
                ", os=" + os +
                ", register_date='" + register_date + '\'' +
                ", device_id='" + device_id + '\'' +
                '}';
    }


}