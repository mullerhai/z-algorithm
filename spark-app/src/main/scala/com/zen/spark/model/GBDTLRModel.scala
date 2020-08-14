package com.zen.spark.model

import com.zen.spark.transformer.{DateParser, TimestampParser}
import com.zen.spark.preprocess.GBDTLRDataProcess
import com.zen.spark.util.Log
import ml.combust.bundle.BundleFile
import ml.combust.mleap.spark.SparkSupport._
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.classification.{CusGBTClassifier, LogisticRegression}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature._
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import resource.managed

/**
 * @Author: xiongjun
 * @Date: 2020/8/6 14:25
 * @description
 * @reviewer
 */
object GBDTLRModel extends Log {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("GBDTLRModel")/*.master("local[*]")*/
      .config("spark.sql.parquet.compression.codec", "uncompressed").getOrCreate()
    val inputPath = args(0)
//    val df = GBDTLRDataProcess.process(inputPath,spark)
    val outputPath = args(1)
    val metricPath = args(2)
    val modelPath = args(3)
    val mleapPath = args(4)
    val df = spark.read.parquet(inputPath)
    df.printSchema()

    /**
     * ['ad_id','connect_type','label','material_id','timestamp','city','device','province',
     * 'register_date','last_15_consumption',
     * 'last_visit_date','num_of_game','skip_ad_times','total_consumption']
     */
    val registeDateParser = new DateParser()
      .setInputCol("register_date")
      .setDaysToCurrentCol("days_since_register")
      .setFormat("yyyy-MM-dd")

    val mathCalInputColumns = Array[String]("days_since_register", "total_consumption", "last_15_consumption", "last_visit_date",
      "num_of_game", "skip_ad_times")
    val mathCalOutputColumns = mathCalInputColumns.map(col => s"log10_$col")
    val mathCals = mathCalInputColumns.zipWithIndex.map(colAndIdx => new MathCal()
      .setInputCol(colAndIdx._1)
      .setOutputCol(mathCalOutputColumns(colAndIdx._2))
      .setOperation(Operation.log10))

    val binaryInputColumns = Array[String]("total_consumption", "skip_ad_times")
    val binaryOutputColumns = binaryInputColumns.map(col => s"binary_$col")
    val binarizers = binaryInputColumns.zipWithIndex.map(colAndIdx => {
      new Binarizer().setInputCol(colAndIdx._1).setOutputCol(binaryOutputColumns(colAndIdx._2)).setThreshold(0 + 1e-3)
    })

    val curTimeParser = new TimestampParser().setInputCol("timestamp").setUnit(0)
      .setHourOfDayCol("hourOfDay").setDayOfWeekCol("dayOfWeek").setDayOfMonthCol("dayOfMonth")
    val stringIndexInputColumns = Array[String]("city", "ad_id", "material_id", "device")
    val stringIndexOutputColumns = stringIndexInputColumns.map(col => s"si_$col")
    val stringIndexers = stringIndexInputColumns.zipWithIndex.map(colAndIdx => {
      new CusStringIndexer().setInputCol(colAndIdx._1).setOutputCol(stringIndexOutputColumns(colAndIdx._2))
        .setHandleInvalid("keep").setStringOrderType("frequencyDesc")
    })

    val onehotInputColumns = Array("si_city", "si_ad_id", "si_material_id", "si_device", "binary_skip_ad_times")
    val onehotOutputColumns = onehotInputColumns.map(col => s"ohe_$col")
    val onehots = new CusOneHotEncoderEstimator()
      .setInputCols(onehotInputColumns)
      .setOutputCols(onehotOutputColumns)
      .setDropLast(false)
      .setHandleInvalid("keep")

    val directOneHotInputColumns = Array("province", "connect_type",
      "hourOfDay", "dayOfWeek", "dayOfMonth")
    val directOneHotOutputColumns = directOneHotInputColumns.map(col => s"dir_ohe_$col")
    val dirOnehots = new CusOneHotEncoderModel("", Array[Int](35, 5, 24, 7, 31))
      .setInputCols(directOneHotInputColumns).setOutputCols(directOneHotOutputColumns)
      .setDropLast(false).setHandleInvalid("keep")

    val connect_type_x_device = new Interaction().setInputCols(Array("dir_ohe_connect_type", "ohe_si_device")).setOutputCol("connect_type_x_device")
    val binary_skip_ad_times_x_connect_type = new Interaction()
      .setInputCols(Array("ohe_binary_skip_ad_times", "dir_ohe_connect_type")).setOutputCol("binary_skip_ad_times_x_connect_type")

    val gbdtInputFeatures = new CusVectorAssemblerEstimator().setInputIntCols(Array("days_since_register", "num_of_game", "skip_ad_times",
      "last_visit_date", "hourOfDay", "dayOfWeek")).setInputDoubleCols(Array("total_consumption", "last_15_consumption"))
      .setInputVectorCols(Array("dir_ohe_connect_type")).setOutputCol("gbdtInputFeatures")

    val gbt = new CusGBTClassifier().setFeaturesCol(gbdtInputFeatures.getOutputCol).setLabelCol("label")
      .setGbtGeneratedFeaturesCol("gbtOutputFeatures").setMaxIter(10).setMaxDepth(3).setMinInstancesPerNode(10).setStepSize(0.5)
    val cusVectorAssembler = new CusVectorAssemblerEstimator()
      .setInputDoubleCols(mathCalOutputColumns ++ binaryOutputColumns /*++bucketizerExtendEstimator.getOutputCols*/)
      .setInputVectorCols(
        onehotOutputColumns ++ directOneHotOutputColumns ++ Array("connect_type_x_device", "binary_skip_ad_times_x_connect_type",
          gbt.getGbtGeneratedFeaturesCol))
      .setOutputCol("features")
    val lr = new LogisticRegression()
      .setFeaturesCol(cusVectorAssembler.getOutputCol)
      .setLabelCol("label")
      .setFitIntercept(true)
      .setElasticNetParam(0.1)
      .setRegParam(0.02)
      .setProbabilityCol("probability")
    val pipeline = new Pipeline().setStages(Array(registeDateParser) ++ binarizers
      ++ mathCals ++ Array(curTimeParser /*,bucketizerExtendEstimator*/) ++ stringIndexers
      ++ Array(onehots, dirOnehots, connect_type_x_device, binary_skip_ad_times_x_connect_type, gbdtInputFeatures, gbt, cusVectorAssembler, lr))

    val paramGrid = new ParamGridBuilder()
      .addGrid(gbt.stepSize, Range(1, 11).map(_ / 10.0).toArray)
      .addGrid(gbt.maxIter, Range(10, 20, 2).toArray)
      .addGrid(gbt.maxDepth, Range(3, 6).toArray)
      .addGrid(gbt.minInstancesPerNode, Range(1, 50, 10).toArray)
      .addGrid(lr.elasticNetParam, Range(0, 11).map(_ / 100.0).toArray)
      .addGrid(lr.regParam, Range(0, 11).map(_ / 100.0).toArray)
      .addGrid(lr.maxIter, Range(800, 1800, 200).toArray)
      .build()
    val cv = new CrossValidator()
      .setEstimator(pipeline)
      .setEvaluator(new BinaryClassificationEvaluator)
      .setEstimatorParamMaps(paramGrid)
      .setNumFolds(5) // Use 3+ in practice
      .setParallelism(8) // Evaluate up to 2 parameter settings in parallel

//    val testRdd = spark.sparkContext.parallelize[Row](Seq(Row(cv.getEstimatorParamMaps(0).toString())))
//    val testDf = spark.createDataFrame(testRdd,StructType(Seq(StructField("bestParams", StringType))))
//    testDf.show(false)

    val cvModel = cv.fit(df)
    val index = cvModel.avgMetrics.zipWithIndex.minBy(-_._1)._2

    logInfo(s"best params: ${cv.getEstimatorParamMaps(index)}")
    logInfo(s"best auc:${cvModel.avgMetrics(index)}")
    logInfo(s"avg auc: ${cvModel.avgMetrics.mkString(",")}")

    val bestModel = cvModel.bestModel.asInstanceOf[PipelineModel]
    val transformedDF = bestModel.transform(df)
    //    transformedDF.repartition(1).write.parquet(outputPath)
    //    val scoreAndLabels = transformedDF.select("probability","label")
    //      .rdd.map(row=>{
    //      (row.getAs[DenseVector](0).values(1),row.getInt(1).toDouble)
    //    })
    //    val binaryClassificationMetrics = new BinaryClassificationMetrics(scoreAndLabels)
    //    val auc = binaryClassificationMetrics.areaUnderROC()
    //    println(s"auc:$auc")
    val rdd = spark.sparkContext.parallelize[Row](Seq(Row(cv.getEstimatorParamMaps(index).toString(), cvModel.avgMetrics(index), cvModel.avgMetrics.mkString(","))))
    val metricDF = spark.createDataFrame(rdd, StructType(Seq(StructField("bestParams", StringType),
      StructField("bestAuc", DoubleType), StructField("avgAuc", StringType))))
    metricDF.repartition(1).write.parquet(metricPath)
    bestModel.write.save(modelPath)

    val sbc = SparkBundleContext().withDataset(transformedDF)
    for (bf <- managed(BundleFile(s"jar:file:$mleapPath"))) {
      bestModel.writeBundle.save(bf)(sbc).get
    }

  }
}
