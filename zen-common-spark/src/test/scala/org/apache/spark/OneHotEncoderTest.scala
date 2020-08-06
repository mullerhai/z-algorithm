package org.apache.spark

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{CusOneHotEncoderEstimator}
import org.apache.spark.sql.types.{IntegerType, StructField}
import org.apache.spark.sql.{Row, SparkSession, types}

/**
 * @Author: morris
 * @Date: 2020/6/22 15:00
 * @description
 * @reviewer
 */
object OneHotEncoderTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("NormalizationScalerTest")
      .master("local[*]")
      .getOrCreate()
    val rdd = spark.sparkContext.parallelize(
      Seq(Row(1, 2)
        , Row(3, 1)
        , Row(1, 1)
        , Row(5, 2)))
    val field = types.StructType(Seq(StructField("A", IntegerType), StructField("B", IntegerType)))
    val dataFrame = spark.createDataFrame(rdd, field)

    val estimator = new CusOneHotEncoderEstimator()
      .setInputCols(Array("A", "B"))
      .setOutputCols(Array("A1", "B1"))
      .setHandleInvalid("keep")
      .setDropLast(false)

    val model = estimator.fit(dataFrame)
    val frame = model.transform(dataFrame)
    frame.show()
    val pipeline = new Pipeline().setStages(Array(estimator))
    val pipelineModel = pipeline.fit(dataFrame)

//    dataFrame.write.parquet(s"F:\\03-ML\\05-train-data\\02-TestData\\OneHot")
    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\OneHot")
  }

}
