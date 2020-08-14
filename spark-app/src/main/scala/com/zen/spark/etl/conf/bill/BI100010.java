package com.zen.spark.etl.conf.bill;

/**
 1.账单ID 2.时间戳 3.订单号 4.主游戏ID(appId) 5.游戏服ID 6.用户ID 7.渠道号 8.支付方式 9.下单金额
 10.道具ID 11.道具数量 12.游戏币 13.手机设备号 14.扩展字段 15.支付状态 16.省份 17.运营商 18.APK版本 19.SDK版本
 20.Game版本 21.支付场景 22.子代码 23.场景详情 24.定位省份 25.定位城市 26.联网方式 27.imsi 28.iccid 29.设备名
 30.接口来源 31.web二次 32.实际金额 33.手机号 34.sdk支持 35.省份来源 36.运营商ID 37.第三方战略支付 38.特殊配置ID 39.优先级主键
 40.clientFrom 41.abTest灰度信息 43.ip 44.流水号 45.子游戏ID 46.开发语言
 * Created by Tom
 * on 2017-03-07.
 */
public class BI100010 {
    public static int billId = 0;// 账单ID
    public static int ts = 1;// 时间戳
    public static int paymentId = 2;// 订单号
    public static int gameId = 3;// 游戏ID
    public static int gameServerId = 4;// 游戏服ID
    public static int userId = 5;// 用户ID
    public static int channelId = 6;// 渠道号
    public static int payType = 7;// 支付方式
    public static int money = 8;// 下单金额
    public static int boxId = 9;// 道具ID

    public static int boxNum = 10;// 道具数量
    public static int coinNum = 11;// 游戏币
    public static int imei = 12;// 手机设备号
    public static int extend = 13;// 扩展字段
    public static int payStatus = 14;// 支付状态
    public static int province = 15;// 省份
    public static int carrier = 16;// 运营商
    public static int apkVersion = 17;// APK版本
    public static int sdkVersion = 18;// SDK版本
    public static int version = 19;// Game版本

    public static int payScene = 20;// 支付场景
    public static int subCode = 21;// 子代码
    public static int paySceneDetail = 22;// 场景详情
    public static int gpsProvince = 23;// 定位省份
    public static int gpsCity = 24;// 定位城市
    public static int network = 25;// 联网方式
    public static int imsi = 26;// imsi
    public static int iccid = 27;// iccid
    public static int device = 28;// 设备名
    public static int interfaceOrigin = 29;// 接口来源

    public static int webTwice = 30;// web二次
    public static int actualMoney = 31;// 实际金额
    public static int mobile = 32;// 手机号
    public static int sdkSupport = 33;// sdk支持
    public static int provinceOrigin = 34;// 省份来源
    public static int carrierId = 35;// 运营商ID
    public static int specialConfigBill = 36;// 特殊配置账单
    public static int specialConfigId = 37;// 特殊配置ID
    public static int priorityKey = 38;// 优先级主键
    public static int clientFrom = 39;//clientFrom

    public static int abTest = 40;//abTest灰度信息
    public static int empty = 41;//clientFrom
    public static int ip = 42;//ip
    public static int transaction = 43;//流失号
    public static int appId = 44;//主游戏(appId)
    public static int developmentLanguage = 45;//开发语言

}
