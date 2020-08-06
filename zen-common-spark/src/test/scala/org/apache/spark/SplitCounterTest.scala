package org.apache.spark

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{OneHotEncoderEstimator, SplitCounter}
import org.apache.spark.sql.types.{StringType, StructField}
import org.apache.spark.sql.{Row, SparkSession, types}

/**
 * @Author: morris
 * @Date: 2020/6/18 11:11
 * @description
 * @reviewer
 */
object SplitCounterTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("NormalizationScalerTest")
      .master("local[*]")
      .getOrCreate()
    val rdd = spark.sparkContext.parallelize(Seq(Row("1.0,2,3,5", "2.0|32|11|111|777")))
    val field = types.StructType(Seq(StructField("A", StringType), StructField("B", StringType)))
    val dataFrame = spark.createDataFrame(rdd, field)


    val value = new OneHotEncoderEstimator().fit(dataFrame)


    val splitCounter = new SplitCounter("1")
      .setInputCols(Array("A", "B"))
      .setOutputCols(Array("A1", "B1"))
      .setDelimiterArray(Array(",", "|"))
    val pipeline = new Pipeline().setStages(Array(splitCounter))
    val pipelineModel = pipeline.fit(dataFrame)




//    dataFrame.write.parquet(s"F:\\03-ML\\05-train-data\\02-TestData\\split")

    //    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\NormalizationScaler")
//    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\StandAndNor")
    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\SplitCounter")


  }
}
