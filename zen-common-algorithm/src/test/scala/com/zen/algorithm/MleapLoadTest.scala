package com.zen.algorithm


import ml.combust.bundle.BundleFile
import ml.combust.mleap.runtime.MleapSupport._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import resource.managed
/**
 * @Author: morris
 * @Date: 2020/6/19 15:00
 * @description
 * @reviewer
 */
object MleapLoadTest {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("LRModel").master("local[*]").getOrCreate()
    val destPath = "/03-ML/03-mleap/1593314702968_lr1.zip"
    val inputPath = "F:\\03-ML\\05-train-data\\03-2020-6-19\\part-00000-fd53b784-f037-4659-a8f4-e12bb2ffa95f-c000.csv"
    val adProfileSchema = StructType(Seq(
      StructField("game_id",IntegerType),
      StructField("user_id",IntegerType),
      StructField("province",StringType),
      StructField("city",StringType),
      StructField("channel",StringType),
      StructField("os",StringType),
      StructField("register_date",StringType),
      StructField("total_consumption",DoubleType),
      StructField("last_15_consumption",DoubleType),
      StructField("advertiser",IntegerType),
      StructField("ad_pos",StringType),
      StructField("scene_id",StringType),
      StructField("material_id",StringType),
      StructField("appname",StringType),
      StructField("label",IntegerType)
    ))

    val inputDF = spark.read.schema(adProfileSchema).csv(inputPath)
    val transformer = (for (bf <- managed(BundleFile(s"jar:file:${destPath}"))) yield {
      bf.loadMleapBundle().get.root
    }).tried.get
//    val triedFrame = transformer.transform(inputDF)
//    triedFrame.get.show(5)



//    transformer.transform()


  }

}
