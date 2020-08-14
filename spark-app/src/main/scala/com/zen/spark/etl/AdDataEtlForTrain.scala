package com.zen.spark.etl

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Properties

import com.alibaba.fastjson.JSONObject
import com.zen.spark.bean.{AdverBean, HistoryBean, UserActionBean, UserBaseBean}
import com.zen.spark.etl.common.HBaseClient
import com.zen.spark.etl.conf.bill.{BI101001, BIUserBill, BIUserGameInfoBill}
import com.zen.spark.etl.conf.{BillUtils, PropertiesUtil}
import com.zen.spark.etl.constant.Constants
import com.zen.spark.etl.util.{AlgoDataUtil, GetAdverMoneyUtil}
import com.zen.spark.preprocess.GBDTLRDataProcess
import com.zen.spark.preprocess.TrainMain.logger
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.NumberUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hbase.client.{Result, Scan, Table}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.{Base64, Bytes}
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions
import scala.collection.mutable.ArrayBuffer

/**
 * @Author: xiongjun
 * @Date: 2020/8/10 15:07
 * @description
 * @reviewer
 */
object AdDataEtlForTrain {
  val logger = LoggerFactory.getLogger("AdDataEtl")
  def getDfs(statDayStr:String,version:String,spark:SparkSession) = {
   /* val statDayStr = args(0)
    val spark = SparkSession.builder().appName("AdDataEtl")
      .config("spark.sql.parquet.compression.codec", "uncompressed").getOrCreate()*/
    val statDate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(statDayStr))
    val sparkContext = spark.sparkContext
    val b101001 = sparkContext.textFile(BillUtils.getBillPath("101001", statDayStr))
    val adverBill = sparkContext.textFile(BillUtils.getBillPath("123003", statDayStr))
    val userBill = sparkContext.textFile(BillUtils.getBillPath("UserBill", statDayStr))
    val userGameInfo = sparkContext.textFile(BillUtils.getBillPath("UserGameInfo", statDayStr))
    // Hbase连接
    val serverProps = PropertiesUtil.getProperties
    val charge = serverProps.getProperty(Constants.HBASE_TABLE_CHARGE)
    val chargeTable = HBaseClient.getInstance(serverProps).getTable(charge)
    val active = serverProps.getProperty(Constants.HBASE_TABLE_ACTIVECOUNT)
    val activeTable = HBaseClient.getInstance(serverProps).getTable(active)
    // 广告位属性
    val adverInfo = getAdverInfo(adverBill)
    val userInfo = getUserInfo(b101001)
    val gameMap = AlgoDataUtil.getGameID("gameId", "oss.algo_gameIdInfo")
    //获取注册日期,消费总额,玩过的游戏
    val regisDateUserInfo = getRegisDateUserInfo(userInfo, gameMap)
//    val rows = regisDateUserInfo.map(uidRegisterInfo=>{
//      Row(uidRegisterInfo._1,uidRegisterInfo._2(0),uidRegisterInfo._2(1),uidRegisterInfo._2(2))
//    })
//    val schema = StructType(Seq(
//      StructField("user_id",StringType),
//      StructField("register_date",StringType),
//      StructField("total_consumption",StringType),
//      StructField("game_id",StringType)
//    ))
//    val df = spark.createDataFrame(rows,schema)
//    df.repartition(1).write.mode(SaveMode.Overwrite).save("/data/lves/new_ad/20200806/registerInfo")
    //最近15天消费
    val userRecentMoneyInfo = getChargeInfo(spark, chargeTable).reduceByKey((v1,v2)=>{
      val m1 = v1.toDouble
      val m2 = v2.toDouble
      (m1+m2).toString
    })
    val userActionInfo = getUserActionInfo(b101001, adverBill, userBill, userGameInfo)

    val userActiveInfo = getActiveInfo(spark, active, serverProps)

    val  adMysqlInfo = JavaConversions.asScalaBuffer(AlgoDataUtil.getAdMysqlInfo)
    val adMysqlRdd = spark.sparkContext.parallelize(adMysqlInfo)

    val userBaseCombineInfo = userInfo.leftOuterJoin(regisDateUserInfo).map(line=>{
      val value1 = line._2._1
      val value2 = line._2._2.getOrElse(Array("", "0.0", ""))
      val value = Array(value1(0), value1(1), value1(4), value1(3), value2(0), value1(6))
      (line._1,value)
    })

    val userActionCombineInfo = userActionInfo.leftOuterJoin(regisDateUserInfo).map(line=>{
      val val1 = line._2._1
      val val2 = line._2._2.getOrElse(Array[String](statDate, "0.0", ""))
      // skipNum, clickAd, playNum, intervalDays, registDays
      // registDate, totalMoney, gameId
      val value = Array(val2(2), val2(1), val1(2), val1(4), val1(3), val1(0), val1(1))
      (line._1, value)
    }).leftOuterJoin(userRecentMoneyInfo).map(line=>{
      val val1 = line._2._1
      val val2 = line._2._2.getOrElse("0.0")

      val value = val1++val2

      (line._1,value.map(_.toString))
    }).leftOuterJoin(userActiveInfo).map(line=>{
      val val1 = line._2._1

      val val2 = line._2._2.getOrElse("0")

      val value = val1++(val2)
      (line._1, value.map(_.toString))
    })

    val toRedisInfo1 = adverInfo.map(line=>{
      val key = line._1.split("\\|")
      val val1 = line._2
      val value = Array(key(0), val1(0), val1(3), val1(5))
      (val1(6), value)
    })
    val toRedisInfo = userBaseCombineInfo.fullOuterJoin(userActionCombineInfo).map(line=>{
      val val1 = line._2._1.getOrElse(new Array[String](6))
      val val2 = line._2._2.getOrElse(new Array[String](9))
      val key = line._1.split("\\|")
      val registDays = val2(3).toInt
      val visitDays = val2(8).toInt * 7
      val f1 = if (visitDays == 0) 0.00
      else new BigDecimal(registDays.toFloat / visitDays).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue
      val value = Array(key(0), val1(0), val1(1), val1(2), val1(3), val1(4), val1(5), val2(0), val2(1), val2(7), val2(2), String.valueOf(f1), val2(4), val2(5), val2(6))
      (val1(5), value)
    }).fullOuterJoin(toRedisInfo1).map(line=>{
      val val1 = line._2._1.getOrElse(new Array[String](15))
      val val2 = line._2._2.getOrElse(new Array[String](4))
      val value = val1++val2
      (line._1,value)
    })
    val redisData = toRedisInfo.filter(_._1.nonEmpty).map(line=>{
      val key1 = line._1
      val redisKey = "ad" + key1
      val value = line._2
      val jsonUserInfo = new JSONObject
      jsonUserInfo.put("user_id", value(0))
      jsonUserInfo.put("province", NumberUtils.toInt(value(1)))
      jsonUserInfo.put("city", NumberUtils.toInt(value(2)))
      jsonUserInfo.put("device", NumberUtils.toInt(value(3)))
      jsonUserInfo.put("os", NumberUtils.toInt(value(4)))
      jsonUserInfo.put("register_date", value(5))
      jsonUserInfo.put("device_id", value(6))

      val gameId = (if (StringUtils.isNotEmpty(value(7))) value(7)
      else "").split(",")
      val adList = (if (StringUtils.isNotEmpty(value(14))) value(14)
      else "").split(",")

      jsonUserInfo.put("hist_game_list", java.util.Arrays.asList(gameId))
      jsonUserInfo.put("total_consumption", NumberUtils.toDouble(value(8)))
      jsonUserInfo.put("last_15_consumption", NumberUtils.toDouble(value(9)))
      jsonUserInfo.put("num_of_game", NumberUtils.toInt(value(10)))
      jsonUserInfo.put("visit_times_average", NumberUtils.toDouble(value(11)))
      jsonUserInfo.put("last_visit_date", NumberUtils.toInt(value(12)))
      jsonUserInfo.put("skip_ad_times", NumberUtils.toInt(value(13)))
      jsonUserInfo.put("clicked_ad_list", java.util.Arrays.asList(adList))

      jsonUserInfo.put("request_id", value(15))
      jsonUserInfo.put("timestamp", NumberUtils.toLong(value(16)))
      jsonUserInfo.put("material_id", value(17))
      jsonUserInfo.put("connect_type", NumberUtils.toInt(value(18)))
      Array(redisKey,jsonUserInfo.toString).mkString("|")
    })

    val historyAdverExposeInfo = adverInfo.map(line => {
      def foo(line: Tuple2[String, Array[String]]) = { // ts, advertisingSceneId, originalityId, materialId, advertisingType, netType, imei, isClick
        val key = line._1.split("\\|")
        val value = line._2
        val ab = new HistoryBean
        ab.setRequest_id(key(0))
        ab.setTimestamp(NumberUtils.toLong(value(0)))
        ab.setScene_id(value(1))
        ab.setAd_id(value(2))
        ab.setMaterial_id(value(3))
        ab.setAd_type(value(4))
        ab.setConnect_type(NumberUtils.toInt(value(5)))
        ab.setDevice_id(value(6))
        ab.setCur_app_id(value(7))
        ab.setChannel(value(8))
        ab.setLabel(NumberUtils.toInt(value(9)))
        ab
      }

      foo(line)
    })

    // 广告信息表
    val adverSqlInfo = adMysqlRdd.map(line => {
      def foo(line: Array[String]) = {
        val value = line
        val ad = new AdverBean
        ad.setAd_id(value(0))
        ad.setAdvertiser_id(value(1))
        ad.setAppname(value(2))
        ad.setMaterial_id(value(3))
        ad
      }
      foo(line)
    })
    // 用户基本信息表
    val userBaseInfo = userBaseCombineInfo.map(line => {
      def foo(line: Tuple2[String, Array[String]]) = {
        val key = line._1.split("\\|")
        val value = line._2
        val ub = new UserBaseBean
        ub.setUser_id(key(0))
        ub.setProvince(NumberUtils.toInt(value(0)))
        ub.setCity(NumberUtils.toInt(value(1)))
        ub.setDevice(NumberUtils.toInt(value(2)))
        ub.setOs(NumberUtils.toInt(value(3)))
        ub.setRegister_date(value(4))
        ub.setDevice_id(value(5))
        ub
      }
      foo(line)
    })

    // 用户平台行为表
    val userActionBaseInfo = userActionCombineInfo.map(line => {
      def foo(line: Tuple2[String, Array[String]]) = {
        val key = line._1.split("\\|")
        val value = line._2
        val gameId = value(0).split(",")
        val registDays = NumberUtils.toInt(value(3))
        val visitDays = NumberUtils.toInt(value(8)) * 7
        val f1 = if (visitDays == 0) 0.00
        else new BigDecimal(registDays.toFloat / visitDays).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue
        val adList = value(6).split(",")
        val ua = new UserActionBean
        ua.setUser_id(key(0))
        ua.setHist_game_list(JavaConversions.seqAsJavaList(gameId))
        ua.setTotal_consumption(value(1).toDouble)
        ua.setLast_15_consumption(value(7).toDouble)
        ua.setNum_of_game(value(2).toInt)
        ua.setVisit_times_average(f1)
        ua.setLast_visit_date(value(4).toInt)
        ua.setSkip_ad_times(value(5).toInt)
        ua.setClicked_ad_list(JavaConversions.seqAsJavaList(adList))
        ua
      }

      foo(line)
    })

    val historyAdverExposeInfo_table = spark.createDataFrame(historyAdverExposeInfo, classOf[HistoryBean])
    val  adverSqlInfo_table = spark.createDataFrame(adverSqlInfo, classOf[AdverBean])
    val  userBaseInfo_table = spark.createDataFrame(userBaseInfo, classOf[UserBaseBean])
    val  userActionBaseInfo_table = spark.createDataFrame(userActionBaseInfo, classOf[UserActionBean])

    // 存入hdfs
    val hdfs = FileSystem.get(new Configuration)

    val historyAdverPath = "/data/lves/train_data/"+ version+"/" + statDayStr + "/historyAdverExposeInfo"
    val adverPath = "/data/lves/train_data/"+version+"/" + statDayStr + "/adverInfo"
    val userBasePath = "/data/lves/train_data/"+version+"/" + statDayStr + "/userBaseInfo"
    val userActionBasePath = "/data/lves/train_data/"+version+"/" + statDayStr + "/userActionBaseInfo"
    hdfs.delete(new Path(historyAdverPath), true)
    hdfs.delete(new Path(adverPath), true)
    hdfs.delete(new Path(userBasePath), true)
    hdfs.delete(new Path(userActionBasePath), true)

    // 不进行压缩，存储为parguet
    historyAdverExposeInfo_table.write.mode(SaveMode.Overwrite).parquet(historyAdverPath)

    adverSqlInfo_table.write.mode(SaveMode.Overwrite).parquet(adverPath)

    userBaseInfo_table.write.mode(SaveMode.Overwrite).parquet(userBasePath)

    userActionBaseInfo_table.write.mode(SaveMode.Overwrite).parquet(userActionBasePath)
    // merge Operate
//    Array(userBaseInfo_table,userActionBaseInfo_table,historyAdverExposeInfo_table,adverSqlInfo_table)
    logger.info("train:data:{} processed",statDayStr)



  }


  def getActiveInfo(spark:SparkSession,chargeTable: String,serverProps: Properties):RDD[(String,String)]={
    val hbase = HBaseConfiguration.create

    hbase.set("hbase.zookeeper.property.clientPort", serverProps.getProperty(Constants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbase.set("hbase.zookeeper.quorum", serverProps.getProperty(Constants.HBASE_ZOOKEEPER_QUORUM))
    val baseScan = new Scan
    val userPay = readFromHbase(spark, hbase, chargeTable, baseScan)
    val baseRs = userPay.mapPartitions(results=>{
      val userVideoList = new ArrayBuffer[(String,String)]()
      results.foreach(result=>{
        JavaConversions.collectionAsScalaIterable(result.listCells()).foreach(cell=>{
          val cloneRow = CellUtil.cloneRow(cell)
          val cloneQualifier = CellUtil.cloneQualifier(cell)
          val cloneValue = CellUtil.cloneValue(cell)
          val oldKey = Bytes.toString(cloneRow).split("_")
          if (Bytes.toString(cloneQualifier).contains("historyInfo:activeSum")) {
            val value = Bytes.toString(cloneValue)
            userVideoList += ((oldKey(2),value))
          }
        })
      })
      userVideoList.toIterator
    })
    baseRs
  }

  def readFromHbase(spark:SparkSession,hbaseConf: Configuration,tablename:String,scan:Scan):RDD[Result]={
    val proto = ProtobufUtil.toScan(scan)
    val ScanToString = Base64.encodeBytes(proto.toByteArray)
    hbaseConf.set(TableInputFormat.SCAN, ScanToString)
    hbaseConf.set(TableInputFormat.INPUT_TABLE, tablename)

    val hbaseRDD = spark.sparkContext.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    hbaseRDD.map(_._2)
  }
  def getUserActionInfo(b101001: RDD[String], adverBill: RDD[String], userBill: RDD[String], userGameInfo: RDD[String])
  :RDD[(String,Array[String])] = {
    val imeiToUserIdInfo = b101001.filter(line=>{
      val data = line.split("\\|")
      val userId = data(BI101001.userId)
      userId.nonEmpty
    }).map(line=>{
      val data = line.split("\\|")
      val userId = data(BI101001.userId)
      val imei = data(BI101001.imei)
      (imei, userId)
    }).reduceByKey((v1: String, v2: String) => v1)
    val gamePlayNum = userGameInfo.map(line=>{
      val data = line.split("\\|")
      val userId = data(BIUserGameInfoBill.userId)
      val playNum = data(BIUserGameInfoBill.playNum).toInt
      (userId, playNum)
    }).reduceByKey(_+_)
    val preDayInfo = userBill.map(line=>{
      val data = line.split("\\|")
      val userId = data(BIUserBill.userId)
      val intervalDays = data(BIUserBill.intervalDays)
      val registDays = data(BIUserBill.registDay)
      val value = Array(intervalDays, registDays)
      (userId, value)
    }).reduceByKey((v1: Array[String], v2: Array[String]) => v1)

    val skipInfo = adverBill.map(line=>{
      val data = line.split("\\|")
      val imei = data(10)
      val eventType = data(20)
      var skipAdCount = if ("click" == eventType) 1
      else 0
      val closeAdCount = if ("video_skip" == eventType) 1
      else 0
      skipAdCount += closeAdCount
      (imei,skipAdCount)
    }).reduceByKey(_+_)

    val clickInfo = adverBill.filter(line=>{
      val data = line.split("\\|")
      val eventType = data(20)
      "click" == eventType
    }).map(line=>{
      val data = line.split("\\|")

      val imei = data(10)

      var originalityId = "0"
      if (data.length > 26) originalityId = if (data(26).isEmpty) "0"
      else data(26)
      (imei, originalityId)
    }).reduceByKey((v1,v2)=>Array(v1,v2).mkString(","))
    val adInfo = skipInfo.fullOuterJoin(clickInfo).map(line=>{
      val skipNum = line._2._1.getOrElse(0).toString
      val clickAd = line._2._2.getOrElse("0")

      val value = Array(skipNum, clickAd, line._1)
      (line._1,value)
    })
    val adActionInfo = adInfo.leftOuterJoin(imeiToUserIdInfo).filter(line=>line._2._2.nonEmpty).map(line=>{
      val userId = line._2._2.get
      (userId,line._2._1)
    }).leftOuterJoin(gamePlayNum).map(line=>{
      val playNum = line._2._2.getOrElse(0).toString
      val val1 = line._2._1
      val skipNum = val1(0)
      val clickAd = val1(1)
      val imei = val1(2)
      val val2 = Array(skipNum, clickAd, playNum, imei)
      (line._1, val2)
    }).leftOuterJoin(preDayInfo).map(line=>{
      val value = line._2._2.getOrElse(Array[String]("0", "0"))
      val intervalDays = value(0)
      val registDays = value(1)
      val val1 = line._2._1
      val skipNum = val1(0)
      val clickAd = val1(1)
      val playNum = val1(2)
      val imei = val1(3)
      val userId = line._1
      val val2 = Array(skipNum, clickAd, playNum, intervalDays, registDays, userId)
      (imei, val2)
    }).reduceByKey((v1: Array[String], v2: Array[String]) => v1).map(line=>{
      val data = line._2
      val skipNum = data(0)
      val clickAd = data(1)
      val playNum = data(2)
      val intervalDays = data(3)
      val registDays = data(4)
      val userId = data(5)
      val value = Array(skipNum, clickAd, playNum, intervalDays, registDays)
      (userId, value)
    })
    adActionInfo
  }
  def getChargeInfo(spark: SparkSession, chargeTable: Table):RDD[(String,String)] = {
    val userChargeList = new ArrayBuffer[(String,String)]()
    val scan = new Scan()
    val rss = chargeTable.getScanner(scan)
    JavaConversions.iterableAsScalaIterable(rss).foreach(r=>{
      JavaConversions.collectionAsScalaIterable(r.listCells()).foreach(cell=>{
        val cloneRow = CellUtil.cloneRow(cell)
        val cloneQualifier = CellUtil.cloneQualifier(cell)
        val cloneValue = CellUtil.cloneValue(cell)
        val oldKey = Bytes.toString(cloneRow).split("_")
        val key = oldKey(2)
        if (Bytes.toString(cloneQualifier).contains("chargeMoney")) {
          val value = Bytes.toString(cloneValue)
          userChargeList +=((key, value))
        }
      })
    })
    rss.close()
    spark.sparkContext.parallelize(userChargeList)
  }
  def getRegisDateUserInfo(userInfo: RDD[(String, Array[String])], gameMap: java.util.HashMap[String, String]):RDD[(String,Array[String])]={
    // 分两步，第一步，根据游戏Id和用户Id去重
    // 第二步，转化为Tuple List在工具类里查询得出结果
    // 分为多个区执行
    val countNums = 20
    val findUserInfo = userInfo.map(line=>{
      line._1
    }).distinct().repartition(countNums)
    val registDateUserInfoCV = findUserInfo.mapPartitions(lines=>{
      val regisDateUserInfoList = GetAdverMoneyUtil.getAlgoUserRegistPriInfo(JavaConversions.seqAsJavaList(lines.toSeq))
      JavaConversions.collectionAsScalaIterable(regisDateUserInfoList).toIterator
    })
//    val count = registDateUserInfoCV.count()
//    logger.debug(s"registerinfo count:$count")

    registDateUserInfoCV.reduceByKey((v1,v2)=>{
      val registDate = if (AlgoDataUtil.isSmallDate(v1(0), v2(0))) v1(0)
      else v2(0)
      val totalMoney = String.valueOf(NumberUtils.toDouble(v1(1)) + NumberUtils.toDouble(v2(1)))
      val newGameId1 = gameMap.getOrDefault(v1(2), "")
      val newGameId2 = gameMap.getOrDefault(v2(2), "")
      val gameId = Array[String](newGameId1, newGameId2).mkString(",")
      val value = Array(registDate, totalMoney, gameId)
      value
    })
  }
  //用户基本信息数据
  def getUserInfo(b101001: RDD[String]):RDD[(String,Array[String])] = {
    val userInfoCV = b101001.filter(line=>{
      val data = line.split("\\|")
      val userId = data(BI101001.userId)
      userId.nonEmpty
    }).map(line=>{
      val data = line.split("\\|")
      val userId = data(BI101001.userId)
      val province = if (data(BI101001.gpsProvince).nonEmpty){
        data(BI101001.gpsProvince)
      } else data(BI101001.ipProvince)
      val city = if (data(BI101001.gpsCity).nonEmpty) {data(BI101001.gpsCity)}
      else data(BI101001.ipCity)
      val channelId = data(BI101001.channelId)
      val systemType = if (channelId.contains("ios")) "ios"
      else "android"
      val deviceType = data(BI101001.deviceCompany)
      val ts = data(BI101001.ts)
      val imei = data(BI101001.imei)
      val value = Array(province, city, channelId, systemType, deviceType, ts, imei)
      (userId, value)
    })
    val provinceRDDList = userInfoCV.map(line=>{
      line._2(0)
    }).distinct().collect()
    // 库无则入，并取ID
    val provinceMap: java.util.HashMap[String, String] = AlgoDataUtil.getElementID("province", "oss.algo_provinceInfo", JavaConversions.seqAsJavaList(provinceRDDList))
    val cityRDDList = userInfoCV.map(line=>{
      line._2(1)
    }).distinct().collect()
    val cityMap = AlgoDataUtil.getElementID("city", "oss.algo_cityInfo", JavaConversions.seqAsJavaList(cityRDDList))

    val deviceRDDList = userInfoCV.map(line=>{
      var device = line._2(4)
      device = device.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，。？“”]+", "")
      device
    }).distinct().collect()
    val deviceMap = AlgoDataUtil.getElementID("deviceType", "oss.algo_deviceInfo",JavaConversions.seqAsJavaList(deviceRDDList))

    //系统类型
    val systemTypeMap = AlgoDataUtil.getSystemTypeID
    userInfoCV.map(line=>{
      val userId = line._1
      val data = line._2
      val province = data(0)
      val city = data(1)
      val channelId = data(2)
      val systemType = data(3)
      val device = data(4)
      val ts = data(5)
      val imei = data(6)
      val value = Array(province, city, channelId, systemType, device, ts, userId)
      (imei,value)
    }).reduceByKey((v1,v2)=>{
      val ts1 = v1(5).toLong
      val ts2 = v2(5).toLong
      if (ts1 > ts2) v1
      else v2
    }).map(line=>{
      val imei = line._1
      val data = line._2
      val province = provinceMap.getOrDefault(data(0), "0")
      val city = cityMap.getOrDefault(data(1), "0")
      val channelId = data(2)
      val systemType = systemTypeMap.getOrDefault(data(3), "0")
      val device = deviceMap.getOrDefault(data(4), "0")
      val ts = data(5)
      val userId = data(6)
      val value = Array(province, city, channelId, systemType, device, ts, imei)
      (userId,value)
    })
  }
  //历史曝光数据
  def getAdverInfo(adverBill:RDD[String]):RDD[(String,Array[String])] = {
    //网络类型字典
    val netTypeMap = AlgoDataUtil.getNetTypeID
    val clickedInfo = adverBill.filter(line=>{//把点击事件过滤出来
      val data = line.split("\\|")
      val eventType = data(20)
      (data.length > 34) && ("click" == eventType)
    }).map(line=>{
      val data = line.split("\\|")
      val requestId = data(34)
      val imei = data(10)
      val key = Array[String](requestId, imei).mkString("|")
      (key,"1")
    }).reduceByKey((v1,v2)=>v1)//去重

    adverBill.filter(line => {
      def foo(line:String) = {
        val data = line.split("\\|")
        val eventType = data(20)
        val requestId = if (data.length > 34) data(34)
        else ""
        "exposed" == eventType && requestId.nonEmpty
      }
      foo(line)
    }).map(line=>{
      val data = line.split("\\|")
      val requestId = data(34)
      val ts = data(1)

      //广告位ID
      val advertisingSceneId = if (data(23).isEmpty) {
        "0"
      } else data(23)
      //创意ID
      val originalityId = if(data(26).isEmpty){
        "0"
      }else data(26)
      //素材ID
      val materialId = if(data(27).isEmpty){
        "0"
      }else data(27)
      //广告位类型
      val advertisingType = if(data(24).isEmpty){
        "0"
      }else data(24)
      //连接网络类型
      val netType = netTypeMap.getOrDefault(data(15), "0");
      val imei = data(10)
      val curAppId = data(3)
      val channel = data(33)
      val key = Array[String](requestId, imei).mkString("|")
      val value = Array(ts, advertisingSceneId, originalityId, materialId, advertisingType, netType, imei,curAppId,channel)
      (key,value)
    }).reduceByKey((v1,v2)=>v1).leftOuterJoin(clickedInfo).map(line=>{
      val key1 = line._1.split("\\|")
      val requestId = if (key1.nonEmpty) key1(0)
      else "0"
      val data = line._2._1
      val isClick = line._2._2.getOrElse("0")
      val value = Array(data(0), data(1), data(2), data(3), data(4), data(5), data(6),data(7),data(8), isClick)
      (requestId,value)
    })

  }
}
