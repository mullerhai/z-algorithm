package com.zen.algorithm

import ml.combust.bundle.BundleFile
import ml.combust.mleap.core.types._
import ml.combust.mleap.runtime.MleapSupport._
import ml.combust.mleap.runtime.frame.{DefaultLeapFrame, Row}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.mllib.linalg.VectorUDT
import resource.managed

/**
 * @Author: morris
 * @Date: 2020/6/19 15:00
 * @description
 * @reviewer
 */
object MleapForecastTest2 {
  def main(args: Array[String]): Unit = {

    val destPath = "/03-ML/03-mleap/1594865229163_data.zip"


    val rows = Seq(
      Row("zz", 5.0, 1),
      Row("aaz", 6.0, 2),
      Row("bbzz", 7.0, 3)
    )

    val schema: StructType = StructType(
      StructField("A",ScalarType.String),
      StructField("B",ScalarType.Double),
      StructField("C",ScalarType.Int)
   ).get
    val frame = DefaultLeapFrame(schema, rows)
    frame.show(5)

    val transformer = (for (bf <- managed(BundleFile(s"jar:file:${destPath}"))) yield {
      bf.loadMleapBundle().get.root
    }).tried.get

    val triedFrame = transformer.transform(frame)
    triedFrame.get.show()
//    val triedFrame = transformer.transform(inputDF)
//    triedFrame.get.show(5)
//    transformer.transform()

  }

}
