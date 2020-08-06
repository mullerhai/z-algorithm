package com.zen.algorithm

import com.zen.spark.transformer.DateParser
import ml.combust.bundle.BundleFile
import ml.combust.mleap.spark.SparkSupport._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.DenseVector
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.functions.{regexp_replace, _}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import resource.managed

/**
 * @Author: xiongjun
 * @Date: 2020/6/10 9:56
 * @description
 * @reviewer
 */
object AllFeatureTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("LRModel").master("local[*]").getOrCreate()
    val inputPath = "F:\\03-ML\\05-train-data\\03-2020-6-19\\part-00000-fd53b784-f037-4659-a8f4-e12bb2ffa95f-c000.csv"
    val outputPath = "F:\\03-ML\\05-train-data\\03-2020-6-19\\lr"
    /* val metricPath = args(2)
     val modelPath = args(3)
     val mleapPath = args(4)*/
    val maxIters = 100
    val adProfileSchema = StructType(Seq(
      StructField("game_id", IntegerType),
      StructField("user_id", IntegerType),
      StructField("province", StringType),
      StructField("city", StringType),
      StructField("channel", StringType),
      StructField("os", StringType),
      StructField("register_date", StringType),
      StructField("total_consumption", DoubleType),
      StructField("last_15_consumption", DoubleType),
      StructField("advertiser", IntegerType),
      StructField("ad_pos", StringType),
      StructField("scene_id", StringType),
      StructField("material_id", StringType),
      StructField("appname", StringType),
      StructField("label", IntegerType)
    ))
    val inputDF = spark.read.schema(adProfileSchema).csv(inputPath)

   val array = inputDF.randomSplit(Array(0.3, 0.6))
    val frame1 = array(1)

    val df = array(0).withColumn("label", regexp_replace(array(0)("label"), "0", "1"))
    val frame = df.withColumn("label", col("label").cast(IntegerType))
    frame1.union(frame)

    val splitCols = Array[String]("channel", "register_date")
    val splitOutCols = splitCols.map(x => s"sp_${x}")
    val splitCounter = new SplitCounter()
      .setInputCols(splitCols)
      .setOutputCols(splitOutCols)
      .setDelimiterArray(Array(".","-"))

    val norCols = Array[String]("game_id", "user_id")
    val norOutCols = norCols.map(x => s"nor_${x}")
    val normalizationScaler = new NormalizationScaler()
      .setInputCols(norCols)
      .setOutputCols(norOutCols)
      .setAlgorithmType(1)

      .setMax(Array(1,0.8))
      .setMin(Array(0,0.5))

    val stringIndexCols = Array[String]("province", "city")
    val stringIndexOutCols = stringIndexCols.map(x => s"si_${x}")

    val stringIndexers = stringIndexCols.zipWithIndex.map(idx => {
      new StringIndexer()
        .setInputCol(idx._1)
        .setOutputCol(stringIndexOutCols(idx._2))
        .setHandleInvalid("keep")
      //        .setStringOrderType()
    })

    val multiHotCols = Array[String]("channel")
    val multiHotOutCols = multiHotCols.map(x => s"mh_${x}")
    val multiHotEncoderEstimator = new MultiHotEncoderEstimator()
      .setInputCols(multiHotCols)
      .setOutputCols(multiHotOutCols)
      .setDelimiter("\\.")
      .setHandleInvalid("keep")

/*
    val mathCols = Array[String]("total_consumption","last_15_consumption")
    val mathOutCols = mathCols.map(x => s"loge_${x}")
    mathCols.zipWithIndex.map(idx =>{
      new MathCal()
        .setInputCol()
    })
    val mathCal = new MathCal()
    .setInputCol()*/


    val stages1 =  Array(normalizationScaler)
//    val stages1 = Array(splitCounter,multiHotEncoderEstimator,normalizationScaler) ++ stringIndexers









    val pipeline1 = new Pipeline()
      .setStages(stages1)
    val splitDF1 = frame1.union(frame).randomSplit(Array(0.8,0.2))
    val trainDF1 = splitDF1(0)
    val testDF1 = splitDF1(1)
    val pipelineModel1 = pipeline1.fit(trainDF1)
    val testDFTransform1 = pipelineModel1.transform(testDF1)
    val trainDFtransformed1 = pipelineModel1.transform(trainDF1)
    trainDFtransformed1.show(20,false)
    val transformedDF1 = trainDFtransformed1.union(testDFTransform1)

    pipelineModel1.write.overwrite().save("F:\\03-ML\\01-model\\Vector")
    val sbc1 = SparkBundleContext().withDataset(transformedDF1)
    for (bf <- managed(BundleFile(s"jar:file:/03-ML/03-mleap/${System.currentTimeMillis()}_lrTest.zip"))) {
      pipelineModel1.writeBundle.save(bf)(sbc1).get
    }









    //    HdfsUtils.uploadModel(mleapPath, localMleapPath)

    // 删除临时文件
    //    new File(localMleapPath).delete()

    spark.close()
  }
}
