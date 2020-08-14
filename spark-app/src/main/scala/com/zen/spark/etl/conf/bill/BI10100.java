package com.zen.spark.etl.conf.bill;

/**
 * DBServer货币流动
 * Created Tom
 *  2017-03-07.
 */
public class BI10100 {
    public static int billId = 0;// 账单ID
    public static int changeType = 1;// 变化类型
    public static int changeSubType = 2;// 变化子类型
    public static int ts = 3;// 时间戳
    public static int userId = 4;// 用户ID
    public static int gameId = 5;// 游戏ID
    public static int coinDiff = 6;// 增减游戏豆
    public static int coinAfter = 7;// 增减后游戏豆
    public static int pointdiff = 8;// 增减积分
    public static int pointAfter = 9;// 增减后积分
    public static int TOTAL = 10;
}
