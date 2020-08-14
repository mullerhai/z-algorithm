package com.zen.spark.etl.conf.bill;

/**
 * 10500
 *
 *  1.账单ID 2.时间戳 3.游戏ID 4.场次ID 5.房间ID 6.桌子ID 7.桌子类型 8.游戏自身账单ID 9.渠道 10.版本 11.用户ID 12.增加游戏币
 *  60018,60020 特殊------------------------------------------------
 *  充值勋章账单
 * 10500|......|60018|userID|渠道|游戏版本|勋章数|加分类型|道具ID|金额
 * 对局勋章账单
 * 10500|......|60020|userID|渠道|游戏版本|勋章数
 */
public class BI10500 {
    public static int billId = 0;// 账单ID
    public static int ts = 1;// 时间戳
    public static int gameId = 2;//游戏ID
    public static int sceneId = 3;// 场次ID
    public static int homeId = 4;// 房间ID
    public static int sonBillId = 7; //子账单ID
    public static int USERID = 8; //用户ID


}
