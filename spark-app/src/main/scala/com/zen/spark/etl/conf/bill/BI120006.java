package com.zen.spark.etl.conf.bill;

/**
 1、120006 2、时间戳 3、三方结果码 4、三方结果描述 5、login或reg 6、userId 7、点击时间戳 8、计划id 9、转化id 10、推广商 11、回调或打折
 12、imei 13、idfa 14、渠道号 15、游戏名 16、游戏ID 17、计划名 18、md5值
 */
public class BI120006 {
    public static int billId = 0;// 账单ID
    public static int gameId = 16;// 游戏ID
    public static int userId = 5;// 用户ID
    public static int channelId = 13;// 渠道
    public static int ts = 1;// 时间
    public static int secretKey = 17; //来源计划名
    public static int ossSecretKey = 25; //oss来源唯一标识
    public static int creativeId = 19; //创意ID
}
