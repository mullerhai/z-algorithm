package com.zen.spark.etl.conf.bill;

/**
 * 玩家对局
 * Created Tom
 * 2017-03-07.
 * 1.账单ID 2.游戏ID 3.场次ID 4.房间ID 5.桌子ID 6.桌子类型 7.记录时间 8.用户ID 9.筹码数量 10.积分 11.等级 12.筹码增减
 * 13.积分增减 14.门票 15.是否是庄家 16.下注额 19.刮风下雨的输赢 20.赢的流量 21.输的流量 22.历史对局数 23.机器人类型
 * 24.输赢标识(1赢 -1输 0平)
 */
public class BI10001 {
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
    public static int point = 9; // 积分
    public static int level = 10; // 等级

    public static int coinDiff = 11; // 筹码增减
    public static int pointdiff = 12; // 积分增减
    public static int ticket = 13; // 门票
    public static int isBanker = 14; // 是否是庄家
    public static int bet = 15; // 下注额
    public static int playType = 17; // 对局类型

    public static int scmjDone = 18; // 川麻刮风下雨结算
    public static int winCoin = 19; // 赢的流量
    public static int loseCoin = 20; // 输的流量

    public static int TOTAL = 21; //历史对局数
    public static int winLose = 23; //输赢标识(1赢 -1输 0平)
}
