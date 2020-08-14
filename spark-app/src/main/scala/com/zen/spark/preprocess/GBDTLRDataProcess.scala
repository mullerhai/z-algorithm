package com.zen.spark.preprocess

import com.alibaba.fastjson.JSON
import com.zen.spark.bean.{AdProfile, HistImpl, UserBehavior, UserProfile}
import com.zen.spark.preprocess.TrainMain.logger
import com.zen.spark.util.HdfsUtils
import org.apache.spark.SparkException
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

import scala.collection.mutable.ArrayBuffer

/**
 * @Author: xiongjun
 * @Date: 2020/8/6 17:21
 * @description
 * @reviewer
 */
object GBDTLRDataProcess {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("GBDTLRDataProcess") /*.master("local[*]")*/
      .config("spark.sql.parquet.compression.codec", "uncompressed").getOrCreate()
    val inputPath = args(0)
    val outputPath = args(1)
    val df = process(inputPath, spark)
    df.repartition(1).write.mode(SaveMode.Overwrite).parquet(outputPath)
  }

  def process(inputPath: String, spark: SparkSession): DataFrame = {
    logger.info("train:data merge and processing")
    import spark.implicits._
    val dataDateArr = HdfsUtils.listdir(inputPath).filter(date => date.substring(5, 7).equals("73") || date.charAt(5) == '8')
    val userProfileDFArr = new ArrayBuffer[Dataset[UserProfile]]()
    val userBehaviorDFArr = new ArrayBuffer[Dataset[UserBehavior]]()
    val histImplDFArr = new ArrayBuffer[Dataset[HistImpl]]()
    val adProfileDFArr = new ArrayBuffer[Dataset[AdProfile]]()
    dataDateArr.map(dataDate => inputPath + "/" + dataDate).foreach(path => {
      val tableArr = HdfsUtils.listdir(path)
      tableArr.foreach(table => {
        val tableDf = spark.read.parquet(path + "/" + table)
        val addDataDate = (date: String) => udf { value: Any => {
          date
        }
        }
        val date = path.substring(path.lastIndexOf("/") + 1)
        val dateFormat = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6)
        tableDf.withColumn("data_date", addDataDate(dateFormat)(tableDf(tableDf.schema.head.name)))

        val jsonDf = tableDf.toJSON
        table match {
          case "userBaseInfo" =>
            userProfileDFArr += jsonDf.map(jsonStr => JSON.parseObject(jsonStr, UserProfile.getClass))
          case "userActionBaseInfo" =>
            userBehaviorDFArr += jsonDf.map(jsonStr => JSON.parseObject(jsonStr, UserBehavior.getClass))
          case "adverInfo" =>
            adProfileDFArr += jsonDf.map(jsonStr => JSON.parseObject(jsonStr, AdProfile.getClass))
          case "historyAdverExposeInfo" =>
            histImplDFArr += jsonDf.map(jsonStr => JSON.parseObject(jsonStr, HistImpl.getClass))
          case _ =>
            throw new SparkException("not support class")
        }

      })
    })
    val firstUserProfile = userProfileDFArr.remove(0)
    val userProfileDataset = userProfileDFArr.foldLeft[Dataset[UserProfile]](firstUserProfile)((a, b) => a.union(b))
    val firstUserBehavior = userBehaviorDFArr.remove(0)
    val userBehaviorDataset = userBehaviorDFArr.foldLeft[Dataset[UserBehavior]](firstUserBehavior)((a, b) => a.union(b))
    val firstAdProfile = adProfileDFArr.remove(0)
    val adProfileDataset = adProfileDFArr.foldLeft[Dataset[AdProfile]](firstAdProfile)((a, b) => a.union(b))
    val firstHistImpl = histImplDFArr.remove(0)
    val histImplDataset = histImplDFArr.foldLeft[Dataset[HistImpl]](firstHistImpl)((a, b) => a.union(b))

    val processedUserProfileDataset = userProfileDataset.filter(_.userId != "707053030").filter(_.deviceId != "").dropDuplicates("deviceId")
      .map(up => {
        if (up.registerDate == "" || up.registerDate.isEmpty) {
          UserProfile(up.userId, up.deviceId, up.device, up.province, up.city, up.dataDate, up.os, up.dataDate)
        } else up
      })
    val processedAdProfileDataset = adProfileDataset.dropDuplicates("adId", "materialId")
    val userInfo = processedUserProfileDataset.join(userBehaviorDataset, Seq("userId", "dataDate"), "left")
    val histImplInfo = histImplDataset.join(processedAdProfileDataset, Seq("adId", "materialId"), "left")
    val dataset = histImplInfo.join(userInfo, Seq("deviceId", "dataDate"), "left").na.fill(0, Array("city", "province", "skipAdTimes"))
      .na.fill(0, Array("device", "last15consumption", "lastVisitDate", "numOfGame"))
    logger.info("train:data in  merge and processed")
    dataset
  }


}
