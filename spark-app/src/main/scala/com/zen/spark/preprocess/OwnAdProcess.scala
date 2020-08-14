package com.zen.spark.preprocess

import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
 * @Author: xiongjun
 * @Date: 2020/7/6 15:34
 * @description
 * @reviewer
 */
object OwnAdProcess {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().config("spark.sql.parquet.compression.codec","uncompressed").appName("OwnAdProcess").master("local[1]").getOrCreate()
    spark.read.parquet("E:\\sample\\ownad\\part-r-00071-65149e1d-27c2-4b1c-b623-bd24639ecc51.parquet").printSchema()
//    val userProfile = spark.read.textFile("E:\\sample\\ownad\\userBaseTmp")
//    val rdd = userProfile.rdd.map(line=>{
//      val lines = line.split("|")
//      val city = lines(0).toInt
//      val device = lines(1).toInt
//      val device_id = lines(2)
//      val os = lines(3).toInt
//      val province = lines(4).toInt
//      val register_date = lines(5)
//      val user_id = lines(6)
//      Row(city,device,device_id,os,province,register_date,user_id)
//    }
//    )
//    val schema = StructType(Seq(
//      StructField("city",IntegerType),
//      StructField("device",IntegerType),
//      StructField("device_id",StringType),
//      StructField("os",IntegerType),
//      StructField("province",IntegerType),
//      StructField("register_date",StringType),
//      StructField("user_id",StringType)
//    ))
//    val df = spark.createDataFrame(rdd,schema)
//    df.write.option("spark.sql.parquet.compression.codec","uncompressed").parquet("E:\\sample\\ownad\\userBaseInfo")
  }
}
