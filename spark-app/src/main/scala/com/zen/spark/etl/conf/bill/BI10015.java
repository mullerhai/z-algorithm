package com.zen.spark.etl.conf.bill;

/**
 * 连续登陆
 * Created Tom
 * 2017-09-19.
 * 1.账单ID 2.游戏ID 3.时间 4.用户ID 5.签到类型 6.增加游戏币 7.增加积分 8.添加后游戏币 9.添加后积分 10.等级
 */
public class BI10015 {
    public static int billId = 0; // 账单ID
    public static int gameId = 1; // 游戏ID
    public static int ts = 2; // 时间
    public static int userId = 3; // 用户ID
    public static int type = 4; // 签到类型
    public static int currency = 5; // 增加游戏币
    public static int coin = 6; // 增加积分
    public static int endCurrency = 7; // 添加后游戏币
    public static int endCoin = 8; // 添加后积分
    public static int level = 9; // 等级

}
