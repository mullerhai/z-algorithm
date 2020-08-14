package com.zen.spark.etl.common;

import com.zen.spark.etl.common.exception.DefinedException;
import com.zen.spark.etl.constant.Constants;
import com.zen.spark.etl.util.CommonHdfsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaPairRDD;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * 
 * @Description:hbase的工具类
 * 
 * @author:tony
 * 
 * @time:2017年3月7日 下午8:06:50
 */
public class HBaseClient {
	private static Log log = LogFactory.getLog(HBaseClient.class);

	private static HBaseClient instance = null;
	private static Connection connection;

	private HBaseClient() {
	}

	public static HBaseClient getInstance(Properties props) {
		if (instance == null) {
			synchronized (HBaseClient.class) {
				if (instance == null) {
					instance = new HBaseClient();
					instance.init(props);
				}
			}
		}

		if (connection == null || connection.isClosed()) {
			synchronized (HBaseClient.class) {
				if (connection == null || connection.isClosed()) {
					instance.init(props);
				}
			}
		}

		return instance;
	}

	public void init(Properties prop) {
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.property.clientPort", prop.getProperty(Constants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT));
		config.set("hbase.zookeeper.quorum", prop.getProperty(Constants.HBASE_ZOOKEEPER_QUORUM));
		try {
			ExecutorService pool = Executors.newFixedThreadPool(100);//建立一个数量为100的线程池 
			connection = ConnectionFactory.createConnection(config,pool);
		} catch (Exception e) {
			log.error("create hbase connetion error!", e);
		}
	}

	public Table getTable(String tableName) {
		Table table = null;
		try {
			table = connection.getTable(TableName.valueOf(tableName));
		} catch (Exception e) {
			table = null;
			log.error("get habse table error,tableName=" + tableName, e);
		}

		return table;
	}

	public static void closeTable(Table table) {
		if (table != null) {
			try {
				table.close();
			} catch (Exception e) {
				log.error("close table error,tableName=" + table.getName(), e);
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void close() {
		if (connection != null && !connection.isClosed()) {
			try {
				connection.close();
			} catch (IOException e) {
				log.error("close hbase connect error", e);
			}
		}
	}

	/**
	 * 用spark读取RDD中的数据
	 * 
	 * @param sc
	 * @param hbaseConf
	 * @param tablename
	 * @param scan
	 * @return
	 */
//	public static JavaRDD<Result> readFromHbase(JavaSparkContext sc, Configuration hbaseConf, String tablename, Scan scan) {
//
//		try {
//			ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
//			String ScanToString = Base64.encodeBytes(proto.toByteArray());
//			hbaseConf.set(TableInputFormat.SCAN, ScanToString);
//			hbaseConf.set(TableInputFormat.INPUT_TABLE, tablename);
//			JavaPairRDD<ImmutableBytesWritable, Result> hbaseRDD = sc.newAPIHadoopRDD(hbaseConf, TableInputFormat.class, ImmutableBytesWritable.class,
//					Result.class);
//			return hbaseRDD.map(x -> x._2());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	public static JavaRDD<Result> readFromHbase(JavaSparkContext sc, Configuration hbaseConf, String tablename) {
//
//		try {
//			hbaseConf.set(TableInputFormat.INPUT_TABLE, tablename);
//			JavaPairRDD<ImmutableBytesWritable, Result> hbaseRDD = sc.newAPIHadoopRDD(hbaseConf, TableInputFormat.class, ImmutableBytesWritable.class,
//					Result.class);
//			return hbaseRDD.map(x -> x._2());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//
//	}

	/**
	 * 将RDD中的数据保存到Hbase中，通过写入到HFile，适合大数量数据的写操作
	 * 
	 * @param hbaseconf
	 * @param tableName
	 *            hbase 表
	 * @param hfileRdd
	 *            插入hbase的rdd
	 * @param hfilePath
	 *            生成hfile的hdfs目录
	 * @throws DefinedException 
	 */
	@SuppressWarnings("deprecation")
	public static void saveToHbaseByHFile(Configuration hbaseconf, String tableName, JavaPairRDD<ImmutableBytesWritable, KeyValue> hfileRdd,
			String hfilePath) throws DefinedException {
		CommonHdfsUtil hdfs = CommonHdfsUtil.getInstance();
		try {
			if (hdfs.isExists(hfilePath)) {
				hdfs.deleteHDFSFile(hfilePath); // 如果输出路径存在，就将其删除
			}
			hbaseconf.set(TableOutputFormat.OUTPUT_TABLE, tableName);
			hbaseconf.setInt("hbase.mapreduce.bulkload.max.hfiles.perRegion.perFamily", 2048);
			Job job = Job.getInstance(hbaseconf);
			job.setMapOutputKeyClass(ImmutableBytesWritable.class);
			job.setMapOutputValueClass(KeyValue.class);
			HTable myTable = new HTable(hbaseconf, tableName);
			HFileOutputFormat.configureIncrementalLoad(job, myTable);
			hfileRdd.saveAsNewAPIHadoopFile(hfilePath, ImmutableBytesWritable.class, KeyValue.class, HFileOutputFormat.class, hbaseconf);
			// 利用bulk load hfile
			LoadIncrementalHFiles bulkLoader = new LoadIncrementalHFiles(hbaseconf);
			bulkLoader.doBulkLoad(new Path(hfilePath), myTable);

		} catch (Exception e) {
			throw new DefinedException(String.valueOf(e.hashCode()), e.getMessage());
		} finally {
			if (hdfs.isExists(hfilePath)) {
				hdfs.deleteHDFSFile(hfilePath); // 如果输出路径存在，就将其删除
			}
		}
	}

	/**
	 * @description 创建表有一个列族
	 *
	 * @param tableName
	 * @param columnfamily
	 * @author zhengxinbo
	 */
	public boolean createTable(Connection conn, String tableName, String columnfamily) {
		Admin admin = null;
		boolean bool = true;
		try {
			admin = conn.getAdmin();
			if (admin.tableExists(TableName.valueOf(tableName))) {
				log.info(tableName + " table is exist ");

			} else {
				HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
				HColumnDescriptor hcr = new HColumnDescriptor(columnfamily);
				desc.addFamily(hcr);
				admin.createTable(desc);
				log.info("Create table " + tableName + " successful!");

			}
			return bool;
		} catch (IOException e) {
			log.info("Create table " + tableName + " error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (admin != null) {
					admin.close();
					admin = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 批量创建表
	 *
	 * @param tableNames
	 * @param columnfamily
	 * @author zhengxinbo
	 */
	public boolean createTable(String[] tableNames, String columnfamily) {
		Admin admin = null;
		boolean bool = true;
		try {
			admin = connection.getAdmin();
			for (String tableName : tableNames) {
				if (admin.tableExists(TableName.valueOf(tableName))) {
					log.info(tableName + " table is exist");
				} else {
					HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
					HColumnDescriptor hcr = new HColumnDescriptor(columnfamily);
					desc.addFamily(hcr);
					admin.createTable(desc);
					log.info("Create table " + tableName + " successful!");
				}
			}
			return bool;
		} catch (IOException e) {
			log.info("Create table error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (admin != null) {
					admin.close();
					admin = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 创建表有多个列族
	 *
	 * @param tableName
	 * @param columnfamilys
	 *            void
	 * @author zhengxinbo
	 */
	public void createTable(String tableName, String[] columnfamilys) {
		Admin admin = null;
		try {
			admin = connection.getAdmin();
			if (admin.tableExists(TableName.valueOf(tableName))) {
				log.info(tableName + " table is exist");
				return;
			} else {
				HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
				for (int i = 0; i < columnfamilys.length; i++) {
					desc.addFamily(new HColumnDescriptor(columnfamilys[i]));
				}
				admin.createTable(desc);
				log.info("create table" + tableName + " sucess!");
			}

		} catch (IOException e) {
			log.info("create table" + tableName + " error!");
			e.printStackTrace();
		} finally {
			try {
				if (admin != null) {
					admin.close();
					admin = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 删除表
	 *
	 * @param tableName
	 *            void
	 * @author zhengxinbo
	 */
	public boolean dropTable(String tableName) {
		boolean bool = true;
		Admin admin = null;
		try {
			admin = connection.getAdmin();
			if (admin.tableExists(TableName.valueOf(tableName))) {
				admin.disableTable(TableName.valueOf(tableName));
				admin.deleteTable(TableName.valueOf(tableName));
				log.info(tableName + " drop successfully");
			} else {
				log.info(tableName + " table does not exist and cannot be deleted!");
			}
			return bool;
		} catch (IOException e) {
			log.info("delete table " + tableName + " error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (admin != null) {
					admin.close();
					admin = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 批量删除表
	 *
	 * @param tableNames
	 *            void
	 * @author zhengxinbo
	 */
	public boolean dropTable(String[] tableNames) {
		boolean bool = true;
		Admin admin = null;
		try {
			admin = connection.getAdmin();
			for (String tableName : tableNames) {
				if (admin.tableExists(TableName.valueOf(tableName))) {
					admin.disableTable(TableName.valueOf(tableName));
					admin.deleteTable(TableName.valueOf(tableName));
					log.info(tableName + " drop successfully");
				} else {
					log.info(tableName + " table does not exist and cannot be deleted!");
				}
			}
			return bool;
		} catch (IOException e) {
			log.info("delete table error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (admin != null) {
					admin.close();
					admin = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 往表中插入一列数据
	 *
	 * @param tableName
	 *            --表名
	 * @param rowkey
	 *            --行键
	 * @param family
	 *            --列族
	 * @param qualifier
	 *            --列
	 * @param value
	 *            --列值
	 * @author zhengxinbo
	 */
	public boolean insertColumnData(String tableName, String rowkey, String family, String qualifier, String value) {
		Table table = null;
		try {
			table = getTable(tableName);
			if (table == null) {
				return false;
			} else {
				Put put = getPut(rowkey, family, qualifier, value);
				table.put(put);
				log.info("Insert table:" + tableName + ",rowkye:" + rowkey + ",column:" + qualifier + " successfully");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Insert table:" + tableName + ",rowkye:" + rowkey + ",column:" + qualifier + " error!");
			return false;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 构造一个put
	 *
	 * @param rowkey
	 *            --行键
	 * @param family
	 *            --列族
	 * @param qualifier
	 *            --列
	 * @param value
	 *            --列值
	 * @return Put
	 * @author zhengxinbo
	 */
	public Put getPut(String rowkey, String family, String qualifier, String value) {
		Put put = new Put(rowkey.getBytes());
		try {
			put.addColumn(family.getBytes("utf-8"), qualifier.getBytes(), value.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return put;
	}

	/**
	 * @description 插入一行数据
	 *
	 * @param tableName
	 *            --表名
	 * @param rowkey
	 *            --行键
	 * @param family
	 *            --列簇
	 * @param columns
	 *            --列集合
	 * @param datas
	 *            --列数据
	 * @return boolean
	 * @author zhengxinbo
	 */
	public boolean insertRowData(String tableName, String rowkey, String family, String[] columns, String[] datas) {
		HTable table = null;
		List<Put> putLists = new ArrayList<Put>();
		try {
			table = (HTable) getTable(tableName);
			if (table == null) {
				return false;
			} else {
				table.setAutoFlush(false, true);
				table.setWriteBufferSize(1024 * 1024);
				for (int i = 0; i < columns.length; i++) {
					if (datas[i] != null) {
						Put put = getPut(rowkey, family, columns[i], datas[i]);
						putLists.add(put);
					}
				}
				if (putLists.size() > 0) {
					table.put(putLists);
					table.flushCommits();
					log.info("Insert table:" + tableName + ",rowkye:" + rowkey + " Data successfully!");
				} else {
					log.info("Insert table:" + tableName + ",rowkye:" + rowkey + " Data is empty! ");
				}
				return true;
			}

		} catch (IOException e) {
			log.info("Insert table:" + tableName + ",rowkye:" + rowkey + " error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 插入多行数据
	 *
	 * @param tableName
	 *            --表名
	 * @param rowkeys
	 *            --行键集合
	 * @param family
	 *            --列簇
	 * @param columns
	 *            --列集合
	 * @param datasLists
	 *            --列数据集合
	 * @return boolean
	 * @author zhengxinbo
	 */
	public boolean insertMultipleRowData(String tableName, List<String> rowkeys, String family, String[] columns, List<String[]> datasLists) {
		HTable table = null;
		List<Put> putLists = new ArrayList<Put>();
		try {
			table = (HTable) getTable(tableName);
			if (table == null) {
				return false;
			} else {
				table.setAutoFlush(false, true);
				table.setWriteBufferSize(5 * 1024 * 1024);
				for (int j = 0; j < rowkeys.size(); j++) {
					String[] datas = datasLists.get(j);
					for (int i = 0; i < columns.length; i++) {
						if (StringUtils.isNotBlank(datas[i])) {
							Put put = getPut(rowkeys.get(j), family, columns[i], datas[i]);
							putLists.add(put);
						}
					}
					if (putLists.size() > 5000) {
						table.put(putLists);
						putLists.clear();
					}
				}
				if (putLists.size() > 0) {
					table.put(putLists);
					table.flushCommits();
					log.info("Insert table:" + tableName + " Data successfully!");
				} else {
					log.info("Insert table:" + tableName + " Data is empty!");
				}
				return true;
			}

		} catch (IOException e) {
			log.info("Insert table:" + tableName + " error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean insertMultipleRowData(String tableName, String family, List<Map<String, String>> datasLists) {
		HTable table = null;
		List<Put> putLists = new ArrayList<Put>();
		try {
			table = (HTable) getTable(tableName);
			if (table == null) {
				return false;
			} else {
				table.setAutoFlush(false, true);
				table.setWriteBufferSize(5 * 1024 * 1024);
				for (int j = 0; j < datasLists.size(); j++) {
					Map<String, String> datas = datasLists.get(j);
					String key = datas.get("key");
					for (Entry<String, String> entry : datas.entrySet()) {
						if (StringUtils.isNotBlank(entry.getValue()) && !entry.getKey().equals("key")) {
							Put put = getPut(key, family, entry.getKey(), entry.getValue());
							putLists.add(put);
						}
					}

					if (putLists.size() > 5000) {
						table.put(putLists);
						putLists.clear();
					}
				}
				if (putLists.size() > 0) {
					table.put(putLists);
					table.flushCommits();
					log.info("Insert table:" + tableName + " Data successfully!");
				} else {
					log.info("Insert table:" + tableName + " Data is empty!");
				}
				return true;
			}

		} catch (IOException e) {
			log.info("Insert table:" + tableName + " error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean insertMultipleRowData(String tableName, List<String> rowkeys, String family, List<String> columns, List<List<String>> datasLists) {
		HTable table = null;
		List<Put> putLists = new ArrayList<Put>();
		try {
			table = (HTable) getTable(tableName);
			if (table == null) {
				return false;
			} else {
				table.setAutoFlush(false, true);
				table.setWriteBufferSize(5 * 1024 * 1024);
				for (int j = 0; j < rowkeys.size(); j++) {
					List<String> datas = datasLists.get(j);
					for (int i = 0; i < columns.size(); i++) {
						if (StringUtils.isNotBlank(datas.get(i))) {
							Put put = getPut(rowkeys.get(j), family, columns.get(i), datas.get(i));
							putLists.add(put);
						}
					}
					if (putLists.size() > 5000) {
						table.put(putLists);
						putLists.clear();
					}
				}
				if (putLists.size() > 0) {
					table.put(putLists);
					table.flushCommits();
					log.info("Insert table:" + tableName + " Data successfully!");
				} else {
					log.info("Insert table:" + tableName + " Data is empty!");
				}
				return true;
			}

		} catch (IOException e) {
			log.info("Insert table:" + tableName + " error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 将结果集构造成Map
	 *
	 * @param result
	 *            --结果集
	 * @return Map<String,String>
	 * @author zhengxinbo
	 */
	private Map<String, String> getCells(Result result) {
		Cell[] cells = result.rawCells();
		Map<String, String> values = new HashMap<String, String>();
		// StringBuffer strs = new StringBuffer();
		// strs.append(tableName + "表一行数据如下：\n");
		for (Cell cell : cells) {
			// strs.append("行:" + new String(CellUtil.cloneRow(cell)) + "\t");
			// strs.append("列:" + new String(CellUtil.cloneQualifier(cell)) +
			// "\t");
			// strs.append("值:" + new String(CellUtil.cloneValue(cell)) + "\n");
			values.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
		}
		// System.out.println(strs.toString());
		return values;
	}

	/**
	 * @description 获取一行数据数据
	 *
	 * @param tableName
	 *            --表名
	 * @param rowkey
	 *            --行键
	 * @return Map<String,String>
	 * @author zhengxinbo
	 */
	public Map<String, String> getRowData(String tableName, String rowkey) {
		HTable table = null;
		try {
			table = (HTable) getTable(tableName);
			if (table == null) {
				return null;
			} else {
				Get get = new Get(rowkey.getBytes());
				Result result = table.get(get);
				Map<String, String> datas = getCells(result);
				return datas;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 获取一行某一字段的最新值
	 *
	 * @param table
	 * @param rowkey
	 * @param family
	 * @param column
	 * @return Map<String,String>
	 * @author zhengxinbo
	 */
	public Map<String, String> getColumnData(Table table, String rowkey, String family, String column) {

		try {
			if (table == null) {
				return null;
			} else {
				Get get = new Get(rowkey.getBytes());
				get.addColumn(family.getBytes(), column.getBytes());
				Result result = table.get(get);
				Map<String, String> datas = getCells(result);
				return datas;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @description 扫描起始行到结尾行的数据
	 *
	 * @param tableName
	 * @param startRow
	 *            --起始行
	 * @param stopRow
	 *            --结尾行
	 * @param batch
	 *            --一次返回列的数量，避免OutofMemory错误
	 * @param cacheBlocks
	 *            --是否缓存块，在热点查询是设为true,全表扫描时要设为false
	 * @return List<Map<String,String>>
	 * @author zhengxinbo
	 */
	public List<Map<String, String>> scanData(String tableName, String startRow, String stopRow, int batch, boolean cacheBlocks) {
		HTable table = null;
		ResultScanner scanner = null;
		List<Map<String, String>> dataLists = new ArrayList<Map<String, String>>();
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {

				Scan scan = new Scan();
				scan.setBatch(batch);
				scan.setCacheBlocks(cacheBlocks);
				scan.setStartRow(startRow.getBytes());
				scan.setStopRow(stopRow.getBytes());
				scanner = table.getScanner(scan);
				for (Result result : scanner) {
					Map<String, String> datas = getCells(result);
					dataLists.add(datas);
				}
			}

			return dataLists;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}
				if (scanner != null) {
					scanner.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 扫描起始行到结尾行的某些字段数据
	 *
	 * @param tableName
	 * @param startRow
	 *            --起始行
	 * @param stopRow
	 *            --结尾行
	 * @param family
	 *            --列族
	 * @param columns
	 *            --列的集合
	 * @return List<Map<String,String>>
	 * @author zhengxinbo
	 */
	public List<Map<String, String>> scanData(String tableName, String startRow, String stopRow, String family, String[] columns) {
		HTable table = null;
		ResultScanner scanner = null;
		List<Map<String, String>> dataLists = new ArrayList<Map<String, String>>();
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {
				Scan scan = new Scan();
				scan.setBatch(100);
				scan.setCacheBlocks(true);
				scan.setStartRow(startRow.getBytes());
				scan.setStopRow(stopRow.getBytes());
				for (String column : columns) {
					scan.addColumn(family.getBytes(), column.getBytes());
				}
				scanner = table.getScanner(scan);
				for (Result result : scanner) {
					Map<String, String> datas = getCells(result);
					dataLists.add(datas);
				}
			}
			return dataLists;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}
				if (scanner != null) {
					scanner.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 全表扫描
	 *
	 * @param tableName
	 * @return List<Map<String,String>>
	 * @author zhengxinbo
	 */
	public List<Map<String, String>> scanData(String tableName) {
		HTable table = null;
		ResultScanner scanner = null;
		List<Map<String, String>> dataLists = new ArrayList<Map<String, String>>();
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {
				Scan scan = new Scan();
				scan.setBatch(100);
				scanner = table.getScanner(scan);
				for (Result result : scanner) {
					Map<String, String> datas = getCells(result);
					dataLists.add(datas);
				}
			}
			return dataLists;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}
				if (scanner != null) {
					scanner.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 全表扫描列字段
	 *
	 * @param tableName
	 * @return List<Map<String,String>>
	 * @author zhengxinbo
	 */
	public List<Map<String, String>> scanColumnData(String tableName, String family, String[] columns) {
		HTable table = null;
		ResultScanner scanner = null;
		List<Map<String, String>> dataLists = new ArrayList<Map<String, String>>();
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {
				Scan scan = new Scan();
				for (String qualifier : columns) {
					scan.addColumn(family.getBytes(), qualifier.getBytes());
				}
				scan.setBatch(100);
				scanner = table.getScanner(scan);
				for (Result result : scanner) {
					Map<String, String> datas = getCells(result);
					dataLists.add(datas);
				}
			}
			return dataLists;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}
				if (scanner != null) {
					scanner.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 使用过滤器扫描表数据
	 *
	 * @param tableName
	 * @param filters
	 *            --过滤器集合
	 * @return List<Map<String,String>>
	 * @author zhengxinbo
	 */
	public List<Map<String, String>> scanDataFilter(String tableName, List<Filter> filters) {
		HTable table = null;
		ResultScanner scanner = null;
		List<Map<String, String>> dataLists = new ArrayList<Map<String, String>>();
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {
				Scan scan = new Scan();
				// 综合使用多个过滤器， AND 和 OR 两种关系
				FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
				scan.setFilter(fl);
				scanner = table.getScanner(scan);
				for (Result result : scanner) {
					Map<String, String> datas = getCells(result);
					dataLists.add(datas);
				}
			}
			return dataLists;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}
				if (scanner != null) {
					scanner.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 前缀过滤器
	 *
	 * @param tableName
	 * @param nsrsbh
	 * @return
	 */
	public List<Result> scanDataPreFilter(String tableName, String nsrsbh) {
		HTable table = null;
		ResultScanner scanner = null;
		List<Result> dataLists = new ArrayList<Result>();
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {
				Scan scan = new Scan();
				scan.setCaching(1000);
				Filter filter = new PrefixFilter(Bytes.toBytes(nsrsbh));
				scan.setFilter(filter);
				scanner = table.getScanner(scan);
				for (Result result : scanner) {
					dataLists.add(result);
				}
			}
			return dataLists;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (table != null) {
					table.close();
				}
				if (scanner != null) {
					scanner.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 删除表某一行某一个字段
	 *
	 * @param tableName
	 * @param rowkey
	 * @param family
	 * @param column
	 * @return boolean
	 * @author zhengxinbo
	 */
	public boolean deleteColumn(String tableName, String rowkey, String family, String column) {
		HTable table = null;
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {
				Delete delete = new Delete(rowkey.getBytes());
				delete.addColumn(family.getBytes(), column.getBytes());
				table.delete(delete);
				log.info("delete Row:" + rowkey + ",column:" + column + "sucess!");
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			log.info("delete Row:" + rowkey + ",column:" + column + "error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 删除表某行
	 *
	 * @param tableName
	 * @param rowkey
	 * @return boolean
	 * @author zhengxinbo
	 */
	public boolean deleteRow(String tableName, String rowkey) {
		HTable table = null;
		try {
			table = (HTable) getTable(tableName);
			if (table != null) {
				Delete delete = new Delete(rowkey.getBytes());
				table.delete(delete);
				log.info("delete Row:" + rowkey + "sucess!");
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			log.info("delete Row:" + rowkey + "error!");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (table != null) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param tableName:表名
	 * @param delList
	 *            批量删除
	 * @return
	 */
	public boolean deleteBatchRow(String tableName, List<Delete> delList) {
		HTable table = null;
		try {
			table = (HTable) getTable(tableName);
			if (null != table) {
				table.delete(delList);
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (null != table) {
					table.close();
					table = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	public JavaPairRDD<ImmutableBytesWritable, KeyValue> sortByKey(JavaPairRDD<String, List<Tuple2<ImmutableBytesWritable, KeyValue>>> mapToPair) {
//		JavaPairRDD<ImmutableBytesWritable, KeyValue> hfileRdd = mapToPair.sortByKey().flatMapToPair(new PairFlatMapFunction<Tuple2<String,List<Tuple2<ImmutableBytesWritable,KeyValue>>>, ImmutableBytesWritable,KeyValue>() {
//			@Override
//			public Iterable<Tuple2<ImmutableBytesWritable, KeyValue>> call(
//					Tuple2<String, List<Tuple2<ImmutableBytesWritable, KeyValue>>> t) throws Exception {
//				String _1 = t._1;
//				List<Tuple2<ImmutableBytesWritable, KeyValue>> list = t._2;
//				//排序
//				Collections.sort(list, new Comparator<Tuple2<ImmutableBytesWritable, KeyValue>>(){
//		            public int compare(Tuple2<ImmutableBytesWritable, KeyValue> p1, Tuple2<ImmutableBytesWritable, KeyValue> p2) {
//		                //按照Tuple2的字段进行升序排列
//		            	String key1 = Bytes.toString(p1._2.getQualifier());
//		            	String key2 = Bytes.toString(p2._2.getQualifier());
//
//		                if(key1.compareTo(key2)>0){    //对象排序用camparTo方法
//		                	return 1;
//		                }
//		                return -1;
//		            }
//		       });
//				return list;
//			}
//		});
//
//		return hfileRdd;
//	}

}
