package com.zen.spark.etl.conf.bill;

/**
 * 格式： 1.账单ID 2.游戏ID 3.用户ID 4.渠道 5.时间 6.注册平台 7.ip 9.浏览器 9.系统 10.游戏版本号 11.设备号 12.apkversion 13.sdkversion 
 * 14.device 15.平台ID 16.运营商 17.网络 18.imsi 19.iccid 20.省份 21.系统版本 22.定位省份 23.定位城市 24.厂商 
 * 25.分辨率 26.手机号 27.ip省份 28.ip城市 29.运营商标识 30.网络运营商 31.extraInfo 32.包渠道 33.接口版本 34.appId 35.邀请人ID 36.来源 37.子来源

	备注： 说明：用户账单ID为固定值101002 运营商 移动-mobile 联通-unicom 电信-telecom 网络 2g 3g wifi

 */
public class BI101002 {
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

    public static int imei = 10;// 设备号
    public static int apkVersion = 11;// apkversion
    public static int sdkVersion = 12;// sdkversion
    public static int device = 13;// device
    public static int platId = 14;// 平台ID
    public static int carrierCode = 15;// 运营商代码
    public static int network = 16;// 网络
    public static int imsi = 17;// imsi
    public static int iccid = 18;// iccid
    public static int province = 19;// 省份
    public static int androidVersion = 20;// 系统版本
    public static int gpsProvince = 21;// 定位省份
    public static int gpsCity = 22;// 定位城市
    public static int deviceCompany = 23;// 厂商
    public static int resolution = 24;// 分辨率
    public static int mobile = 25;// 手机号
    public static int ipProvince = 26;// ip省份
    public static int ipCity = 27;// ip城市
    public static int carrier = 28;// 运营商
    public static int bi33 = 29;// 网络运营商
    public static int extraInfo = 30;// extraInfo
    public static int packageChannelId = 31; //包渠道
    public static int interfaceVersion = 32; //接口版本
    public static int appId = 33; //appId
    public static int inviteId = 34; //邀请人ID
    public static int fromChannelId = 35; //来源
    public static int sonFromChannelId = 36; //子来源

}
