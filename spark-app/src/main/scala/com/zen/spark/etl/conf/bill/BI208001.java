package com.zen.spark.etl.conf.bill;

/**
 * Created by Tom
 * Date  2017-03-27
 * 活动信息
 */
public class BI208001 {
    /*activeid -1是活动中心，其他值代表具体活动
    imei 设备号
    imsi 手机卡唯一标识
    type  -1无意义 -2表示查看活动 1表示参加活动 2表示活动行为一 3表示活动行为二  4表示活动行为三（PS:2和3是不同的行为统计,请把它们分别做在oss上）
    活动框架：10发道具
    gifttype  0是默认值没有具体意义 1是游戏币 2是兑换卷  3是道具 （活动发放游戏豆、兑换券或者道具）
    giftsubtype 当gifttype等于3是道具的时候，则giftsubtype是道具ID  10015是1天vip卡 10017是5天vip卡 198是7天vip卡 7是流量卡
    giftnum 发放数量  这个字段与gifttype相对应（当gifttype等于1 giftnum是游戏豆发放数量，当gifttype等于2 giftnum是兑换券发放数量，当gifttype等于3 giftnum是道具发放数量）
    backGifttype  0是默认值没有具体意义 1是游戏币 2是兑换卷（活动回收游戏豆、兑换券）
    backGiftnum 回收数量  这个字段与backGifttype相对应（当backGifttype等于1 backGiftnum是游戏豆回收数量，当backGifttype等于2 backGiftnum是兑换券回收数量）
    money 默认是0表示没有充值  大于0的数值则表示用户充值的具体金额
    extra 不同活动的额外信息，活动的具体额外信息会以#号*/
    public static int billId = 0;
    public static int ts = 1;
    public static int gameId = 2;
    public static int userId = 3;
    public static int activeid = 4;
    public static int channelId = 5;
    public static int version = 6;
    public static int sdkVersion = 7;
    public static int apkVersion = 8;
    public static int imei = 9;// -1是活动中心，其他值代表具体活动
    public static int imsi=10 ;
    public static int type = 11;
    public static int gifttype = 12;
    public static int giftsubtype = 13;
    public static int giftnum = 14;
    public static int backGifttype = 15;
    public static int backGiftnum = 16;
    public static int money = 17;
    public static int extra = 18;

}
