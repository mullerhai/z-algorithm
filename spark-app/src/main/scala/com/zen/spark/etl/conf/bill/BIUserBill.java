package com.zen.spark.etl.conf.bill;

/**
 * 用户账单
 * Created by Tom
 * on 2017-03-07.
 */
public class BIUserBill {
    // 1.时间戳 2.游戏ID 3.用户ID 4.渠道 5.版本 6.注册用户 7.真实注册（设备历史去重) 8.登陆平台 9.VIP等级 10.AB标识
    // 11.注册时间戳 12.已注册天数 13.充值总额 14.第三方充值总额 15.APK版本 16.SDK版本 17.登录运营商 18.appId
    public static int ts = 0;// 时间戳
    public static int gameId = 1;// 游戏ID
    public static int userId = 2;// 用户ID
    public static int channelId = 3;// 渠道
    public static int version = 4;// 版本
    public static int isRegist = 5;// 注册用户
    public static int isRegistImei = 6;// 真实注册（设备历史去重)
    public static int country = 7;// 国家
    public static int vipLevel = 8;// VIP等级
    public static int abFlag = 9;// AB标识---------只是简单的奇偶，基本弃用，最新的都是用的userId的最后一位
    public static int registTs = 10;// 注册时间戳

    public static int registDay = 11;// 已注册天数
    public static int rechargeMoney = 12;// 充值总额
    public static int modeThirdMoney = 13;// 第三方充值总额
    public static int loginInterstatic = 14;// 登陆间隔(秒)
    public static int intervalDays = 15;// 登陆间隔(天)
    public static int appId = 16;// appId
    public static int totalPlays = 17;// 历史对局数(当日首次登陆)
    public static int totalMoneys = 18;// 历史充值总额(当日首次登陆)
    public static int lastPayTotalPlays = 19;// 上次充值时的对局数(当日首次登陆)
    public static int fromChannelId = 20; //当日来源
    public static int firstTs = 21; //首次登录时间戳
    public static int fromAdverId = 22; //来源广告ID
    public static int registFromChannelId = 23; //注册来源

    public static int todayBeInvitedInfo = 24; //当日邀请
    public static int beInvitedInfo = 25; //历史邀请
    public static int attributionInfo = 26; //历史归因
    public static int finalInfo = 27; //最终归因
    public static int creativeId = 28; //创意ID
}
