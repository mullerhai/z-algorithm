package com.zen.spark.etl.conf.bill;

/**
 * 支付账单
 */
public class BIChargeBill {
    // 1.订单号 2.时间戳 3.游戏ID 4.用户ID 5.Imei 6.支付渠道 7.注册渠道 8.版本 9.支付类型 10.省份
    // 11.道具ID 12.道具数量 13.DB金额 14.金额1 15.注册用户16.尝试 17.点击 18.发送 19.发送结果 20.回调
    // 21.回调结果 22.回调时间 23.数据库充值 24.支付场景 25.金额3 26.子代码 27.金额4 28.金额2 29.折扣1 30.折扣2
    // 31.折扣3 32.折扣4 33.三张牌游戏ID 34.APK版本 35.子支付场景 36.SDK版本 37.代码应用编号（codeAppNo）38.代码渠道编号（codeChannelNo）
    // 39.充值来源  43.原渠道  44.二次确认 45.渠道商 46.注册渠道 47.appId 48.已注册天数
    public static int paymentId = 0;
    public static int ts = 1;
    public static int gameId = 2;
    public static int userId = 3;
    public static int imei = 4;
    public static int channelId = 5;
    public static int channelId2 = 6;
    public static int version = 7;
    public static int payType = 8;
    public static int province = 9;
    public static int propId = 10;
    public static int propNum = 11;
    public static int dbMoney = 12;
    public static int money1 = 13;
    public static int isRegist = 14;
    public static int isTry = 15;
    public static int isClick = 16;
    public static int isSend = 17;
    public static int sendResult = 18;
    public static int isCallback = 19;
    public static int callbackResult = 20;
    public static int callbackTime = 21;
    public static int isDBCharge = 22;
    public static int paySceneId = 23;
    public static int money3 = 24;
    public static int subCode = 25;
    public static int money4 = 26;
    public static int money2 = 27;
    public static int discountRate1 = 28;
    public static int discountRate2 = 29;
    public static int discountRate3 = 30;
    public static int discountRate4 = 31;
    public static int gameId2 = 32;
    public static int apkVersion = 33;
    public static int subPaySceneId = 34;
    public static int sdkVersion = 35;
    public static int codeAppNo = 36;
    public static int codeChannelNo = 37;
    public static int codeSource = 38;

    public static int TOTAL = 39;
    public static int OriginalChannel = 42;
    public static int webTwice = 43;
    public static int carrier = 44;
    public static int registChannelId = 45;
    public static int appId = 46;
    public static int registDay = 47;
    public static int isSelfCharge = 48;
}