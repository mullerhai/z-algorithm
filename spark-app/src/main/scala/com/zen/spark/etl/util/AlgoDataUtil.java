package com.zen.spark.etl.util;

import com.zen.spark.etl.common.exception.DefinedException;
import com.zen.spark.etl.jdbc.JDBCHelper;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static org.apache.hadoop.hbase.io.crypto.KeyProvider.PASSWORD;

public class AlgoDataUtil {
    // 得到系统对应的ID
    public static HashMap<String, String> getSystemTypeID() {
        HashMap<String, String> systemTypeIDMap = new HashMap<>();
        systemTypeIDMap.put("ios", "1");
        systemTypeIDMap.put("android", "2");
        return systemTypeIDMap;
    }

    //用户当前网络情况
    public static HashMap<String, String> getNetTypeID() {
        HashMap<String, String> systemTypeIDMap = new HashMap<>();
        systemTypeIDMap.put("无连接", "0");
        systemTypeIDMap.put("WIFI", "1");
        systemTypeIDMap.put("2G", "2");
        systemTypeIDMap.put("3G", "3");
        systemTypeIDMap.put("4G", "4");
        systemTypeIDMap.put("5G", "5");
        return systemTypeIDMap;
    }

    // 省份，城市，设备类型等入库并返回ID 通用方法
    public static HashMap<String, String> getElementID(String elementName, String dataTable, List<String> provinceInfo) throws DefinedException {
        JDBCHelper db = new JDBCHelper();
        HashMap<String, String> provinceMap = new HashMap<>();

        // 1、遍历省份list
        for (String province : provinceInfo) {
            if ((!"".equals(province)) && (!"796126334".equals(province))) {
                // 2、表里不存在就插入
                String selectSql = String.format("select count(1) as count from %s where %s = '%s';", dataTable, elementName, province);
                int count = NumberUtils.toInt(db.query(selectSql).get(0).get("count").toString());
//            System.out.println(count);
                if (count == 0) {
                    String insertSql = String.format("insert into %s(%s) value('%s');", dataTable, elementName, province);
                    db.update(insertSql);
                }
            }

        }
        // 3、取出表里所有的要拿到的名字和对应的id，并组成HashMap返回
        String selectSql = String.format("select id,%s from %s;", elementName, dataTable);
        db.executeQuery(selectSql, null, rs -> {
            while (rs.next()) {
                provinceMap.put(rs.getObject(2).toString(), rs.getObject(1).toString());
            }
        });
        db.close();
        return provinceMap;
    }

    // 游戏Id入库获取编号
    public static HashMap<String, String> getGameID(String elementName, String dataTable) throws DefinedException {
        JDBCHelper db = new JDBCHelper();
        List<String> config_gameIdList = new ArrayList<>();

        HashMap<String, String> gameIdMap = new HashMap<>();

        // 0、获取所有的gameId
        String selectSql = "select gameId from oss.config_game;";
        db.executeQuery(selectSql, null, rs -> {
            while (rs.next()) {
                config_gameIdList.add(rs.getObject(1).toString());
            }
        });

        // 1、遍历省份list
        for (String province : config_gameIdList) {
            // 2、表里不存在就插入
            String selectSql1 = String.format("select count(1) as count from %s where %s = '%s';", dataTable, elementName, province);
            int count = NumberUtils.toInt(db.query(selectSql1).get(0).get("count").toString());
//            System.out.println(count);
            if (count == 0) {
                String insertSql = String.format("insert into %s(%s) value('%s');", dataTable, elementName, province);
                db.update(insertSql);
            }
        }
        // 3、取出表里所有的要拿到的名字和对应的id，并组成HashMap返回
        String selectSql2 = String.format("select id,%s from %s;", elementName, dataTable);
        db.executeQuery(selectSql2, null, rs -> {
            while (rs.next()) {
                gameIdMap.put(rs.getObject(2).toString(), rs.getObject(1).toString());
            }
        });
        db.close();
        return gameIdMap;
    }

    public static List<String[]> getAdMysqlInfo() throws SQLException {
        List<String[]> adMysqlInfo = new ArrayList<>();
        String URL = "jdbc:mysql://ad_mysql:3306";
        Properties info = new Properties();
        info.setProperty("user", "oss");
        info.setProperty("password", "WXyB9A2&4K");
//        System.out.println("开始连接");
        JDBCHelper db = new JDBCHelper(URL, info);
//        System.out.println("连接成功");
//        JDBCHelper db = new JDBCHelper("jdbc:mysql://ad_mysql:3306","oss","WXyB9A2&4K");
        String selectSql = "SELECT a.videoId, a.cmtlId, b.accountId, b.projectId FROM zengame_ssp.ssp_adplan_creative_mtl a LEFT JOIN zengame_ssp.ssp_adplan b ON a.adId = b.adId;";
//        System.out.println(selectSql);
        db.executeQuery(selectSql, null, rs -> {
            while (rs.next()) {
                String[] val = {rs.getObject(2).toString(), rs.getObject(3).toString(), rs.getObject(4).toString(), rs.getObject(1).toString()};
                adMysqlInfo.add(val);
            }
        });
        db.close();
        return adMysqlInfo;
    }




    public static boolean isSmallDate(String date1, String date2) {
        return NumberUtils.toInt(date1.replace("-", "")) < NumberUtils.toInt(date2.replace("-", ""));
    }


}
