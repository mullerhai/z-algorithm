package org.apache.spark

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.NormalizationScaler
import org.apache.spark.sql.types.{DoubleType, StructField}
import org.apache.spark.sql.{DataFrame, Row, SparkSession, types}

/**
 * @Author: morris
 * @Date: 2020/6/15 14:10
 * @description
 * @reviewer
 */
object NormalizationTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("NormalizationScalerTest")
      .master("local[*]")
      .getOrCreate()
    val rdd = spark.sparkContext.parallelize(Seq(Row(1.0, 2.0), Row(2.0, 3.0), Row(5.0, 4.0),Row(5.0, 4.0),Row(5.0, 4.0)))
    val field = types.StructType(Seq(StructField("features", DoubleType), StructField("B", DoubleType)))
    val dataFrame: DataFrame = spark.createDataFrame(rdd, field)

    val normalize = new NormalizationScaler("1")
      .setInputCols(Array("features","B"))
      .setOutputCols(Array("features1","B1"))
      .setAlgorithmType(1)
      .setMin(Array(0,0.1))
      .setMax(Array(0.8,1))
    val Standardization = new NormalizationScaler("1")
      .setInputCols(Array("features","B"))
      .setOutputCols(Array("features2","B2"))
      .setAlgorithmType(2)
      .setMin(Array(-1000,0.1))
      .setMax(Array(0.8,1))
    val pipeline = new Pipeline().setStages(Array(normalize,Standardization))
    val pipelineModel = pipeline.fit(dataFrame)

    val model = normalize.fit(dataFrame)
    val frame = model.transform(dataFrame)

    dataFrame.write.parquet(s"F:\\03-ML\\05-train-data\\02-TestData\\nor${System.currentTimeMillis()}")

//    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\NormalizationScaler")
    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\StandAndNor")





  }

}
