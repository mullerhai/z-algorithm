package com.zen.spark.etl.conf.bill;

/**
 * 1.账单ID 2.游戏ID 3.登陆时间 4.用户ID 5.筹码 6.积分 7.等级 8.IMEI
 * 9.渠道 10.IP 11.版本 12.运营商 13.网络类型 14.VIP等级 15.登陆平台
 * 16.充值金额*100 17.注册时间 18.历史对局数 19.appID
 * Created by Tom
 * 2017-03-07.
 */
public class BI10008 {
    public static int billId = 0;// 账单ID
    public static int gameId = 1; // 游戏ID
    public static int ts = 2;// 时间戳
    public static int userId = 3;// 用户ID
    public static int coin = 4;// 游戏豆，筹码
    public static int point = 5;// 积分
    public static int level = 6;// 等级
    public static int imei = 7;// IMEI
    public static int channelId = 8;// 渠道
    public static int ip = 9;// IP
    public static int version = 10; // 版本
    public static int carrier = 11;// 运营商
    public static int network = 12;// 网络类型
    public static int vipLevel = 13;// VIP等级
    public static int loginPlat = 14;// 登陆平台
    public static int TOTAL = 15; //充值总额*100
    public static int registTs = 16; //注册时间戳
    public static int playTotal = 17; //对局总数
    public static int appId = 18; //appID
    public static int lastPayTotalPlay = 20; //上次充值时的历史对局数

}
