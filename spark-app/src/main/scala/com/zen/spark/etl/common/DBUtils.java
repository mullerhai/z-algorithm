package com.zen.spark.etl.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * MySQL工具类
 *
 * @author howie
 * @since 2017-03-08
 */
public class DBUtils implements Serializable {
    public DBUtils getConnection() {
        return new DBUtils();
    }

    public List<Map<String, String>> query(String sql) {
        return null;
    }

    public List<List<String>> queryRaw(String sql) {
        return null;
    }

    public Map<String, String> get(String sql) {
        return null;
    }

    public List<String> getRaw(String sql) {
        return null;
    }

    /**
     * 更新
     *
     * @param sql 更新SQL
     * @return 更改的行数
     */
    public int update(String sql) {
        return 0;
    }

    /**
     * 插入
     *
     * @param sql 插入SQL
     * @return 最后一行ID
     */
    public int insert(String sql) {
        return 0;
    }
}
