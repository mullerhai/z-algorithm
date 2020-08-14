package com.zen.spark.etl.conf.bill;

/**
 * 用户账单
 * Created by Tom
 * on 2017-03-07.
 */
public class BIAdverHourBill {
    // 账单格式：游戏，用户，去重点击次数，展示次数，视频去重点击次数，视频展示次数
    public static int gameId = 0;// 游戏ID
    public static int userId = 1;// 用户ID
    public static int distinctClickCount = 2;// 去重点击次数
    public static int showCount = 3;// 展示次数
    public static int videoDistinctClickCount = 4;// 视频去重点击次数
    public static int videoShowCount = 5;// 视频展示次数

}
