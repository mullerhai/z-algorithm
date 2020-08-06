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
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import resource.managed
import org.apache.spark.sql.functions.regexp_replace
import org.apache.spark.sql.functions._

/**
 * @Author: xiongjun
 * @Date: 2020/6/10 9:56
 * @description
 * @reviewer
 */
object LRModel {
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




    //    inputDF.printSchema()
    /* inputDF.show(5)
     return*/
    //对total_consumption进行对数变换
    val mathCalInputColumns = Array[String]("total_consumption", "last_15_consumption")
    val mathCalOutputColumns = mathCalInputColumns.map(col => s"loge_$col")
    val mathCals = mathCalInputColumns.zipWithIndex.map(colAndIdx => new MathCal()
      .setInputCol(colAndIdx._1)
      .setOutputCol(mathCalOutputColumns(colAndIdx._2))
      .setOperation(Operation.loge))
    val loge_consumption_bucketizers = new CusBucketizer()
      .setInputCols(Array[String]("loge_total_consumption", "loge_last_15_consumption"))
      .setOutputCols(Array[String]("loge_total_consumption_buck", "loge_last_15_consumption_buck"))
      .setSplitsArray(Array(Array(Double.NegativeInfinity, 0.0, 1.0, 6.0, 10.0, Double.PositiveInfinity), Array(Double.NegativeInfinity, 0.0, 1.0, 6.0, 10.0, Double.PositiveInfinity)))
      .setHandleInvalid("keep")

    val dateParser = new DateParser()
      .setInputCol("register_date")
      .setDaysToCurrentCol("days_since_register")
      .setFormat("yyyy-MM-dd")


    val dateParserVec = new CusVectorAssemblerEstimator()
      .setInputIntCols(Array(dateParser.getDaysToCurrentCol))
      .setOutputCol("days_since_register_vec")

    /*  val dateParserVec = new VectorAssembler()
        .setInputCols(Array(dateParser.getDaysToCurrentCol))
        .setOutputCol("days_since_register_vec")*/

    val minMaxScaler = new MinMaxScaler()
      .setInputCol("days_since_register_vec")
      .setOutputCol("days_since_register_norm")

    //todo test
    /*val stages1 = mathCals ++ Array(loge_consumption_bucketizers,dateParser,
      dateParserVec,minMaxScaler)
    val pipeline1 = new Pipeline()
      .setStages(stages1)
    val splitDF1 = inputDF.randomSplit(Array(0.8,0.2))
    val trainDF1 = splitDF1(0)
    val testDF1 = splitDF1(1)
    val pipelineModel1 = pipeline1.fit(trainDF1)
    val testDFTransform1 = pipelineModel1.transform(testDF1)
    val trainDFtransformed1 = pipelineModel1.transform(trainDF1)
    trainDFtransformed1.show(5,false)
    val transformedDF1 = trainDFtransformed1.union(testDFTransform1)

    pipelineModel1.write.overwrite().save("F:\\03-ML\\01-model\\Vector")
    val sbc1 = SparkBundleContext().withDataset(transformedDF1)
    for (bf <- managed(BundleFile(s"jar:file:/03-ML/03-mleap/${System.currentTimeMillis()}_lr1.zip"))) {
      pipelineModel1.writeBundle.save(bf)(sbc1).get
    }
    return*/


    val stringIndexInputColumns = Array[String]("game_id", "province", "city", "channel", "os", "advertiser", "ad_pos", "scene_id", "material_id", "appname")
    val stringIndexOutputColumns = stringIndexInputColumns.map(col => s"si_$col")
    val stringIndexers = stringIndexInputColumns.zipWithIndex.map(colAndIdx => {
      new StringIndexer().setInputCol(colAndIdx._1).setOutputCol(stringIndexOutputColumns(colAndIdx._2))
        .setHandleInvalid("keep").setStringOrderType("frequencyDesc")
    })
    val onehotInputColumns = Array("si_game_id", "si_province", "si_city", "si_channel", "si_os", "si_advertiser", "si_ad_pos", "si_scene_id",
      "si_material_id", "si_appname")
    val onehotOutputColumns = onehotInputColumns.map(col => s"oh_$col")
    val onehots = new CusOneHotEncoderEstimator()
      .setInputCols(onehotInputColumns)
      .setOutputCols(onehotOutputColumns)
      .setDropLast(false)
      .setHandleInvalid("keep")


    val vectorAssemblerInputColumns = mathCalOutputColumns ++ loge_consumption_bucketizers.getOutputCols ++ Array(
      minMaxScaler.getOutputCol) ++ stringIndexOutputColumns ++ onehotOutputColumns
    //++onehotOutputColumns
    val intAssemblerInputColumns = loge_consumption_bucketizers.getOutputCols
    val vectorAssemblerInputColumnsDouble = stringIndexOutputColumns ++ mathCalOutputColumns ++ loge_consumption_bucketizers.getOutputCols
    val vectorAssemblerInputColumnsVector = Array(minMaxScaler.getOutputCol)

    val vectorAssembler = new CusVectorAssemblerEstimator()
      .setInputDoubleCols(intAssemblerInputColumns)
      .setInputVectorCols(onehotOutputColumns)
      .setOutputCol("features")

     /*val vectorAssembler = new VectorAssembler()
       .setInputCols(vectorAssemblerInputColumns)
       .setOutputCol("features")*/

    //    val colSelector = new ColSelector().setCols(vectorAssemblerInputColumns++Array("label"))
    val lr = new LogisticRegression()
      .setFeaturesCol("features")
      .setMaxIter(maxIters)
      .setElasticNetParam(0.5)
      .setLabelCol("label")
      .setRegParam(0.1)
      .setFitIntercept(true)
      .setProbabilityCol("probability")
    val stages = mathCals ++ Array(loge_consumption_bucketizers,
      dateParser, dateParserVec, minMaxScaler) ++ stringIndexers ++ Array(onehots, /*colSelector,*/ vectorAssembler, lr)
    val pipeline = new Pipeline()
      .setStages(stages)
    val splitDF = frame1.union(frame).randomSplit(Array(0.8, 0.2))
    val trainDF = splitDF(0)
    val testDF = splitDF(1)
    val pipelineModel = pipeline.fit(trainDF)
    val testDFTransform = pipelineModel.transform(testDF)
    val trainDFtransformed = pipelineModel.transform(trainDF)
    val transformedDF = trainDFtransformed.union(testDFTransform)
    //    transformedDF.write.parquet(outputPath)
    transformedDF.show(200, false)
    println("count##"+transformedDF.count())

    val scoreAndLabels = testDFTransform.select("probability", "label")
      .rdd.map(row => {
      (row.getAs[DenseVector](0).values(0), row.getInt(1).toDouble)
    })
    val binaryClassificationMetrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auc = binaryClassificationMetrics.areaUnderROC()
    println("####"+auc)
    val rdd = spark.sparkContext.parallelize[Row](Seq(Row(auc)))
    /*val metricDF = spark.createDataFrame(rdd,StructType(Seq(StructField("auc",DoubleType))))
    metricDF.repartition(1).write.parquet(metricPath)
    pipelineModel.write.save(modelPath)*/
    val localMleapPath = "/03-ML/03-mleap"
    /*   val localMleapPath = s"${System.getProperty("java.io.tmpdir")}/" +
         s"${HdfsUtils.getFileName(mleapPath)}_${System.currentTimeMillis}.zip"*/

    val sbc = SparkBundleContext().withDataset(transformedDF)
    for (bf <- managed(BundleFile(s"jar:file:$localMleapPath/${System.currentTimeMillis()}_lr1.zip"))) {
      pipelineModel.writeBundle.save(bf)(sbc).get
    }



    //    HdfsUtils.uploadModel(mleapPath, localMleapPath)

    // 删除临时文件
    //    new File(localMleapPath).delete()

    spark.close()
  }
}
