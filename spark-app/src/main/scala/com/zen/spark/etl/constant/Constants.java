package com.zen.spark.etl.constant;

/**
 * @Description 常量接口
 * @author tony
 * @time:2017年3月8日 下午4:10:54
 */
public interface Constants {
    /**
     * jdbc配置相关的常量
     */
    String JDBC_DRIVER = "jdbc.driver";
    String JDBC_DATASOURCE_SIZE = "jdbc.datasource.size";
    String JDBC_URL = "jdbc.url";
    String JDBC_WEB_URL = "jdbc.web.url";
    String JDBC_WEB2_URL = "jdbc.web2.url";

    String JDBC_USER = "jdbc.user";
    String JDBC_PASSWORD = "jdbc.password";

    String JDBC_WEB_DATABASE = "jdbc.web.database";
    String JDBC_MYSQL_DATABASE = "jdbc.mysql.database";
    String JDBC_DATASTAT_DATABASE = "jdbc.DataStat.database";

    /**
     * clickHouse jdbc配置相关的常量
     */
    String CLICKHOUSE_JDBC_DRIVER = "clickHouse.jdbc.driver";
    String CLICKHOUSE_JDBC_URL = "clickHouse.jdbc.url";
    String MYSQL_OSS_USERNAME = "mysql.oss.userName";
    String MYSQL_OSS_PASSWORD = "mysql.oss.password";
    String MYSQL_OSS_HOST = "mysql.oss.host";
    String MYSQL_OSS_WEB_HOST = "mysql.oss.web.host";
    String MYSQL_OSS_PORT = "mysql.oss.port";
    
    
    /**
     * 副库
     */
    String JDBC_WebSalve_URL = "jdbc.url.web_db_slave";
    String JDBC_WebSalve_DATABASE = "jdbc.webSalve.database";
    String JDBC_WebSalve_USER = "jdbc.user.web_db_slave";
    String JDBC_WebSalve_PASSWORD = "jdbc.password.web_db_slave";


    /**
     * cs_game_s2
     */
    String JDBC_csGameS2_URL = "jdbc.url.cs_game_s2";
    String JDBC_csGameS2_DATABASE = "jdbc.csGameS2.database";
    String JDBC_csGameS2_USER = "jdbc.user.cs_game_s2";
    String JDBC_csGameS2_PASSWORD = "jdbc.password.cs_game_s2";

    /**
     * ddz_db5
     */
    String JDBC_ddz_db_URL = "jdbc.url.ddz_db";
    String JDBC_ddz_db_DATABASE = "jdbc.ddz_db.database";
    String JDBC_ddz_db_USER = "jdbc.user.ddz_db";
    String JDBC_ddz_db_PASSWORD = "jdbc.password.ddz_db";

    /**
     * ddz_db4
     */
    String JDBC_ddz_db_odd_URL = "jdbc.url.ddz_db_odd";
    String JDBC_ddz_db_odd_DATABASE = "jdbc.ddz_db_odd.database";
    String JDBC_ddz_db_odd_USER = "jdbc.user.ddz_db_odd";
    String JDBC_ddz_db_odd_PASSWORD = "jdbc.password.ddz_db_odd";

    /**
     * shmj_s1
     */
    String JDBC_mj_db_URL = "jdbc.url.mj_db";
    String JDBC_mj_db_DATABASE = "jdbc.mj_db.database";
    String JDBC_mj_db_USER = "jdbc.user.mj_db";
    String JDBC_mj_db_PASSWORD = "jdbc.password.mj_db";

    /**
     * bj_web_gxl
     */
    String JDBC_gxl_db_URL = "jdbc.url.gxl_db";
    String JDBC_gxl_db_DATABASE = "jdbc.gxl_db.database";
    String JDBC_gxl_db_USER = "jdbc.user.gxl_db";
    String JDBC_gxl_db_PASSWORD = "jdbc.password.gxl_db";

    /**
     * jdbc测试的本地库
     */
    String JDBC_URL_TEST = "jdbc.url.test";
    String JDBC_USER_TEST = "jdbc.user.test";
    String JDBC_PASSWORD_TEST = "jdbc.password.test";
    /**
     * kafka配置相关的常量
     */
    String KAFKA_METADATA_BROKER_LIST = "kafka.metadata.broker.list";
    String KAFKA_METADATA_TOPICS = "kafka.metadata.topics";
    String KAFKA_GROUP_ID = "kafka.group.id";
    /**
     * kafka测试的常量
     */
    String KAFKA_TEST_GROUP_ID = "kafka.test.group.id";
    /**
     * spark配置相关的常量
     */
    String SPARK_LOCAL = "spark.local";
    String STREAMING_CHECKPOINT_PATH = "streaming.checkpoint.path";
    /**
     * hbase配置相关的常量
     */
    String HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT = "hbase.zookeeper.property.clientPort";
    String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
    String HBASE_TABLE_USERPAYDETAIL200003 = "hbase.table.userPayDetail200003";
    String HBASE_TABLE_USERPAYDETAIL100010 = "hbase.table.userPayDetail100010";
    String HBASE_TABLE_USERPAYDETAIL100011 = "hbase.table.userPayDetail100011";

    String HBASE_TABLE_BILL10100 = "hbase.table.bill10100";//游戏豆变化账单
    String HBASE_TABLE_BILL10001 = "hbase.table.bill10001";//玩家对局详情账单
    String HBASE_TABLE_BILL10008 = "hbase.table.bill10008";//玩家IP记录
    String HBASE_TABLE_BILL10029 = "hbase.table.bill10029";//聊天账单
    String HBASE_TABLE_BILL10001Play = "hbase.table.bill10001Play";//玩家对局
    String HBASE_TABLE_BILL101001 = "hbase.table.bill101001";//web登录
    String HBASE_TABLE_BILLUSERINFO = "hbase.table.billUserInfo";//10008账单,100011回调,10001对局
    String HBASE_TABLE_BILLUSERINFOHISTORY = "hbase.table.billUserInfoHistory";//历史数据:10008账单,100011回调,10001对局
    String HBASE_TABLE_USERCHEATCHECK = "hbase.table.userCheatCheck";//检查用户作弊
    String HBASE_TABLE_LOGIN101001 = "hbase.table.login101001";//用户登录
    String HBASE_TEST = "hbase.table.test";//测试的
    String HBASE_TABLE_BILL10510 = "hbase.table.bill10510";//对局ID
    String HBASE_TABLE_BILL10511 = "hbase.table.bill10511";//游戏内容
    
    String HBASE_TABLE_BILL10100_S = "hbase.table.bill10100_s";//游戏豆变化账单
    
    String HBASE_TABLE_USER_PORTRAIT_BASE="hbase.table.userPortraitBase";//用户画像的基础表
    
    String HBASE_TABLE_USER_PORTRAIT_PAY="hbase.table.userPortraitPay";//用户画像支付相关的表

    String HBASE_TABLE_USER_PORTRAIT_MATCHUP = "hbase.table.userPortraitMatchup";//用户画像对局相关的表
    
    String HBASE_TABLE_USER_PORTRAIT_LOGIN = "hbase.table.userPortraitLogin";//用户画像登陆相关的表
    
    String HBASE_TABLE_USER_PORTRAIT_PLAY = "hbase.table.userPortraitPlay";//用户画像玩法对局相关的表
    
    String HBASE_TABLE_USER_BASE_TAG="hbase.table.userBaseTag";//用户id的基础表
    
    String HBASE_TABLE_FISH_USER_PORTRAIT_BASE="hbase.table.fish.userPortraitBase";//捕鱼用户画像的基础表
    
    String HBASE_TABLE_FISH_USER_PORTRAIT_LOGIN = "hbase.table.fish.userPortraitLogin";//捕鱼用户画像登陆相关的表
    
    String HBASE_TABLE_FISH_USER_PORTRAIT_MATCHUP = "hbase.table.fish.userPortraitMatchup";//捕鱼用户画像对局相关的表
    
    String HBASE_TABLE_FISH_USER_PORTRAIT_PAY="hbase.table.fish.userPortraitPay";//捕鱼用户画像支付相关的表
    
    String HBASE_TABLE_MAHJONG_USER_PORTRAIT_BASE="hbase.table.mahjong.userPortraitBase";//川麻用户画像的基础表
    
    String HBASE_TABLE_MAHJONG_USER_PORTRAIT_LOGIN = "hbase.table.mahjong.userPortraitLogin";//川麻用户画像登陆相关的表
    
    String HBASE_TABLE_MAHJONG_USER_PORTRAIT_MATCHUP = "hbase.table.mahjong.userPortraitMatchup";//川麻用户画像对局相关的表
    
    String HBASE_TABLE_MAHJONG_USER_PORTRAIT_PAY="hbase.table.mahjong.userPortraitPay";//川麻用户画像支付相关的表
    
    String HBASE_TABLE_ChANNEL101001 = "hbase.table.bill101001_channel";//登陆渠道

    String HBASE_TABLE_SHARE = "hbase.table.share";//分享带量
//    String HBASE_TABLE_SHARE_TEST = "hbase.table.share.test";//测试分享带量

    String HBASE_TABLE_CLICK = "hbase.table.click";//点击观看视频
    String HBASE_TABLE_CLICK1 = "hbase.table.click1";//点击观看视频1

    String HBASE_TABLE_PLAYCOUNT = "hbase.table.playCount";//某一天对局次数

    String HBASE_TABLE_PLAYTIME = "hbase.table.playTime";//对局时长与历史时长

    String HBASE_TABLE_ACTIVECOUNT = "hbase.table.activeSum";//对局活跃

    String HBASE_TABLE_VIDEOANDSHARE = "hbase.table.videoShare";//对局活跃

    String HBASE_TABLE_USERAD = "hbase.table.userAd";//插屏、视频次数

    String HBASE_TABLE_TRY = "hbase.table.userTry";//用户尝试次数

    String HBASE_TABLE_CHARGE = "hbase.table.userCharge";//用户充值

    // 游戏工作室需要统计的需求
    String HBASE_TABLE_DAYMONEY= "hbase.table.dayUserMoney"; // 用户充值
    String HBASE_TABLE_WATCHVIDEO= "hbase.table.watchVideo"; // 用户是否观看广告
    String HBASE_TABLE_POCAN= "hbase.table.pocan"; // 用户破产且未看广告

    // 微信表(用户唯一id表)
    String HBASE_TABLE_RT_WECHAT = "hbase.table.rt.weChat";

    // H5游戏用户表
    String HBASE_TABLE_RT_USERINFO = "hbase.table.rt.userInfo";

    // 反查匹配表
    String HBASE_TABLE_RT_WECHATTMP = "hbase.table.rt.weChatTmp";

    // 头像存储表
    String HBASE_TABLE_RT_HEADDATA = "hbase.table.rt.headData";
    
    //广告
    String HBASE_TABLE_AD = "hbase.table.rt.ad";
    String HBASE_TABLE_AD_TYPE = "hbase.table.rt.ad.type";
    String HBASE_TABLE_AD_DATE = "hbase.table.rt.ad.date";
    String HBASE_TABLE_AD_TYPE_DATE = "hbase.table.rt.ad.type.date";

    /**
     * zookeeper相关的
     */
    String ZOOKEEPER_ADDRESS = "zookeeper.address";

    /*
     * redis相关的
     */
    String REDIS_ADDRESS = "redis.address";


    /**
     * mongo相关的
     */
    String MONGO_ADDRESS = "mongo.address";
    String MONGO_USERNAME = "mongo.username";
    String MONGO_PASSWORD = "mongo.password";
    String MONGO_DATABASE = "mongo.database";

    /**
     * mongo测试相关的
     */
    String MONGO_TEST_ADDRESS = "mongo.test.address";
    String MONGO_TEST_USERNAME = "mongo.test.username";
    String MONGO_TEST_PASSWORD = "mongo.test.password";
    String MONGO_TEST_DATABASE = "mongo.test.database";
    
    String APP_PARALLELISM="app.parallelism";
    String APP_RUNMODE="app.runmode";
	
    /**
     * 测试的
     */
    String HBASE_TABLE_TESTFISH_USER_PORTRAIT_BASE="hbase.table.test.fish.userPortraitBase";//捕鱼用户画像的基础表
    
    /**
     * 第二期用户画像的表
     * 
     */
    String HBASE_TABLE_USER_PROFILE_TAG = "hbase.table.userProfileTag";//用户画像用户基本属性表
    String HBASE_TABLE_USER_AD_TAG = "hbase.table.userAdTag";//用户画像用户广告属性表
    String HBASE_TABLE_USER_GAME_TAG = "hbase.table.userGameTag";//用户画像用户行为属性表
    String HBASE_TABLE_USER_CONSUME_TAG = "hbase.table.userConsumeTag";//用户画像消费行为属性表
	
    /*
     * es相关的
     */
    String ES_HOST_NAME = "es.host.name";
    
}
