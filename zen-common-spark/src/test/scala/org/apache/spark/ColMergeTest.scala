package org.apache.spark

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.ColumnMerge
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField}
import org.apache.spark.sql.{Row, SparkSession, types}

/**
 * @Author: morris
 * @Date: 2020/6/18 11:11
 * @description
 * @reviewer
 */
object ColMergeTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("NormalizationScalerTest")
      .master("local[*]")
      .getOrCreate()
    val rdd = spark.sparkContext.parallelize(Seq(
      Row("zz", 5.0, 1),
      Row("aaz", 6.0, 2),
      Row("bbzz", 7.0, 3)
    ))
    val field = types.StructType(Seq(StructField("A", StringType), StructField("B", DoubleType), StructField("C", IntegerType)))
    val dataFrame = spark.createDataFrame(rdd, field)


    //    val value = new OneHotEncoderEstimator().fit(dataFrame)


    val columnMerge = new ColumnMerge()
      .setInputCols(Array("A", "B", "C"))
      .setOutputCol("zz")
//      .setDelimiter("_")

    val frame = columnMerge.transform(dataFrame)
    frame.show()
    val pipeline = new Pipeline().setStages(Array(columnMerge))
    val pipelineModel = pipeline.fit(dataFrame)


        dataFrame.write.parquet(s"F:\\03-ML\\05-train-data\\02-TestData\\ColumnMerge")

    //    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\NormalizationScaler")
    //    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\StandAndNor")
        pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\ColumnMerge")


  }
}
