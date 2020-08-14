package com.zen.spark.etl.conf.bill;

/**
 * 1.账单ID 2.游戏ID 3.用户ID 4.渠道 5.时间 6.注册平台 7.ip 8.浏览器 9.系统 10.游戏版本号
 * 11.运营商 12.网络 13.设备号 14.apkversion 15.sdkversion 16.device 17.平台ID 18.imsi 19.iccid
 * 20.省份 21.系统版本 22.定位省份 23.定位城市 24.厂商 25.分辨率 26.手机号 27.用户名 28.第三方扩展 29.登录间隔
 * 30.ip省份 31.ip城市 32.运营商 33.网络运营商 34.新用户标识 35.idfa 36.注册天数 37.上次充值时间 38.注册渠道 39.appId
 * 备注： 说明：用户账单ID为固定值101001 运营商 移动-mobile 联通-unicom 电信-telecom 网络 2g 3g wifi
 */
public class BI101001 {
    public static int billId = 0;// 账单ID
    public static int gameId = 1;// 游戏ID
    public static int userId = 2;// 用户ID
    public static int channelId = 3;// 渠道
    public static int ts = 4;// 时间
    public static int plat = 5;// 注册平台
    public static int ip = 6;// IP
    public static int browser = 7;// 浏览器
    public static int system = 8;// 系统
    public static int version = 9;// 版本
    public static int carrierCode = 10;// 运营商代码
    public static int network = 11;// 网络
    public static int imei = 12;// 设备号
    public static int apkVersion = 13;// apk版本
    public static int sdkVersion = 14;// sdk版本
    public static int device = 15;// 设备
    public static int platId = 16;// 平台ID
    public static int imsi = 17;// imsi
    public static int iccid = 18;// iccid
    public static int province = 19;// 省份
    public static int androidVersion = 20;// 系统版本
    public static int gpsProvince = 21;// 定位省份
    public static int gpsCity = 22;// 定位城市
    public static int deviceCompany = 23;// 厂商
    public static int resolution = 24;// 分辨率
    public static int mobile = 25;// 手机号
    public static int userName = 26;// 用户名
    public static int bi28 = 27;// 第三方扩展
    public static int loginInterstatic = 28;// 登录间隔
    public static int ipProvince = 29;// ip省份
    public static int ipCity = 30;// ip城市
    public static int carrier = 31;// 运营商
    public static int bi33 = 32;// 网络运营商
    public static int newUserFlag = 33;// 新用户标识
    public static int extraInfo = 34;// extraInfo
    public static int registDays = 35;
    public static int chagreInterval = 36; //上次充值时间
    public static int loginChannelId = 37; //注册渠道
    public static int appId = 38; //注册渠道
    public static int packageChannel=39;//包渠道
    public static int interfaceVersion=40;//接口版本
    public static int guid=41;
    public static int LTE=42;//网络子类型

    public static int fromChannelId = 46;//来源
    public static int address = 48;//完整地址
    public static int latitude = 49;//纬度
    public static int lontitude = 50;//经度
    public static int country = 51;//国家

}
