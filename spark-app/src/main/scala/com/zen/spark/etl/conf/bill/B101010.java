package com.zen.spark.etl.conf.bill;

/**
 * 小程序上报
 *
 * @author Tom
 * @time 2018/8/21
 * <p>
 * 2.子账单ID 3.时间戳 4.appId 5.游戏ID 6.用户ID 7.渠道号 8.openId
 * 9. 子账单内容
 * 是否授权#
 * 来源渠道#来源应用ID#来源信息
 * 场景#模块#子模块#事件
 * 微信场景ID#
 */
public class B101010 {
    public static int billId = 0;// 账单ID
    public static int sonType = 1;
    public static int ts = 2;
    public static int appId = 3;
    public static int gameId = 4;
    public static int userId = 5;
    public static int channelId = 6;
    public static int openId = 7;
    public static int sonBillContent = 8;


}
