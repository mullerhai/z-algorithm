package com.zen.spark.etl.conf.bill;

/**
 * 用户对局
 *
 * @author Tom
 * @time 2017-04-13
 */
public class BIUserGameInfoBill {
    /*1.时间 2.游戏ID 3.用户ID 4.场次ID 5.对局次数 6.Imei 7.总输赢 8.赢流量 9.输流量 10.门票消耗
    11.玩家赢的流量(系统坐庄) 12.玩家输的流量(系统坐庄) 13.玩家赢的流量(玩家坐庄) 14.玩家输的流量(玩家坐庄)*/
    public static int statDate = 0;
    public static int gameId = 1;
    public static int userId = 2;
    public static int gameSceneId = 3;
    public static int playNum = 4;
    public static int imei = 5;
    public static int finalCoin = 6;
    public static int winCoin = 7;
    public static int lossCoin = 8;
    public static int ticketCoin = 9;
    public static int robotBankerWinCoin = 10;
    public static int robotBankerLossCoin = 11;
    public static int userBankerWinCount = 12;
    public static int userBankerLoseCount = 13;
}
