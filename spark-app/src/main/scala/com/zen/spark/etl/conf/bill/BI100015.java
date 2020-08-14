package com.zen.spark.etl.conf.bill;

/**
 * @Description:100015
 * @author:tony
 * @time:2017年3月17日 上午10:53:23
 */
public class BI100015{
    // 1.账单ID 2.时间戳 3.游戏ID 4.用户ID 5.订单号 6.支付类型 7.省份 8.imei 9.imsi 10.执行耗时
    // 11.指令 12.端口号 13.扩展参数 14.渠道 15.sdk版本 16.iccid 17.游戏版本 18.APK版本 19.扩展日志 20.手机号
    // 21.from

    public static int billId = 0;// 账单ID
    public static int ts = 1;// 时间戳
    public static int gameId = 2;// 游戏ID
    public static int userId = 3;// 用户ID
    public static int paymentId = 4;//订单号 
    public static int payType = 5;// 支付类型 
    public static int province = 6;// 省份
    public static int imei = 7;// imei
    public static int imsi = 8;// imsi
    public static int executionTime  = 9;// 执行耗时

    public static int instructions = 10;//指令
    public static int port = 11;//端口号
    public static int extParam = 12;// 扩展参数 
    public static int channelId = 13;// 渠道 
    public static int sdkVersion = 14;// sdk版本
    public static int iccid = 15;// iccid
    public static int gameVersion = 16;// 游戏版本
    public static int apkVersion = 17;// APK版本
    public static int extLog = 18;// 扩展日志
    public static int phone = 19;// 手机号

    public static int from = 20;// from
}
