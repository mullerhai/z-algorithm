package com.zen.spark.model

import com.zen.spark.transformer.{DateParser, TimestampParser}
import ml.combust.bundle.BundleFile
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.DenseVector
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.types.{DoubleType, StructField, StructType,StringType}
import org.apache.spark.sql.{Row, SparkSession}
import resource.managed
import ml.combust.mleap.spark.SparkSupport._
/**
 * @Author: xiongjun
 * @Date: 2020/7/15 15:09
 * @description
 * @reviewer
 */
object OfficialLR3 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("OfficialLR3").master("local[*]")
      .config("spark.sql.parquet.compression.codec", "uncompressed").getOrCreate()
    val inputPath = args(0)
    val outputPath = args(1)
    val metricPath = args(2)
    val modelPath = args(3)
    val mleapPath = args(4)
    val maxIters = args(5).toInt
    val df = spark.read.parquet(inputPath)
    df.printSchema()
    /**
     * ['ad_id','channel','connect_type','cur_app_id','label','material_id','timestamp','city','device','province',
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
    val bucketizerExtendEstimator = new BucketizerExtendEstimator()
      .setRule(BucketizerRule.equifrequent)
      .setNumOfBucket(3)
      .setInputCols(mathCalOutputColumns)
      .setOutputCols(mathCalOutputColumns.map(col => s"bucket_$col"))
    val binaryInputColumns = Array[String]("total_consumption", "skip_ad_times")
    val binaryOutputColumns = binaryInputColumns.map(col => s"binary_$col")
    val binarizers = binaryInputColumns.zipWithIndex.map(colAndIdx => {
      new Binarizer().setInputCol(colAndIdx._1).setOutputCol(binaryOutputColumns(colAndIdx._2)).setThreshold(0 + 1e-3)
    })

    val curTimeParser = new TimestampParser().setInputCol("timestamp").setUnit(0)
      .setHourOfDayCol("hourOfDay").setDayOfWeekCol("dayOfWeek").setDayOfMonthCol("dayOfMonth")
    val stringIndexInputColumns = Array[String]("city", "ad_id", "material_id", "device","channel","cur_app_id")
    val stringIndexOutputColumns = stringIndexInputColumns.map(col => s"si_$col")
    val stringIndexers = stringIndexInputColumns.zipWithIndex.map(colAndIdx => {
      new CusStringIndexer().setInputCol(colAndIdx._1).setOutputCol(stringIndexOutputColumns(colAndIdx._2))
        .setHandleInvalid("keep").setStringOrderType("frequencyDesc")
    })

    val onehotInputColumns = Array("si_city", "si_ad_id", "si_material_id", "si_device", "binary_skip_ad_times",
      "si_channel","si_cur_app_id")
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
    val channel_x_curr_app = new Interaction().setInputCols(Array("ohe_si_channel","ohe_si_cur_app_id"))
      .setOutputCol("channel_x_curr_app")

    val cusVectorAssembler = new CusVectorAssemblerEstimator()
      .setInputDoubleCols(mathCalOutputColumns ++ binaryOutputColumns /*++bucketizerExtendEstimator.getOutputCols*/).setInputVectorCols(
      onehotOutputColumns ++ directOneHotOutputColumns ++ Array("connect_type_x_device", "binary_skip_ad_times_x_connect_type","channel_x_curr_app"))
      .setOutputCol("features")
    val lr = new LogisticRegression()
      .setFeaturesCol(cusVectorAssembler.getOutputCol)
      .setLabelCol("label")
      .setFitIntercept(true)
      .setElasticNetParam(0.0)
      .setRegParam(0.08)
      .setMaxIter(maxIters)
      .setProbabilityCol("probability")

    val pipeline = new Pipeline().setStages(Array(registeDateParser) ++ binarizers
      ++ mathCals ++ Array(curTimeParser /*,bucketizerExtendEstimator*/) ++ stringIndexers
      ++ Array(onehots, dirOnehots, connect_type_x_device, binary_skip_ad_times_x_connect_type,channel_x_curr_app, cusVectorAssembler, lr))

    val paramGrid = new ParamGridBuilder()
//      .addGrid(lr.elasticNetParam, Range(0, 11).map(_ / 10.0).toArray)
//      .addGrid(lr.regParam, Range(0, 11).map(_ / 100.0).toArray)
      .addGrid(lr.maxIter, Range(800, 1800, 200).toArray)
      .build()
    val cv = new CrossValidator()
      .setEstimator(pipeline)
      .setEvaluator(new BinaryClassificationEvaluator)
      .setEstimatorParamMaps(paramGrid)
      .setNumFolds(5) // Use 3+ in practice
      .setParallelism(8) // Evaluate up to 2 parameter settings in parallel
   /* val cvModel = cv.fit(df)
    val index = cvModel.avgMetrics.zipWithIndex.minBy(-_._1)._2
    println(s"best params: ${cv.getEstimatorParamMaps(index)}")
    println(s"best auc:${cvModel.avgMetrics(index)}")
    println(s"avg auc: ${cvModel.avgMetrics.mkString(",")}")*/


    // 使用最佳参数训练
    val lr2 = new LogisticRegression()
      .setFeaturesCol(cusVectorAssembler.getOutputCol)
      .setLabelCol("label")
      .setFitIntercept(true)
      .setElasticNetParam(0.0)
      .setRegParam(0.08)
      .setMaxIter(800)
      .setProbabilityCol("probability")
    val pipelineRes = new Pipeline().setStages(Array(registeDateParser) ++ binarizers
      ++ mathCals ++ Array(curTimeParser /*,bucketizerExtendEstimator*/) ++ stringIndexers
      ++ Array(onehots, dirOnehots, connect_type_x_device, binary_skip_ad_times_x_connect_type,channel_x_curr_app, cusVectorAssembler
      , lr2))

    //    val splitDF = df.randomSplit(Array(0.7,0.3))
    //    val trainDF = splitDF(0)
    //    val testDF = splitDF(1)
        val pipelineModel = pipelineRes.fit(df)
        val transformedDF = pipelineModel.transform(df)
        transformedDF.select("probability","prediction").show(false)
    //    transformedDF.select("dir_ohe_os").show(false)
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
