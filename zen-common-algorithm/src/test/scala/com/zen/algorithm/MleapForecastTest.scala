package com.zen.algorithm

import ml.combust.bundle.BundleFile
import ml.combust.mleap.runtime.MleapSupport._
import resource.managed


import ml.combust.mleap.core.types._
import ml.combust.mleap.runtime.frame.{DefaultLeapFrame, Row}

/**
 * @Author: morris
 * @Date: 2020/6/19 15:00
 * @description
 * @reviewer
 */
object MleapForecastTest {
  def main(args: Array[String]): Unit = {

    val destPath = "/03-ML/03-mleap/1594281517996_lr1.zip"
    val inputPath = "F:\\03-ML\\05-train-data\\03-2020-6-19\\part-00000-fd53b784-f037-4659-a8f4-e12bb2ffa95f-c000.csv"
    val dataset = Seq(
      Row(30009, 265020201, "湖北", "天门", "xiaomi.ttddzzrb.2200112411","android","2018-04-07",0.0,0.0,5,"1","300041","unknow","unknow",1))
    val schema: StructType = StructType(
      StructField("game_id",ScalarType.Int),
      StructField("user_id",ScalarType.Int),
      StructField("province",ScalarType.String),
      StructField("city",ScalarType.String),
      StructField("channel",ScalarType.String),
      StructField("os",ScalarType.String),
      StructField("register_date",ScalarType.String),
      StructField("total_consumption",ScalarType.Double),
      StructField("last_15_consumption",ScalarType.Double),
      StructField("advertiser",ScalarType.Int),
      StructField("ad_pos",ScalarType.String),
      StructField("scene_id",ScalarType.String),
      StructField("material_id",ScalarType.String),
      StructField("appname",ScalarType.String),
      StructField("label",ScalarType.Int)).get
    val frame = DefaultLeapFrame(schema, dataset)

    frame.show(5)

    val transformer = (for (bf <- managed(BundleFile(s"jar:file:${destPath}"))) yield {
      bf.loadMleapBundle().get.root
    }).tried.get

    val triedFrame = transformer.transform(frame)
    triedFrame.get.show(1)
    /*val triedFrame1 = triedFrame.get.select("probability")
    println(triedFrame1)*/


//    val triedFrame = transformer.transform(inputDF)
//    triedFrame.get.show(5)
//    transformer.transform()

  }

}
