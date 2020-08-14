package com.zen.spark.etl.jdbc;

import com.zen.spark.etl.common.exception.DefinedException;
import com.zen.spark.etl.conf.PropertiesUtil;
import com.zen.spark.etl.constant.Constants;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.*;
import java.util.*;

/**
 * JDBC辅助组件
 *
 * @author tony
 * @time 2017-03-08
 */
public class JDBCHelper {

	static {
		try {
			String driver = PropertiesUtil.getProperty(Constants.JDBC_DRIVER);
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection conn = null;

	public JDBCHelper() {
		boolean local = PropertiesUtil.getBoolean(Constants.SPARK_LOCAL);
		String url = null;
		String user = null;
		String password = null;

		if (local) {
			url = PropertiesUtil.getProperty(Constants.JDBC_URL_TEST);
			user = PropertiesUtil.getProperty(Constants.JDBC_USER_TEST);
			password = PropertiesUtil.getProperty(Constants.JDBC_PASSWORD_TEST);
		} else {
			url = PropertiesUtil.getProperty(Constants.JDBC_URL);
			user = PropertiesUtil.getProperty(Constants.JDBC_USER);
			password = PropertiesUtil.getProperty(Constants.JDBC_PASSWORD);
		}

		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public JDBCHelper(String db) {
		boolean local = PropertiesUtil.getBoolean(Constants.SPARK_LOCAL);
		String url = null;
		String user = null;
		String password = null;
		if (local) {
			url = PropertiesUtil.getProperty(Constants.JDBC_URL_TEST);
			user = PropertiesUtil.getProperty(Constants.JDBC_USER_TEST);
			password = PropertiesUtil.getProperty(Constants.JDBC_PASSWORD_TEST);
		} else {
			String dbMysql = PropertiesUtil.getProperty(Constants.JDBC_MYSQL_DATABASE);
			String dbWeb = PropertiesUtil.getProperty(Constants.JDBC_WEB_DATABASE);
			String dataStat = PropertiesUtil.getProperty(Constants.JDBC_DATASTAT_DATABASE);
			String dataWebSlave = PropertiesUtil.getProperty(Constants.JDBC_WebSalve_DATABASE);
			String dataCsGameS2 = PropertiesUtil.getProperty(Constants.JDBC_csGameS2_DATABASE);
			String ddz_db = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_DATABASE);
			String ddz_db_odd = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_odd_DATABASE);
			String mj_db = PropertiesUtil.getProperty(Constants.JDBC_mj_db_DATABASE);
			String gxl_db = PropertiesUtil.getProperty(Constants.JDBC_gxl_db_DATABASE);
			if (null != db && dbWeb.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_WEB_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_PASSWORD);
			} else if (null != db && dbMysql.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_PASSWORD);
			} else if (null != db && dataStat.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_PASSWORD);
			} else if (null != db && dataWebSlave.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_WebSalve_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_WebSalve_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_WebSalve_PASSWORD);
			} else if (null != db && dataCsGameS2.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_csGameS2_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_csGameS2_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_csGameS2_PASSWORD);
			} else if (null != db && ddz_db.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_PASSWORD);
			} else if (null != db && mj_db.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_mj_db_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_mj_db_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_mj_db_PASSWORD);
			} else if (null != db && ddz_db_odd.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_odd_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_odd_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_ddz_db_odd_PASSWORD);
			} else if (null != db && gxl_db.equalsIgnoreCase(db)) {
				url = PropertiesUtil.getProperty(Constants.JDBC_gxl_db_URL);
				user = PropertiesUtil.getProperty(Constants.JDBC_gxl_db_USER);
				password = PropertiesUtil.getProperty(Constants.JDBC_gxl_db_PASSWORD);
			}
		}

		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public JDBCHelper(String host, String user, String password) {
		try {
			conn = DriverManager.getConnection(host, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JDBCHelper(String host, Properties info) {
		try {
			conn = DriverManager.getConnection(host, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return conn;
	}

	public int[] goBatch(List<String> insertSqlList) throws DefinedException {
		if (insertSqlList == null) {
			return null;
		}
		Statement sm = null;
		try {
			sm = conn.createStatement();
			for (String sql : insertSqlList) {
				sm.addBatch(sql);
			}
			// 一次执行多条SQL语句
			return sm.executeBatch();
		} catch (SQLException e) {
			System.out.println("错误语句是：" + ArrayUtils.toString(insertSqlList));
			e.printStackTrace();
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			try {
				sm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public int[] executeBatchCloseConn(String sql, List<Object[]> paramsList) throws DefinedException {

		int[] rtn = null;
		PreparedStatement pstmt = null;
		try {
			// 第一步：使用Connection对象，取消自动提交
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			// 第二步：使用PreparedStatement.addBatch()方法加入批量的SQL参数
			if (paramsList != null && paramsList.size() > 0) {
				for (Object[] params : paramsList) {
					for (int i = 0; i < params.length; i++) {
						pstmt.setObject(i + 1, params[i]);
					}
					pstmt.addBatch();
				}
			}
			// 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
			rtn = pstmt.executeBatch();
			// 最后一步：使用Connection对象，提交批量的SQL语句
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rtn;
	}

	public int executeQuery(String sql) throws SQLException, DefinedException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修改
	 * @param pstmtInsert
	 * @param pstmtUpdate
	 * @throws SQLException
	 * @throws DefinedException
	 */
	public void executeAndClose(PreparedStatement pstmtInsert, PreparedStatement pstmtUpdate) throws SQLException, DefinedException {
		try {
			// 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
			pstmtInsert.executeBatch();
			pstmtUpdate.executeBatch();

			// 最后一步：使用Connection对象，提交批量的SQL语句
			conn.commit();
		} catch (Exception e) {
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			if (null != pstmtInsert) {
				pstmtInsert.close();
			}
			if (null != pstmtUpdate) {
				pstmtUpdate.close();
			}
			if (null != conn) {
				conn.close();
			}
		}
	}

	/**
	 * 修改
	 * @param sql
	 * @param paramsList
	 * @return
	 * @throws DefinedException
	 */
	public int[] executeBatch(String sql, List<Object[]> paramsList) throws DefinedException {
		int[] rtn = null;
		PreparedStatement pstmt = null;
		try {
			// 第一步：使用Connection对象，取消自动提交
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);

			// 第二步：使用PreparedStatement.addBatch()方法加入批量的SQL参数
			if (paramsList != null && paramsList.size() > 0) {
				for (Object[] params : paramsList) {
					for (int i = 0; i < params.length; i++) {
						pstmt.setObject(i + 1, params[i]);
					}
					pstmt.addBatch();
				}
			}
			// 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
			rtn = pstmt.executeBatch();

			// 最后一步：使用Connection对象，提交批量的SQL语句
			conn.commit();
		} catch (Exception e) {
			System.err.println(paramsList.toString());
			e.printStackTrace();
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rtn;
	}

	public int update(String sql) {
		return executeUpdate(sql);
	}

	public int executeUpdate(String sql) {
		int rtn = 0;
		PreparedStatement pstmt = null;
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			rtn = pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rtn;
	}

	public int executeUpdate(String sql, Object[] params) {
		int rtn = 0;
		PreparedStatement pstmt = null;
		try {

			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
			rtn = pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rtn;
	}

	public void executeQuery(String sql, QueryCallback callback) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			callback.process(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void executeQuery(String sql, Object[] params, QueryCallback callback) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);

			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
			rs = pstmt.executeQuery();

			callback.process(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 静态内部类：查询回调接口
	 *
	 * @author Administrator
	 */
	public static interface QueryCallback {

		/**
		 * 处理查询结果
		 *
		 * @param rs
		 * @throws Exception
		 */
		void process(ResultSet rs) throws Exception;

	}

	/**
	 * 修改
	 * @param pstmtInsert
	 * @throws DefinedException 
	 */
	public void executeAndClose(PreparedStatement pstmtInsert) throws DefinedException {
		try {
			// 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
			pstmtInsert.executeBatch();
			// 最后一步：使用Connection对象，提交批量的SQL语句
			conn.commit();
		} catch (Exception e) {
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			try {
				if (null != pstmtInsert) {
					pstmtInsert.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Map<String, Object>> query(String sql) {
		List<Map<String, Object>> result = new ArrayList<>();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i < columnCount + 1; i++) {
					row.put(metaData.getColumnLabel(i), rs.getObject(i));
				}
				result.add(row);
			}
		} catch (Exception e) {
			System.out.println("错误SQL:" + sql);
			System.out.println("错误信息:" + e);
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println("错误SQL:" + sql);
				e.printStackTrace();
			}
		}
		return result;
	}

	public Map<String, Object> query(String sql, String key, String value) throws DefinedException {
		Map<String, Object> result = new HashMap<>();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				// Map<String, Object> row = new HashMap<>();
				String keys = "";
				Object values = "";
				for (int i = 1; i < columnCount + 1; i++) {
					if (metaData.getColumnLabel(i).equals(key)) {
						keys = rs.getObject(i).toString();
					}
					if (metaData.getColumnLabel(i).equals(value)) {
						values = rs.getObject(i);
					}
				}
				result.put(keys, values);
			}
		} catch (Exception e) {
			System.out.println("错误SQL:" + sql);
			e.printStackTrace();
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println("错误SQL:" + sql);
				e.printStackTrace();
			}
		}
		return result;
	}

	// 查询条数
	public int searchNumber(String sql) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.last(); // 将光标移动到最后一行
			int rowCount = rs.getRow(); // 得到当前行号，即结果集记录数
			return rowCount;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	// 返回 ResultSet 自己操作
	public ResultSet searchReturnResultSet(String sql) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close(Connection conn) throws SQLException {
		conn.close();
	}
	
	public void close(Connection conn, PreparedStatement pstmtUpdate) throws SQLException, DefinedException {
		try {
			// 第三步:使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
			pstmtUpdate.executeBatch();

			// 最后一步：使用Connection对象，提交批量的SQL语句
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			if (null != pstmtUpdate) {
				pstmtUpdate.close();
			}
			if (null != conn) {
				conn.close();
			}
		}
	}

	 public void close(Connection conn, PreparedStatement pstmtUpdate, String clssName) throws SQLException, DefinedException {
			try{
	    		//第三步:使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
				pstmtUpdate.executeBatch();

	            // 最后一步：使用Connection对象，提交批量的SQL语句
	            conn.commit();
	        }catch(Exception e){
	            e.printStackTrace();
	            throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage(),clssName);
	        }finally{
	        	if( null != pstmtUpdate){
	        		pstmtUpdate.close();
	        	}
	            if(null != conn){
	                conn.close();
	            }
	        }
		}
	
	/**
	 * 修改
	 * @param conn2
	 * @param pstmtInsert
	 * @throws SQLException
	 * @throws DefinedException
	 */
	public void executeAndClose(Connection conn2, PreparedStatement pstmtInsert) throws SQLException, DefinedException {
		try {
			// 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
			pstmtInsert.executeBatch();

			// 最后一步：使用Connection对象，提交批量的SQL语句
			conn2.commit();
		} catch (Exception e) {
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			if (null != pstmtInsert) {
				pstmtInsert.close();
			}
			if (null != conn2) {
				conn2.close();
			}
			if (null != conn) {
				conn.close();
			}
		}

	}

}
