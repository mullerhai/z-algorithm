package com.zen.spark.preprocess

import com.zen.spark.etl.AdDataEtlForTrain
import com.zen.spark.model.OfficialLR3ForTrain
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

/**
 * @Author: morris
 * @Date: 2020/8/13 16:29
 * @reviewer
 */
object TrainMain {
  val logger = LoggerFactory.getLogger("TrainMain")
  def main(args: Array[String]): Unit = {
    //(outputPath,metricPath,modelPath,mleapPath)
    val modelPaths = Array(args(0),args(1),args(2),args(3))
    //20200812,20200813,20200814
    val dateStrs = args(4)
    val dataPath = args(5)
    val version = args(6)
    val maxIters = args(7)


    val spark = SparkSession.builder().appName("training-main")
      .config("spark.sql.parquet.compression.codec", "uncompressed").getOrCreate()
    logger.info("train:data processing")
    val strings = dateStrs.split(',')
    for (dateStr <- strings){
      AdDataEtlForTrain.getDfs(dateStr,version, spark)
    }
    logger.info("train:data in {} processed",dateStrs)
    val df = GBDTLRDataProcess.process(dataPath, spark)
    OfficialLR3ForTrain.train(df,spark,maxIters.toInt,modelPaths)
    spark.close()
  }

}
