package com.zen.spark.etl.conf.bill;

/**
 * 1.账单ID 2.时间戳 3.用户ID 4.应用ID 5.游戏ID 6.渠道号 7.apk版本 8.sdk版本 9.游戏版本 10.imei 11.imsi 12.iccid 13.网络 14.型号 15.系统版本 16.主事件ID 17.子事件ID 18.自定义账单 #号分隔
 * Created by Tom
 * on 2017-03-07.
 */
public class BI200017 {
    public static int billId = 0;// 账单ID
    public static int ts = 1;//  时间戳
    public static int userId = 2;//  用户ID
    public static int appId = 3; //appId
    public static int gameId = 4;//游戏ID
    public static int channelId = 5;// 渠道
    public static int apkVerison = 6;// APK版本
    public static int sdkVerison = 7;// SDK版本
    public static int version = 8;// 游戏版本
    public static int imei = 9;// IMEI
    public static int network = 12;// 网络环境
    public static int mainId = 15;//主事件ID
    public static int sonId = 16;//子事件ID

    public static int extra = 17;//自定义账单
    public static int country = 20;//国家


}
