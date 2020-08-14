package com.zen.spark.model

import com.zen.spark.transformer.{DateParser, TimestampParser}
import ml.combust.bundle.BundleFile
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.feature.{CusOneHotEncoderEstimator, CusOneHotEncoderModel, CusVectorAssemblerEstimator, MathCal, Operation, StringIndexer}
import org.apache.spark.ml.linalg.DenseVector
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import resource.managed
import ml.combust.mleap.spark.SparkSupport._

/**
 * @Author: xiongjun
 * @Date: 2020/7/15 15:09
 * @description
 * @reviewer
 */
object OfficialLR {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("OfficialLR").master("local[*]")
      .config("spark.sql.parquet.compression.codec","uncompressed").getOrCreate()
    val inputPath = args(0)
    val outputPath = args(1)
    val metricPath = args(2)
    val modelPath = args(3)
    val mleapPath = args(4)
    val maxIters = args(5).toInt
    val df = spark.read.parquet(inputPath)
    df.printSchema()

    val registeDateParser = new DateParser()
      .setInputCol("register_date")
      .setDaysToCurrentCol("days_since_register")
      .setFormat("yyyy-MM-dd")

    val mathCalInputColumns = Array[String]("days_since_register")
    val mathCalOutputColumns = mathCalInputColumns.map(col=>s"loge_$col")
    val mathCals = mathCalInputColumns.zipWithIndex.map(colAndIdx=>new MathCal()
      .setInputCol(colAndIdx._1)
      .setOutputCol(mathCalOutputColumns(colAndIdx._2))
      .setOperation(Operation.loge))
//    val columnMerge = new ColumnMerge().setInputCols(Array("ad_id","material_id"))
//      .setOutputCol("ad_material_id").setDelimiter("_")
    val curTimeParser = new TimestampParser().setInputCol("timestamp").setUnit(0)
      .setHourOfDayCol("hourOfDay").setDayOfWeekCol("dayOfWeek").setDayOfMonthCol("dayOfMonth")
    val stringIndexInputColumns = Array[String]("scene_id","city","ad_id","material_id","device")
    val stringIndexOutputColumns = stringIndexInputColumns.map(col=>s"si_$col")
    val stringIndexers = stringIndexInputColumns.zipWithIndex.map(colAndIdx=>{
      new StringIndexer().setInputCol(colAndIdx._1).setOutputCol(stringIndexOutputColumns(colAndIdx._2))
        .setHandleInvalid("keep").setStringOrderType("frequencyDesc")
    })

    val onehotInputColumns = Array("si_scene_id","si_city","si_ad_id","si_material_id","si_device")
    val onehotOutputColumns = onehotInputColumns.map(col=>s"ohe_$col")
    val onehots = new CusOneHotEncoderEstimator()
      .setInputCols(onehotInputColumns)
      .setOutputCols(onehotOutputColumns)
      .setDropLast(false)
      .setHandleInvalid("keep")

    val directOneHotInputColumns = Array("connect_type","os",
      "hourOfDay","dayOfWeek","dayOfMonth")
    val directOneHotOutputColumns = directOneHotInputColumns.map(col=>s"dir_ohe_$col")
    val dirOnehots = new CusOneHotEncoderModel("",Array[Int](5,2,24,7,31))
      .setInputCols(directOneHotInputColumns).setOutputCols(directOneHotOutputColumns)
      .setDropLast(false).setHandleInvalid("keep")

    val cusVectorAssembler = new CusVectorAssemblerEstimator()
      .setInputDoubleCols(mathCalOutputColumns).setInputVectorCols(onehotOutputColumns++directOneHotOutputColumns)
      .setOutputCol("features")
    val lr = new LogisticRegression()
      .setFeaturesCol(cusVectorAssembler.getOutputCol)
      .setLabelCol("label")
      .setFitIntercept(true)
      .setElasticNetParam(0)
      .setRegParam(0.5)
      .setMaxIter(maxIters)
      .setProbabilityCol("probability")

    val pipeline = new Pipeline().setStages(Array(registeDateParser)++mathCals++Array(curTimeParser)
    ++stringIndexers++Array(onehots,dirOnehots,cusVectorAssembler,lr))
    val pipelineModel = pipeline.fit(df)
    val transformedDF = pipelineModel.transform(df)
    transformedDF.select("probability","prediction").show(false)
    transformedDF.select("dir_ohe_os").show(false)
    transformedDF.repartition(1).write.parquet(outputPath)
    val scoreAndLabels = transformedDF.select("probability","label")
      .rdd.map(row=>{
      (row.getAs[DenseVector](0).values(1),row.getInt(1).toDouble)
    })
    val coeff = pipelineModel.stages.last.asInstanceOf[LogisticRegressionModel].coefficients
    println(s"coeff:${coeff.toArray.mkString(",")}")
    val binaryClassificationMetrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auc = binaryClassificationMetrics.areaUnderROC()
    println(s"auc:$auc")
    val rdd = spark.sparkContext.parallelize[Row](Seq(Row(auc)))
    val metricDF = spark.createDataFrame(rdd,StructType(Seq(StructField("auc",DoubleType))))
    metricDF.repartition(1).write.parquet(metricPath)
    pipelineModel.write.save(modelPath)

    val sbc = SparkBundleContext().withDataset(transformedDF)
    for (bf <- managed(BundleFile(s"jar:file:$mleapPath"))) {
      pipelineModel.writeBundle.save(bf)(sbc).get
    }

//    val localMleapPath = s"${System.getProperty("java.io.tmpdir")}/" +
//      s"${HdfsUtils.getFileName(mleapPath)}_${System.currentTimeMillis}.zip"
//    val sbc = SparkBundleContext().withDataset(transformedDF)
//    for (bf <- managed(BundleFile(s"jar:file:$localMleapPath"))) {
//      pipelineModel.writeBundle.save(bf)(sbc).get
//    }
//    HdfsUtils.uploadModel(mleapPath, localMleapPath)
//
//    // 删除临时文件
//    new File(localMleapPath).delete()

    spark.close()
  }
}
