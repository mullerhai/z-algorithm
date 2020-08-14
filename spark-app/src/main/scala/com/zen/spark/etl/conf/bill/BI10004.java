package com.zen.spark.etl.conf.bill;

/**
 * @author Tom
 * @time 2017/5/2
 */
public class BI10004 {
    /*    1.账单ID 2.游戏ID 3.记录时间 4.用户ID 5.boxID 6.价格 7.赠送的筹码(数量X单价) 8.之前的筹码 9.积分 10.等级
        11.支付金额(价格*数量) 12.购买数量 13.首充赠送 14.当日对局数 15.当日破产次数 16.订单号 17.类型 18.vip等级 19.vip到期时间*/
    public static int billId = 0;// 账单ID
    public static int gameId = 1; // 游戏ID
    public static int ts = 2;// 时间戳
    public static int userId = 3;// 用户ID
    public static int boxId = 4;// boxID
    public static int qb = 5;// 价格
    public static int givingCoin = 6;// 赠送的筹码
    public static int preCoin = 7;// 之前的筹码
    public static int points = 8;// 积分
    public static int grade = 9;// 等级
    public static int realPay = 10;// 支付金额
    public static int boxNum = 11;// 购买数量
    public static int firstGivingCoin = 12;// 首充赠送
    public static int todayPlayCount = 13;// 当日对局数
    public static int todayBankruptCount = 14;// 当日领取破产保护的次数
    public static int orderNO = 15;// 订单号
    public static int type = 16;// 类型
    public static int vipValue = 17;// vip等级
    public static int vipExpire = 18;// vip到期时间
}
