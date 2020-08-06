package org.apache.spark

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{CusVectorAssemblerEstimator, MinMaxScaler, OneHotEncoderModel, VectorAssembler}
import org.apache.spark.ml.linalg.{VectorUDT, Vectors}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StructField}
import org.apache.spark.sql.{Row, SparkSession, types}

/**
 * @Author: morris
 * @Date: 2020/6/22 15:00
 * @description
 * @reviewer
 */
object VectorAssembleTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("NormalizationScalerTest")
      .master("local[*]")
      .getOrCreate()
    val rdd = spark.sparkContext.parallelize(
      Seq(
          Row(1.0, 2, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)),Vectors.dense(1,2,4,5))
        , Row(3.0, 1, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)),Vectors.dense(2,3,4,5))
        , Row(5.0, 2, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)),Vectors.dense(3,1,34,5))
        , Row(7.0, 2, Vectors.sparse(3, Array(1, 2), Array(1.0, 3.0)),Vectors.dense(5,42,1,3))
      ))
    val field = types.StructType(Seq(
      StructField("A", DoubleType),
      StructField("B", IntegerType),
      StructField("C", new VectorUDT),
      StructField("D", new VectorUDT)

    ))
    val dataFrame = spark.createDataFrame(rdd, field)
//    dataFrame.show()


    val estimator = new CusVectorAssemblerEstimator()
//      .setInputVectorCols(Array("C","D"))
//      .setInputDoubleCols(Array("A"))
      .setInputIntCols(Array("B"))
      .setOutputCol("res")
    val model = estimator.fit(dataFrame)
    val frame = model.transform(dataFrame)
    frame.show(false)


    val minMaxScaler = new MinMaxScaler()
      .setInputCol("res")
      .setOutputCol("days_since_register_norm")
//    println(model.outputVectorDim)


    val assembler = new VectorAssembler()
      .setInputCols(Array("A","B","C","D"))
      .setOutputCol("feature")
    assembler.transform(dataFrame).show(false)




    val pipeline = new Pipeline().setStages(Array(estimator,minMaxScaler))
    val pipelineModel = pipeline.fit(dataFrame)
    pipelineModel.transform(dataFrame).show(5)

        dataFrame.write.parquet(s"F:\\03-ML\\05-train-data\\02-TestData\\Vector")
    pipelineModel.write.overwrite().save("F:\\03-ML\\01-model\\Vector")
  }

}
