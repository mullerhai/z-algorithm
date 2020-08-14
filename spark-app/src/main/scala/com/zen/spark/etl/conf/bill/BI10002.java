package com.zen.spark.etl.conf.bill;

/**
 * 余额不足赠送
 * Created Tom
 * 2017-09-09.
 * 1.账单ID 2.游戏ID 3.场次ID 4.房间ID 5.桌子ID 6.桌子类型 7.记录时间 8.用户ID 9.之前的筹码数量 10.赠送了多少 11.第几次赠送 12.当日截至目前对局次数 13.历史充值金额*100
 */
public class BI10002 {
    public static int billId = 0; // 账单ID
    public static int gameId = 1; // 游戏ID
    public static int gameSceneId = 2; // 场次ID
    public static int roomId = 3; // 房间ID
    public static int deskId = 4; // 桌子ID
    public static int tableId = deskId;
    public static int deskType = 5; // 桌子类型
    public static int tableType = deskType;
    public static int ts = 6; // 记录时间
    public static int userId = 7; // 用户ID
    public static int coin = 8; // 筹码数量
    public static int giftCoin = 9; // 赠送了多少
    public static int giftNum = 10; // 第几次赠送
    public static int playNum = 11; // 当日截至目前对局次数
    public static int chargeMoney = 12; // 历史充值金额*100

}
