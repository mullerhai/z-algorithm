package com.zen.spark.etl.util;


import com.zen.spark.etl.common.exception.DefinedException;
import com.zen.spark.etl.conf.PropertiesUtil;
import com.zen.spark.etl.constant.Constants;
import com.zen.spark.etl.jdbc.JDBCHelper;
import org.apache.commons.lang.math.NumberUtils;
import scala.Tuple2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetAdverMoneyUtil {

    //    渠道合并信息-----------------------------------------------------
    //    好彩斗地主广告参数合并
    private static String hcddzInfo = "365you.tfqdzqbhcddz";
    //    头条广告参数合并
    private static List<String> ttggInfo = Arrays.asList("touTiaoAdverChannelId", "365you.nttddzzrb.3004054305", "sougou.ttddzfkb.3004055100", "365you.tfqd0ttddzzrb.3004056553", "365you.tfqd0ttddzzrb.3004053709", "bdxxl.ttddzzrb.3004053690");
    //    消星星广告参数合并
    private static List<String> xxxInfo = Arrays.asList("xxxAdverChannelId", "365you.tfqdxxxhj.5006000104", "365you.tfqdxxxhj.5006000132");
    //    消星星广告参数合并2
    private static List<String> xxxInfo2 = Arrays.asList("xxxAdverChannelId2", "365you.tfqdtthbxxxhj.5006000136", "365you.tfqdxxxhj.5006000109");
    //    OV华广告买量渠道
    private static List<String> ovhwInfo = Arrays.asList("oppo.wbl0ttddzzrb.2200145619", "oppo.wbl0twottddzzrb.2200145619", "oppo.chysg.2200145619", "vivo.klddzshf.2200169810", "vivo.djddzhlb.2200169810", "vivo.jjby.2200169810", "huawei.ttddzzrb0lw.2200109612", "oppo.ksby.2200145619", "oppo.ttddzfkb.2200145619");

    //    广告用户渠道
    public static List<String> adUserChannelIdList = Arrays.asList("oppo.mlttddzzrb.2200145619", "oppo.mltwottddzzrb.2200145619", "oppo.tfqdchysg.2200145619", "vivo.tfqdklddzshf.2200169810", "vivo.tfqddjddzhlb.2200169810", "vivo.tfqdjjby.2200169810", "huawei.tfttddzzrb0lw.2200109612", "oppo.tfqdksby.2200145619");

    //    ios的子广告商收入归入原广告商
    private static List<String> iosAdvertiserIdInfo = Arrays.asList("101", "102", "103", "104");


    //    广告用户渠道转化 A->A'
    public static String adChannelIdChangeInfo(String channelId) {
        String finalChannelId = channelId;
        if ("oppo.wbl0ttddzzrb.2200145619".equals(channelId)) {
            finalChannelId = "oppo.mlttddzzrb.2200145619";
        } else if ("oppo.wbl0twottddzzrb.2200145619".equals(channelId)) {
            finalChannelId = "oppo.mltwottddzzrb.2200145619";
        } else if ("oppo.chysg.2200145619".equals(channelId)) {
            finalChannelId = "oppo.tfqdchysg.2200145619";
        } else if ("vivo.klddzshf.2200169810".equals(channelId)) {
            finalChannelId = "vivo.tfqdklddzshf.2200169810";
        } else if ("vivo.djddzhlb.2200169810".equals(channelId)) {
            finalChannelId = "vivo.tfqddjddzhlb.2200169810";
        } else if ("vivo.jjby.2200169810".equals(channelId)) {
            finalChannelId = "vivo.tfqdjjby.2200169810";
        } else if ("huawei.ttddzzrb0lw.2200109612".equals(channelId)) {
            finalChannelId = "huawei.tfttddzzrb0lw.2200109612";
        } else if ("oppo.ksby.2200145619".equals(channelId)) {
            finalChannelId = "oppo.tfqdksby.2200145619";
        } else if ("oppo.ttddzfkb.2200145619".equals(channelId)) {
            finalChannelId = "oppo.tfqdttddzfkb.2200145619";
        }
        return finalChannelId;
    }

    //    渠道聚合转化方法
    public static String calcChannelIdInfo(String channelId) {
        String calcChannelId = channelId;
        if (channelId.startsWith(hcddzInfo)) {
            calcChannelId = "365you.tfqdzqbhcddz";
        }
        if (ttggInfo.contains(channelId)) {
            calcChannelId = "touTiaoAdverChannelId";
        }
        if (xxxInfo.contains(channelId)) {
            calcChannelId = "xxxAdverChannelId";
        }
        if (xxxInfo2.contains(channelId)) {
            calcChannelId = "xxxAdverChannelId2";
        }
        return calcChannelId;
    }

    //    判断是否是合并渠道
    public static Boolean isCalcChannelIdInfo(String channelId) {
        boolean result = false;
        if (channelId.startsWith(hcddzInfo)) {
            result = true;
        }
        if (ttggInfo.contains(channelId)) {
            result = true;
        }
        if (xxxInfo.contains(channelId)) {
            result = true;
        }
        if (xxxInfo2.contains(channelId)) {
            result = true;
        }
        if (ovhwInfo.contains(channelId)) {
            result = true;
        }
        return result;
    }

    public static String getIOSAdvertiserId(String channelId, String advertiserId) {
        String finalAdvertiserId = advertiserId;
        if (channelId.contains("ios")) {
            if (iosAdvertiserIdInfo.contains(advertiserId)) {
                if ("101".equals(advertiserId) || "102".equals(advertiserId)) {
                    finalAdvertiserId = "22";
                } else if ("103".equals(advertiserId) || "104".equals(advertiserId)) {
                    finalAdvertiserId = "9";
                }
            }
        }
        return finalAdvertiserId;
    }

    //    获取广告收入方法
    public static HashMap<String, String> getAdverMoneyMap(String statDate) {
        HashMap<String, String> adverMoneyMap = new HashMap<>();
        JDBCHelper db = new JDBCHelper();
        String selectSql = String.format("select advertiserId,advertiserTypeId,channelId,sum(money)  from oss.stat_statAdvertisingThridSuper where statDate='%s' group by advertiserId,advertiserTypeId,channelId", statDate);
//        System.out.println(selectSql);
        db.executeQuery(selectSql, null, rs -> {
            while (rs.next()) {
                String advertiserId = rs.getObject(1).toString();
                String advertiserTypeId = rs.getObject(2).toString();
                String channelId = rs.getObject(3).toString();
                String money = rs.getObject(4).toString();
                //开屏广告不分广告商
                if ("3".equals(advertiserTypeId)) {
                    advertiserId = "0";
                }
                //转化渠道
                channelId = calcChannelIdInfo(channelId);

                // ios的子广告商收入归入原广告商
                advertiserId = getIOSAdvertiserId(channelId,advertiserId);

                String key = String.join("|", new String[]{advertiserId, advertiserTypeId, channelId});
                if (adverMoneyMap.containsKey(key)) {
                    String newMoney = String.format("%.2f", (NumberUtils.toFloat(adverMoneyMap.get(key)) + NumberUtils.toFloat(money)));
//                    System.out.println(adverMoneyMap.get(key) + "||" + money + "||" + newMoney);
                    adverMoneyMap.put(key, newMoney);
                } else {
                    adverMoneyMap.put(key, money);
                }
            }
        });
        return adverMoneyMap;
    }

    //每日的广告收入入库
    public static void insertadverMoneyToday(String statDate, String dataTable, HashMap<String, String> adverMoneyMap) throws DefinedException {
        JDBCHelper db = new JDBCHelper();
        List<Object[]> insertParamsList = new ArrayList<>();
        String insertSql = String.format("INSERT INTO %s (`statDate`, `channelId`, `adverMoneyToday`) VALUES (?, ?, ?) on duplicate key update adverMoneyToday=adverMoneyToday+?;",
                dataTable);
        //合并的渠道不需要入库
        for (Map.Entry<String, String> entry : adverMoneyMap.entrySet()) {
            String[] key = entry.getKey().split("\\|");
            String advertiserId = key[0];
            String advertiserTypeId = key[1];
            String channelId = "";
            if (key.length >= 3) {
                channelId = key[2];
            }

            String money = entry.getValue();
            //判断渠道是否是合并渠道，非合并渠道才会进行入库
            boolean isCalcChannelId = isCalcChannelIdInfo(channelId);
            //插入数据库
            if (!isCalcChannelId) {
                Object[] params = new Object[4];
                params[0] = statDate;
                params[1] = channelId;
                params[2] = money;
                params[3] = money;
                insertParamsList.add(params);
            }
        }
        db.executeBatch(insertSql, insertParamsList);
        db.close();
    }

    //通过imei获取userId 从www_12317wan_com.register_info中查询得到
    public static List<Tuple2<String, String>> imeiUserIdList(List<Tuple2<String, String>> selectImeiList) {
//        imeiUserIdList.add(new Tuple2<>("1", "2"));
        List<Tuple2<String, String>> imeiUserIdList = new ArrayList<>();
        String host = PropertiesUtil.getProperty(Constants.JDBC_WebSalve_URL);
        String user = PropertiesUtil.getProperty(Constants.JDBC_USER);
        String password = PropertiesUtil.getProperty(Constants.JDBC_PASSWORD);
        JDBCHelper db = new JDBCHelper(host, user, password);
        List<String> imeiList = new ArrayList<>();
        for (Tuple2<String, String> tuple : selectImeiList) {
            String imei = tuple._1();
            imeiList.add(imei);
            if (imeiList.size() > 5000) {
                String selectSql = String.format("select UserID,IMEI from register_info where IMEI in ('%s')", String.join("','", imeiList));
//                System.out.println(selectSql);
                db.executeQuery(selectSql, null, rs -> {
                    while (rs.next()) {
//                        System.out.println(rs.getObject(2).toString() + "------" + rs.getObject(1).toString());
                        imeiUserIdList.add(new Tuple2<>(rs.getObject(2).toString(), rs.getObject(1).toString()));
                    }
                });
                imeiList.clear();
            }
        }
        if (imeiList.size() > 0) {
            String selectSql = String.format("select UserID,IMEI from register_info where IMEI in ('%s')", String.join("','", imeiList));
            db.executeQuery(selectSql, null, rs -> {
                while (rs.next()) {
                    imeiUserIdList.add(new Tuple2<>(rs.getObject(2).toString(), rs.getObject(1).toString()));
                }
            });
            imeiList.clear();
        }
        db.close();
        return imeiUserIdList;

    }

    //通过账单获取唯一标识 从oss2020.config_touFangPlan中查询得到
    public static HashMap<String, String> getChannelIdandPlanIdMap(List<Tuple2<String, String>> selectSecretKeyList) {
//        imeiUserIdList.add(new Tuple2<>("1", "2"));
        HashMap<String, String> getCPMap = new HashMap<>();
        JDBCHelper db = new JDBCHelper();
        List<String> CPList = new ArrayList<>();
        for (Tuple2<String, String> tuple : selectSecretKeyList) {
            String imei = tuple._1();
            CPList.add(imei);
            if (CPList.size() > 5000) {
                String selectSql = String.format("select secretKey,channelId,touFangPlanId from oss2020.config_touFangPlan where secretKey in ('%s')", String.join("','", CPList));
//                System.out.println(selectSql);
                db.executeQuery(selectSql, null, rs -> {
                    while (rs.next()) {
//                        System.out.println(rs.getObject(2).toString() + "------" + rs.getObject(1).toString());
                        String value = String.join("|", new String[]{rs.getObject(2).toString(), rs.getObject(3).toString()});
                        getCPMap.put(rs.getObject(1).toString(), value);
                    }
                });
                CPList.clear();
            }
        }
        if (CPList.size() > 0) {
            String selectSql = String.format("select secretKey,channelId,touFangPlanId from oss2020.config_touFangPlan where secretKey in ('%s')", String.join("','", CPList));
            db.executeQuery(selectSql, null, rs -> {
                while (rs.next()) {
                    String value = String.join("|", new String[]{rs.getObject(2).toString(), rs.getObject(3).toString()});
                    getCPMap.put(rs.getObject(1).toString(), value);
                }
            });
            CPList.clear();
        }
        db.close();
        return getCPMap;
    }

    //通过userId, channelId, gameId，在DataStat.userInfoChannelId表里查找对应的用户渠道注册日期
    public static List<Tuple2<String, String>> getUserRegistDateListInfo(List<Tuple2<String, String>> findUserInfoList) {
        List<Tuple2<String, String>> userRegistDateList = new ArrayList<>();
        JDBCHelper db = new JDBCHelper("jdbc:mysql://rsyslog-proxy4:3306","oss","oss");
        List<String> sqlList = new ArrayList<>();
        for (Tuple2<String, String> tuple : findUserInfoList) {
            String[] data = tuple._1().split("\\|");
            String gameId = data[0];
            String userId = data[1];
            String channelId = data[2];
            String selectSql = String.format("select gameId,userId,channelId,registDate from DataStat.userInfoChannelId where userId=%s and channelId='%s' and gameId=%s", userId, channelId, gameId);
            sqlList.add(selectSql);
            if (sqlList.size() > 5000) {
                String sqlLists = String.join(" union ", sqlList);
                db.executeQuery(sqlLists, null, rs -> {
                    while (rs.next()) {
                        String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                        userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                    }
                });
                sqlList.clear();
            }
        }
        if (sqlList.size() > 0) {
            String sqlLists = String.join(" union ", sqlList);
            db.executeQuery(sqlLists, null, rs -> {
                while (rs.next()) {
                    String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                    userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                }
            });
            sqlList.clear();
        }
        db.close();
        return userRegistDateList;
    }
    //通过userId, channelId, gameId，在DataStat.userInfoChannelId表里查找对应的用户渠道注册日期
    public static List<Tuple2<String, String>> getUserRegistDateListInfoBak(List<Tuple2<String, String>> findUserInfoList) {
        List<Tuple2<String, String>> userRegistDateList = new ArrayList<>();
        JDBCHelper db = new JDBCHelper("jdbc:mysql://rsyslog-proxy4:3306","oss","oss");
        List<String> sqlList = new ArrayList<>();
        for (Tuple2<String, String> tuple : findUserInfoList) {
            String[] data = tuple._1().split("\\|");
            String gameId = data[0];
            String userId = data[1];
            String channelId = data[2];
            String selectSql = String.format("select gameId,userId,channelId,registDate from DataStat.userInfoChannelId where userId=%s and channelId='%s' and gameId=%s", userId, channelId, gameId);
            sqlList.add(selectSql);
            if (sqlList.size() > 5000) {
                String sqlLists = String.join(" union ", sqlList);
                db.executeQuery(sqlLists, null, rs -> {
                    while (rs.next()) {
                        String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                        userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                    }
                });
                sqlList.clear();
            }
        }
        if (sqlList.size() > 0) {
            String sqlLists = String.join(" union ", sqlList);
            db.executeQuery(sqlLists, null, rs -> {
                while (rs.next()) {
                    String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                    userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                }
            });
            sqlList.clear();
        }
        db.close();
        return userRegistDateList;
    }

    //通过userId, channelId, gameId，在DataStat.userInfo表里查找对应的用户注册日期
    public static List<Tuple2<String, String>> getUserRegistInfo(List<Tuple2<String, String>> findUserInfoList) {
        List<Tuple2<String, String>> userRegistDateList = new ArrayList<>();
        JDBCHelper db = new JDBCHelper("jdbc:mysql://rsyslog-proxy4:3306","oss","oss");
        List<String> sqlList = new ArrayList<>();
        for (Tuple2<String, String> tuple : findUserInfoList) {
            String[] data = tuple._1().split("\\|");
            String gameId = data[0];
            String userId = data[1];
            String channelId = data[2];
            String selectSql = String.format("select gameId,userId,registChannel,registDate from DataStat.userInfo where userId=%s and registChannel='%s' and gameId=%s", userId, channelId, gameId);
            sqlList.add(selectSql);
            if (sqlList.size() > 5000) {
                String sqlLists = String.join(" union ", sqlList);
                db.executeQuery(sqlLists, null, rs -> {
                    while (rs.next()) {
                        String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                        userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                    }
                });
                sqlList.clear();
            }
        }
        if (sqlList.size() > 0) {
            String sqlLists = String.join(" union ", sqlList);
            db.executeQuery(sqlLists, null, rs -> {
                while (rs.next()) {
                    String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                    userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                }
            });
            sqlList.clear();
        }
        db.close();
        return userRegistDateList;

    }

    public static List<Tuple2<String, String>> getUserRegistInfoBak(List<Tuple2<String, String>> findUserInfoList) {
        List<Tuple2<String, String>> userRegistDateList = new ArrayList<>();
        JDBCHelper db = new JDBCHelper("jdbc:mysql://rsyslog-proxy4:3306","oss","oss");
        List<String> sqlList = new ArrayList<>();
        for (Tuple2<String, String> tuple : findUserInfoList) {
            String[] data = tuple._1().split("\\|");
            String gameId = data[0];
            String userId = data[1];
            String channelId = data[2];
            String selectSql = String.format("select gameId,userId,registChannel,registDate from DataStat.userInfo where userId=%s and registChannel='%s' and gameId=%s", userId, channelId, gameId);
            sqlList.add(selectSql);
            if (sqlList.size() > 5000) {
                String sqlLists = String.join(" union ", sqlList);
                db.executeQuery(sqlLists, null, rs -> {
                    while (rs.next()) {
                        String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                        userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                    }
                });
                sqlList.clear();
            }
        }
        if (sqlList.size() > 0) {
            String sqlLists = String.join(" union ", sqlList);
            db.executeQuery(sqlLists, null, rs -> {
                while (rs.next()) {
                    String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString(), rs.getObject(3).toString()});
                    userRegistDateList.add(new Tuple2<>(key, rs.getObject(4).toString()));
                }
            });
            sqlList.clear();
        }
        db.close();
        return userRegistDateList;

    }

    //通过userId, gameId，在DataStat.userInfo表里查找对应的用户注册日期
    public static List<Tuple2<String, String[]>> getUserRegistPriInfo(List<Tuple2<String, String>> findUserInfoList) {
        List<Tuple2<String, String[]>> userRegistDateList = new ArrayList<>();
//        JDBCHelper db = new JDBCHelper();
        JDBCHelper db = new JDBCHelper("jdbc:mysql://rsyslog-proxy4:3306","oss","oss");
        List<String> sqlList = new ArrayList<>();
        for (Tuple2<String, String> tuple : findUserInfoList) {
            String[] data = tuple._1().split("\\|");
            String gameId = data[0];
            String userId = data[1];
            String selectSql = String.format("select gameId,userId,registDate,totalMoney from DataStat.userInfo where userId=%s and gameId=%s", userId, gameId);
            sqlList.add(selectSql);
            if (sqlList.size() > 5000) {
                String sqlLists = String.join(" union ", sqlList);
                db.executeQuery(sqlLists, null, rs -> {
                    while (rs.next()) {
                        String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString()});
                        String[] val = {rs.getObject(3).toString(),rs.getObject(4).toString()};
                        userRegistDateList.add(new Tuple2<>(key, val));
                    }
                });
                sqlList.clear();
            }
        }
        if (sqlList.size() > 0) {
            String sqlLists = String.join(" union ", sqlList);
            db.executeQuery(sqlLists, null, rs -> {
                while (rs.next()) {
                    String key = String.join("|", new String[]{rs.getObject(1).toString(), rs.getObject(2).toString()});
                    String[] val = {rs.getObject(3).toString(),rs.getObject(4).toString()};
                    userRegistDateList.add(new Tuple2<>(key, val));
                }
            });
            sqlList.clear();
        }
        db.close();
        return userRegistDateList;

    }

    //通过userId在DataStat.userInfo表里查找对应的用户注册日期和消费总额
    public static List<Tuple2<String, String[]>> getAlgoUserRegistPriInfo(List<String> findUserInfoList) {
        List<Tuple2<String, String[]>> userRegistDateList = new ArrayList<>();
//        JDBCHelper db = new JDBCHelper();
        JDBCHelper db = new JDBCHelper("jdbc:mysql://rsyslog-proxy4:3306","oss","oss");
        List<String> sqlList = new ArrayList<>();
        for (String userId : findUserInfoList) {
            String selectSql = String.format("select userId,registDate,totalMoney,gameId from DataStat.userInfo where userId=%s", userId);
            sqlList.add(selectSql);
            if (sqlList.size() > 5000) {
                String sqlLists = String.join(" union ", sqlList);
                db.executeQuery(sqlLists, null, rs -> {
                    while (rs.next()) {
                        String key = String.join("|", new String[]{rs.getObject(1).toString()});
                        String[] val = {rs.getObject(2).toString(),rs.getObject(3).toString(),rs.getObject(4).toString()};
                        userRegistDateList.add(new Tuple2<>(key, val));
                    }
                });
                sqlList.clear();
            }
        }
        if (sqlList.size() > 0) {
            String sqlLists = String.join(" union ", sqlList);
            db.executeQuery(sqlLists, null, rs -> {
                while (rs.next()) {
                    String key = String.join("|", new String[]{rs.getObject(1).toString()});
                    String[] val = {rs.getObject(2).toString(),rs.getObject(3).toString(),rs.getObject(4).toString()};
                    userRegistDateList.add(new Tuple2<>(key, val));
                }
            });
            sqlList.clear();
        }
        db.close();
        return userRegistDateList;

    }

    public static int between_days(String smdate, String bdate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(smdate));

        long time1 = cal.getTimeInMillis();

        cal.setTime(sdf.parse(bdate));

        long time2 = cal.getTimeInMillis();

        long between_daysNum = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_daysNum));

    }

    public static int between_days2(String smdate, String bdate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(smdate));

        long time1 = cal.getTimeInMillis();

        cal.setTime(sdf.parse(bdate));

        long time2 = cal.getTimeInMillis();

        long between_daysNum = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_daysNum));

    }

//    public static int between_days(String a, String b) {
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 自定义时间格式
//
//        Calendar calendar_a = Calendar.getInstance();// 获取日历对象
//        Calendar calendar_b = Calendar.getInstance();
//
//        Date date_a = null;
//        Date date_b = null;
//
//        try {
//            date_a = simpleDateFormat.parse(a);//字符串转Date
//            date_b = simpleDateFormat.parse(b);
//            calendar_a.setTime(date_a);// 设置日历
//            calendar_b.setTime(date_b);
//        } catch (ParseException e) {
//            e.printStackTrace();//格式化异常
//        }
//
//        long time_a = calendar_a.getTimeInMillis();
//        long time_b = calendar_b.getTimeInMillis();
//
//        int between_days = (int) (time_a - time_b) / (1000 * 3600 * 24);//计算相差天数
//
//        return between_days;
//    }

}
