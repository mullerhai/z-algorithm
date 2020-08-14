package com.zen.spark.etl.conf.bill;

/**
 * 设备号基础账单
 */
public class BIImeiBill {
    // 1.时间戳 2.游戏ID 3.IMEI 4.渠道 5.版本 6.注册设备 7.新进设备 8.只玩单机场 9.登陆平台 10.AB标识 11.登陆间隔(秒) 12.登陆间隔(天) 13.appId
    public static int ts = 0;// 时间戳
    public static int gameId = 1;// 游戏ID
    public static int imei = 2;// 用户ID
    public static int channelId = 3;// 渠道
    public static int version = 4;// 版本
    public static int isRegistImei = 6;// 新进设备
    public static int isRegist = isRegistImei;
    public static int singlePlay = 7;// 只玩单机场
    public static int loginPlat = 8;// 登陆平台
    public static int abFlag = 9;// AB标识
    public static int loginInterstatic = 10;// 登陆间隔(秒)
    public static int intervalDays = 11;// 登陆间隔(天)
    public static int appId = 12;// appId


}
