package com.zen.spark.etl.conf.bill;

/**
 * 1.账单ID 2.时间戳 3.游戏ID 4.渠道 5.apk版本 6.IMEI 7.网络环境 8.运营商 9.游戏自定义 10.游戏版本 11.自定义子账单 12.appId
 * Created by Tom
 * on 2017-03-07.
 */
public class BI200007 {
    public static int billId = 0;// 账单ID
    public static int ts = 1;//  时间戳
    public static int gameId = 2;//游戏ID
    public static int channelId = 3;// 用户ID
    public static int apkVerison = 4;// APK版本
    public static int imei = 5;// IMEI
    public static int network = 6;// 网络环境
    public static int carrier = 7;// 运营商
    public static int subBill = 8;// 游戏自定义
    public static int version = 9;// 游戏版本
    public static int TOTAL = 10;
    public static int appId = 11; //appId
    public static int province = 13; //省份
    public static int city = 14; //城市
}
