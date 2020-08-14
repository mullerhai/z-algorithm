package com.zen.spark.etl.conf.bill;

/**
 * @author Tom
 * @time 2017-04-01
 */
public class BIDBChargeTemp {
    /*1.支付时间 2.支付时间戳 3.支付结束时间 4.游戏ID 5.用户ID 6.订单号 7.支付渠道 8.支付渠道（原注册渠道） 9.支付类型 10.道具ID
        11.道具数量 12.DB金额 13.金额1 14.金额2 15.金额3 16.金额4 17.折扣1 18.折扣2 19.折扣3 20.折扣4
        21.子代码 22.省份 23.单机支付 24.param1-代码渠道 25.param2-代码应用 26.param3 27.PaymentInfor*/

    public static int payTime = 0;// 支付时间
    public static int ts = 1;// 支付时间戳
    public static int payCloseTime = 2;// 支付结束时间
    public static int gameId = 3;// 游戏ID
    public static int userId = 4;// 用户ID
    public static int paymentId = 5;// 订单号
    public static int payChannelId = 6;// 支付渠道
    public static int payRChannelId = 7;// 支付渠道（原注册渠道）
    public static int payType = 8;// 支付类型（原注册渠道）
    public static int boxId = 9;// 道具ID
    public static int boxNum = 10;//道具数量
    public static int dbMoney = 11;//DB金额
    public static int money1 = 12;//金额1
    public static int money2 = 13;//金额2
    public static int money3 = 14;//金额3
    public static int money4 = 15;//金额4
    public static int discountRate1 = 16;//折扣1
    public static int discountRate2 = 17;//折扣2
    public static int discountRate3 = 18;//折扣3
    public static int discountRate4 = 19;//折扣4
    public static int subCode = 20;//子代码
    public static int province = 21;//省份
    public static int alonePay = 22;//单机支付
    public static int param1CodeChannal1 = 23;//param1-代码渠道
    public static int param1CodeChannal2 = 24;//param2-代码渠道
    public static int param3 = 25;//param3
    public static int paymentInfor = 26; //json

}
