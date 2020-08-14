package com.zen.spark.etl.conf.bill;

/**
 * 聚合广告回调
 * Created Tom
 * 2018-06-11
 * 2、时间戳,3、用户ID,4、游戏ID,5、渠道,6、结果:1正常,其他接口异常,7、描述,8、流水号,
 * 9、imei,10、imsi,11、ip,12、广告商ID,13、场景ID,14、子场景ID,15、网络,16、apk版本,17、sdk版本,18、游戏版本,19、设备,
 * 20、广告类型,21、广告结果,22、广告结果描述,23、发货状态:1发货,24、道具类型,25、道具数量,26、道具ID
 */
public class BI109103 {
    public static int billId = 0; // 账单ID
    public static int ts = 1; // 时间戳
    public static int userId = 2; // 用户ID
    public static int gameId = 3; // 游戏ID
    public static int channelId = 4; // 渠道
    public static int results = 5; // 结果:1正常,其他接口异常
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
    public static int advertisingResult = 20; // 广告结果
    public static int advertisingResultDesc = 21; // 广告结果描述
    public static int deliveryStatus = 22; // 发货状态:1发货
    public static int boxType = 23; // 道具类型
    public static int boxNum = 24; // 道具数量
    public static int boxID = 25; // 道具ID
    public static int advertisingConfigId = 26; // 广告类型配置表ID

}
