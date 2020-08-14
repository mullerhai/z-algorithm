package com.zen.spark.etl.conf.bill;

/**
 * 聚合广告拉取配置
 * Created Tom
 * 2018-06-11
 * 2、时间戳,3、用户ID,4、游戏ID,5、渠道,6、结果:1弹其他不弹,7、描述,8、流水号,
 * 9、imei,10、imsi,11、ip,12、广告商ID,13、场景ID,14、子场景ID,15、网络,
 * 16、apk版本,17、sdk版本,18、游戏版本,19、设备,20、广告类型,21、今天已领奖次数,
 * 22、奖励配置表ID,23、奖励类型,24、奖励数量,25、道具ID,26、奖励展示描述,27、广告类型配置表ID
 */
public class BI109102 {
    public static int billId = 0; // 账单ID
    public static int ts = 1; // 时间戳
    public static int userId = 2; // 用户ID
    public static int gameId = 3; // 游戏ID
    public static int channelId = 4; // 渠道
    public static int results = 5; // 结果:1弹其他不弹
    public static int describe = 6; // 描述
    public static int serial = 7; // 流水号
    public static int imei = 8; // imei
    public static int imsi = 9; // imsi
    public static int ip = 10; // ip
    public static int advertisingId = 11; // 广告商ID
    public static int sceneId = 12; // 场景ID
    public static int sonSceneId = 13; // 子场景ID
    public static int netWork = 14; // 网络
    public static int apkVersion = 15; // apk版本
    public static int sdkVersion = 16; // sdk版本
    public static int gameVersion = 17; // 游戏版本
    public static int equipment = 18; // 设备
    public static int advertisingTypes = 19; // 广告类型
    public static int awardToday = 20; // 今天已领奖次数
    public static int awardConfigTypeId = 21; // 奖励配置表ID
    public static int awardType = 22; // 奖励类型
    public static int awardNum = 23; // 奖励数量
    public static int boxId = 24; // 道具ID
    public static int awardDescribe = 25; // 奖励展示描述
    public static int advertisingConfigId = 26; // 广告类型配置表ID


}
