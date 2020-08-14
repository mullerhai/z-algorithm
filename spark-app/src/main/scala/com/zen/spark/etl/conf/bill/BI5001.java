package com.zen.spark.etl.conf.bill;

/**
 * 端午活动账单
 */
public class BI5001 {
    // 1.5001 2.入口编号（1-5查看活动入口，6-8报名成功，参与活动入口） 3.游戏ID 4.用户ID 5.渠道 6.活动ID 7.入口标识 8.时间戳
    public static int billId = 0;// 5001
    public static int typeId = 1;// 入口编号
    public static int gameId = 2;// 游戏ID
    public static int userId = 3;// 用户ID
    public static int channelId = 4;// 渠道
    public static int activityId = 5;// 活动ID
    public static int typeName = 6;// 入口标识
    public static int ts = 7;// 时间戳

}
