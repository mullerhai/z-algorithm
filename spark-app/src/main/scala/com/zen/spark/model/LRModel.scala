package com.zen.spark.model

import java.io.File

import com.zen.spark.transformer.{ColSelector, DateParser}
import com.zen.spark.util.HdfsUtils
import ml.combust.bundle.BundleFile
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{Bucketizer, MathCal, MinMaxScaler, OneHotEncoderEstimator, Operation, StringIndexer, VectorAssembler}
import org.apache.spark.ml.linalg.DenseVector
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import resource.managed
import ml.combust.mleap.spark.SparkSupport._
/**
 * @Author: xiongjun
 * @Date: 2020/6/10 9:56
 * @description
 * @reviewer
 */
object LRModel {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("LRModel").getOrCreate()
    val inputPath = args(0)
    val outputPath = args(1)
    val metricPath = args(2)
    val modelPath = args(3)
    val mleapPath = args(4)
    val maxIters = args(5).toInt
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
    val inputDF = spark.read.schema(adProfileSchema).csv(inputPath)
    //    inputDF.printSchema()
    //对total_consumption进行对数变换
    val mathCalInputColumns = Array[String]("total_consumption","last_15_consumption")
    val mathCalOutputColumns = mathCalInputColumns.map(col=>s"loge_$col")
    val mathCals = mathCalInputColumns.zipWithIndex.map(colAndIdx=>new MathCal()
      .setInputCol(colAndIdx._1)
      .setOutputCol(mathCalOutputColumns(colAndIdx._2))
      .setOperation(Operation.loge))
    val loge_consumption_bucketizers = new Bucketizer()
      .setInputCols(Array[String]("loge_total_consumption","loge_last_15_consumption"))
      .setOutputCols(Array[String]("loge_total_consumption_buck","loge_last_15_consumption_buck"))
      .setSplitsArray(Array(Array(0.0,1.0,6.0,10.0,Double.PositiveInfinity),Array(0.0,1.0,6.0,10.0,Double.PositiveInfinity)))
      .setHandleInvalid("keep")

    val dateParser = new DateParser()
      .setInputCol("register_date")
      .setDaysToCurrentCol("days_since_register")
      .setFormat("yyyy-MM-dd")
    val dateParserVec = new VectorAssembler()
      .setInputCols(Array(dateParser.getDaysToCurrentCol))
      .setOutputCol("days_since_register_vec")

    val minMaxScaler = new MinMaxScaler()
      .setInputCol("days_since_register_vec")
      .setOutputCol("days_since_register_norm")

    val stringIndexInputColumns = Array[String]("game_id","province","city","channel","os","advertiser","ad_pos","scene_id","material_id","appname")
    val stringIndexOutputColumns = stringIndexInputColumns.map(col=>s"si_$col")
    val stringIndexers = stringIndexInputColumns.zipWithIndex.map(colAndIdx=>{
      new StringIndexer().setInputCol(colAndIdx._1).setOutputCol(stringIndexOutputColumns(colAndIdx._2))
        .setHandleInvalid("keep").setStringOrderType("frequencyDesc")
    })
    val onehotInputColumns = Array("si_game_id","si_province","si_city","si_channel","si_os","si_advertiser","si_ad_pos","si_scene_id",
      "si_material_id","si_appname")
    val onehotOutputColumns = onehotInputColumns.map(col=>s"oh_$col")
    val onehots = new OneHotEncoderEstimator()
      .setInputCols(onehotInputColumns)
      .setOutputCols(onehotOutputColumns)
      .setDropLast(false)
      .setHandleInvalid("keep")
    val vectorAssemblerInputColumns = mathCalOutputColumns++loge_consumption_bucketizers.getOutputCols++Array(
      minMaxScaler.getOutputCol)++stringIndexOutputColumns++onehotOutputColumns

    val vectorAssembler = new VectorAssembler()
      .setInputCols(vectorAssemblerInputColumns)
      .setOutputCol("features")
    val colSelector = new ColSelector().setCols(vectorAssemblerInputColumns++Array("label"))
    val lr = new LogisticRegression()
      .setFeaturesCol("features")
      .setMaxIter(maxIters)
      .setElasticNetParam(0.5)
      .setLabelCol("label")
      .setRegParam(0.1)
      .setFitIntercept(true)
      .setProbabilityCol("probability")
    val stages = mathCals++Array(loge_consumption_bucketizers,
      dateParser,dateParserVec,minMaxScaler)++stringIndexers++Array(onehots,colSelector,vectorAssembler,lr)
    val pipeline = new Pipeline()
      .setStages(stages)
    val splitDF = inputDF.randomSplit(Array(0.8,0.2))
    val trainDF = splitDF(0)
    val testDF = splitDF(1)
    val pipelineModel = pipeline.fit(trainDF)
    val testDFTransform = pipelineModel.transform(testDF)
    val trainDFtransformed = pipelineModel.transform(trainDF)
    val transformedDF = trainDFtransformed.union(testDFTransform)
    transformedDF.write.parquet(outputPath)
    val scoreAndLabels = testDFTransform.select("probability","label")
      .rdd.map(row=>{
      (row.getAs[DenseVector](0).values(0),row.getInt(1).toDouble)
    })
    val binaryClassificationMetrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auc = binaryClassificationMetrics.areaUnderROC()
    val rdd = spark.sparkContext.parallelize[Row](Seq(Row(auc)))
    val metricDF = spark.createDataFrame(rdd,StructType(Seq(StructField("auc",DoubleType))))
    metricDF.repartition(1).write.parquet(metricPath)
    pipelineModel.write.save(modelPath)

    val localMleapPath = s"${System.getProperty("java.io.tmpdir")}/" +
      s"${HdfsUtils.getFileName(mleapPath)}_${System.currentTimeMillis}.zip"
    val sbc = SparkBundleContext().withDataset(transformedDF)
    for (bf <- managed(BundleFile(s"jar:file:$localMleapPath"))) {
      pipelineModel.writeBundle.save(bf)(sbc).get
    }
    HdfsUtils.uploadModel(mleapPath, localMleapPath)

    // 删除临时文件
    new File(localMleapPath).delete()

    spark.close()
  }
}
