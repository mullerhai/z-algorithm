package org.apache.spark

import com.zen.spark.transformer.DateParser
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{NormalizationScaler, VectorAssembler}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField}
import org.apache.spark.sql.{DataFrame, Row, SparkSession, types}

/**
 * @Author: morris
 * @Date: 2020/6/15 14:10
 * @description
 * @reviewer
 */
object DateTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("NormalizationScalerTest")
      .master("local[*]")
      .getOrCreate()
    val rdd = spark.sparkContext.parallelize(Seq(Row("2019-01-01", 2.0)))
    val field = types.StructType(Seq(StructField("register_date", StringType), StructField("B", DoubleType)))
    val dataFrame: DataFrame = spark.createDataFrame(rdd, field)

    val dateParser = new DateParser()
      .setInputCol("register_date")
      .setDaysToCurrentCol("days_since_register")
      .setFormat("yyyy-MM-dd")
    new VectorAssembler()
    val pipeline = new Pipeline().setStages(Array(dateParser))
    val pipelineModel = pipeline.fit(dataFrame)



    dataFrame.write.parquet(s"F:\\03-ML\\05-train-data\\02-TestData\\dateParser${System.currentTimeMillis()}")

//    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\NormalizationScaler")
    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\dateParser")





  }

}
