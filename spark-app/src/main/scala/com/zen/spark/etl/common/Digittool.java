package com.zen.spark.etl.common;

/** 
 * @Description: 数字工具类
 * @Author: boris
 * @Date: 2019/1/3
 */
public final class Digittool {
    /**
     *  规避魔鬼数字，提高代码可维护性
     */
    private Digittool(){}

    /* 获取的值，default 为0 */
    public static final int VAULE_ZERO = 0;

    /* 保留1位小数 */
    public static final int SAVE_DECIMAL_ONE = 1;
    /* 保留2位小数 */
    public static final int SAVE_DECIMAL_TWO = 2;

    /* 钱计算单位是分，除以100转为元 */
    public static final int MONEY_UNIT1 = 100;

    /* 毫秒 -> 秒 */
    public static final int TIMESTAMP_TO_SECOND = 1000;

    /* 一天24个小时 */
    public static final int DAY_HOUR_NUM = 24;
    /* 一小时3600秒 */
    public static final int HOUR_TO_SECOND = 3600;
    // redis 保存时限 60秒
    public static final int MIN_TO_SECOND = 60;

    /* xxx 比例 */
    public static final double BILL_PERCENT_SHOOTER1 = 0.8;
    /* 游戏ID：35044，mengjia 开头的比例 */
    public static final double BILL_PERCENT_SHOOTER2 = 0.9;

    /* 注册天数  30 */
    public static final int REGIST_DAY_30 = 30;
    /* 注册天数  60 */
    public static final int REGIST_DAY_60 = 60;
    /* 注册天数  90 */
    public static final int REGIST_DAY_90 = 90;

    /* 留存率，天数 */
    public static final int SURVIVAL_DAY_ONE = -1;
    /* 留存率，天数 */
    public static final int SURVIVAL_DAY_TWO = -2;

    /* 留存率，月份 */
    public static final int SURVIVAL_MONTH_ONE = -1;
    /* 留存率，月份 */
    public static final int SURVIVAL_MONTH_TWO = -2;

    /* 游戏：枪手本色 */
    public static final int GAME_ID_SHOOTER = 35044;
    /* 游戏：王者猜词 */
    public static final int GAME_ID_KING = 35049;

    /* 比较图片相似度临界值 */
    public static final int HEAD_DATA_80 = 80;

    /**
     *  也可以添加其他组别
     */
    public interface Role {
        /* 普通用户 */
        int ROLE_CUSTOMER = 0;

        /* 管理员 */
        int ROLE_ADMIN = 1;
    }

}