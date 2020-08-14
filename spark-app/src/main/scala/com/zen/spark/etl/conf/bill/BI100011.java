package com.zen.spark.etl.conf.bill;

/**
 * 1.账单ID 2.时间戳 3.订单号 4.第三方交易号 5.游戏ID 6.游戏服ID 7.用户ID 8.渠道号 9.支付方式 10.实际支付金额
 * 11.下单金额 12.道具ID 13.道具数量 14.发豆数量 15.手机设备号 16.扩展字段 17.支付状态 18.省份 19.结果码 20.第三方结果码
 * 21.第三方状态描述 22.运营商 23.APK版本 24.SDK版本 25.Game版本 26.支付场景 27.支付消耗时间(回调-下单时间戳) 28.子代码 29.场景详情 30.定位省份
 * 31.定位城市 32.联网方式 33.imsi 34.iccid 35.设备名 36.尝试接口 37.第三方记录 38.手机号 39. 40.应用ID
 * 41.公司/账号 42.计费点 43.D 44.xvivoTask 45.ip 46.开发语言 47.主游戏appId 48.子渠道
 * Created by Tom
 * on 2017-03-07.
 */
public class BI100011 {
    public static int billId = 0;// 账单ID
    public static int ts = 1;// 时间戳
    public static int paymentId = 2;// 订单号
    public static int outTradeNo = 3;// 第三方交易号
    public static int gameId = 4;// 游戏ID
    public static int bi6 = 5;// 游戏服ID
    public static int userId = 6;// 用户ID
    public static int channelId = 7;// 渠道号
    public static int payType = 8;// 支付类型
    public static int money = 9;// 实际支付金额

    public static int billMoney = 10;// 下单金额
    public static int boxId = 11;// 道具ID
    public static int boxNum = 12;// 道具数量
    public static int coin = 13;// 游戏币
    public static int imei = 14;// 手机设备号
    public static int bi16 = 15;// 扩展字段
    public static int status = 16;// 支付状态
    public static int province = 17;// 省份
    public static int bi19 = 18;// 结果码
    public static int bi20 = 19;// 第三方结果码

    public static int bi21 = 20;// 第三方状态描述
    public static int carrier = 21;// 运营商
    public static int apkVersion = 22;// APK版本
    public static int sdkVersion = 23;// SDK版本
    public static int version = 24;// Game版本
    public static int paySceneId = 25;// 支付场景
    public static int bi27 = 26;// 支付消耗时间(回调-下单时间戳)
    public static int subCode = 27;// 子代码
    public static int bi29 = 28;// 场景详情
    public static int bi30 = 29;// 定位省份

    public static int bi31 = 30;// 定位城市
    public static int network = 31;// 联网方式
    public static int imsi = 32;// imsi
    public static int iccid = 33;// iccid
    public static int device = 34;// 设备名
    public static int interfaceOrigin = 35;// 尝试接口
    public static int bi37 = 36;// 第三方记录
    public static int mobile = 37;// 手机号
    public static int orderType = 48;// orderType

    public static int TOTAL = 38;
}
