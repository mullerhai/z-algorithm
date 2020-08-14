package com.zen.spark.preprocess

import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
 * @Author: xiongjun
 * @Date: 2020/6/9 11:07
 * @description
 * @reviewer
 */
object DataSampling {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DataSampling").getOrCreate()

    val rowRdd = spark.read.textFile("/data/lves/ad/20200608/adverBaseInfo")
      .rdd.map(row=>{
      row.split('|')
    })
    val adProfileRow = rowRdd.map(arr=>{
      val gameId = if(arr(0).nonEmpty){arr(0).toInt}else -1
      val userId = arr(1).toInt
      val province = if(arr(2).nonEmpty){arr(2)}else "unknow"
      val city = if(arr(3).nonEmpty){arr(3)}else "unknow"
      val channel = if(arr(4).nonEmpty){arr(4)}else "unknow"
      val os = if(arr(5).nonEmpty){arr(5)}else "unknow"
      val registerDate = if(arr(6).nonEmpty){arr(6)}else "unknow"
      val totalConsumption = if(arr(7).nonEmpty){arr(7).toDouble}else 0.0
      val last15consumption = if(arr(8).nonEmpty){arr(8).toDouble}else 0.0
      val reg = "^\\d+$".r
      val advertiser = if(arr(9).nonEmpty&&reg.findFirstMatchIn(arr(9))!=None){
        arr(9).toInt
      }else -1
      val adPos = if(arr(10).nonEmpty){arr(10)}else "unknow"
      val sceneId = if(arr(11).nonEmpty){arr(11)}else "unknow"
      val materialId = if(arr(12).nonEmpty){arr(12)}else "unknow"
      val appname = if(arr(13).nonEmpty){arr(13)}else "unknow"
      val label = arr(14).toInt
      Row(gameId,userId,province,city,channel,os,registerDate,totalConsumption,last15consumption,advertiser,adPos,sceneId,materialId,appname,label)
    })
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
    val adProfileDF = spark.createDataFrame(adProfileRow,adProfileSchema)

    val adProfilePos = adProfileDF.filter("label==1")
    val adProfileNeg = adProfileDF.filter("label==0")
    val posCount = adProfilePos.count().toDouble
    val negCount = adProfileNeg.count().toDouble
    val samplingDF = adProfileNeg.sample((posCount*10)/negCount).union(adProfilePos)
    val fields = Array("user_id","province","city","channel","os","register_date","total_consumption","last_15_consumption")
    val userProfile = adProfileDF.select(fields.head,fields.tail:_*).dropDuplicates("user_id")
    samplingDF.write.csv("/data/lves/preprocess/sampling")
    userProfile.write.csv("/data/lves/preprocess/user_profile")

    spark.close()
  }
}
