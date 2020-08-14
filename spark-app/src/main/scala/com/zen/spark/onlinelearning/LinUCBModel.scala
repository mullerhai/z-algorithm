package com.zen.spark.onlinelearning

import java.io.{File, FileOutputStream, ObjectOutputStream}
import java.util

import com.zen.bandit.cb.LinUCB
import com.zen.bandit.event.CbEvent
import com.zen.spark.transformer.{DateParser, TimestampParser}
import ml.combust.bundle.BundleFile
import ml.combust.mleap.spark.SparkSupport._
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import resource.managed

import scala.collection.JavaConversions

/**
 * @Author: xiongjun
 * @Date: 2020/7/30 15:39
 * @description
 * @reviewer
 */
object LinUCBModel {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("LinUCBModel").master("local[*]")
      .config("spark.sql.parquet.compression.codec", "uncompressed").getOrCreate()

    val inputPath = args(0)
    val outputPath = args(1)
    val metricPath = args(2)
    val modelPath = args(3)
    val mleapPath = args(4)
    val linUCBPath = args(5)
    val df = spark.read.parquet(inputPath)
    df.printSchema()

    /**
     * ['ad_id','connect_type','label','material_id','request_id','timestamp','city','device','province',
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
    val stringIndexInputColumns = Array[String]("city", "ad_id", "material_id", "device")
    val stringIndexOutputColumns = stringIndexInputColumns.map(col => s"si_$col")
    val stringIndexers = stringIndexInputColumns.zipWithIndex.map(colAndIdx => {
      new StringIndexer().setInputCol(colAndIdx._1).setOutputCol(stringIndexOutputColumns(colAndIdx._2))
        .setHandleInvalid("keep").setStringOrderType("frequencyDesc")
    })

    val onehotInputColumns = Array("si_city", "si_ad_id", "si_material_id", "si_device")
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
    //    val columnMerge = new ColumnMerge().setInputCols(Array("ad_id","material_id")).setOutputCol("ad_material_id").setDelimiter("_")
    val cusVectorAssembler = new CusVectorAssemblerEstimator()
      .setInputDoubleCols(mathCalOutputColumns ++ binaryOutputColumns /*++bucketizerExtendEstimator.getOutputCols*/).setInputVectorCols(
      onehotOutputColumns ++ directOneHotOutputColumns)
      .setOutputCol("features")
    val pipeline = new Pipeline().setStages(Array(registeDateParser) ++ binarizers ++ mathCals ++ Array(/*columnMerge,*/ curTimeParser)
      ++ stringIndexers ++ Array(onehots, dirOnehots, cusVectorAssembler))
    val pipelineModel = pipeline.fit(df)
    val transformedDF = pipelineModel.transform(df)
    val featureDim = transformedDF.select("features").head().get(0).asInstanceOf[Vector].size
    val adMaterialIdSet = new util.HashSet[String]()
    transformedDF.select("ad_id", "material_id").rdd.map(row => {
      row.getString(0) + "_" + row.getString(1)
    }).distinct().collect().foreach(adMaterialIdSet.add)
    transformedDF.write.parquet(outputPath)
    val trainDF = transformedDF.select("timestamp", "request_id", "ad_id", "material_id", "features", "label").filter(col("timestamp") <= 1596297599)
    val testDF = transformedDF.select("timestamp", "request_id", "ad_id", "material_id", "features", "label").filter(col("timestamp") > 1596297599)
    val event_func = (row: Row) => {
      val request_id = row.getString(1)
      val ad_id = row.getString(2)
      val material_id = row.getString(3)
      val features = row.get(4).asInstanceOf[Vector]
      val label = row.getInt(5).toDouble
      val event = new CbEvent(request_id, ad_id, material_id, new ArrayRealVector(features.toArray))
      event.setReward(label)
      event
    }
    val trainEvents = trainDF.rdd.map(event_func).collect()
    val testEvents = testDF.rdd.map(event_func).collect()
    //    val events = transformedDF.select("timestamp","request_id","ad_id","material_id","features","label").sort(col("timestamp")).rdd.collect()
    //        .map(row=>{
    //          val request_id = row.getString(0)
    //          val ad_id = row.getString(1)
    //          val material_id = row.getString(2)
    //          val features = row.get(3).asInstanceOf[Vector]
    //          val label = row.getDouble(4)
    //          val event = new CbEvent(request_id,ad_id,material_id,new ArrayRealVector(features.toArray))
    //          event.setReward(label)
    //          event
    //        })
//    Range(0,10).map(alpha=>{
//      val linUCB = new LinUCB(featureDim, adMaterialIdSet, alpha.toDouble/10.0)
//      linUCB.close()
//      linUCB.receiveRewards(JavaConversions.seqAsJavaList(trainEvents))
//      val mse = evaluator(linUCB, testEvents)
//      (alpha.toDouble/10.0,mse)
//    }).foreach(t=>println(s"alpha:${t._1},mse:${t._2}"))

    val linUCB = new LinUCB(featureDim, adMaterialIdSet, 0.1)
    linUCB.close()
    linUCB.receiveRewards(JavaConversions.seqAsJavaList(trainEvents))
    val mse = evaluator(linUCB, testEvents)
    println(s"mse:$mse")
    val rdd = spark.sparkContext.parallelize[Row](Seq(Row(mse)))
    val metricDF = spark.createDataFrame(rdd, StructType(Seq(StructField("mse", DoubleType))))
    metricDF.repartition(1).write.parquet(metricPath)
    pipelineModel.write.save(modelPath)

    val sbc = SparkBundleContext().withDataset(transformedDF)
    for (bf <- managed(BundleFile(s"jar:file:$mleapPath"))) {
      pipelineModel.writeBundle.save(bf)(sbc).get
    }
    val fos = new FileOutputStream(linUCBPath)
    val objectOutputStream=new ObjectOutputStream(fos)
    objectOutputStream.writeObject(linUCB)
    objectOutputStream.close()

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

  def evaluator(linUCB: LinUCB, testEvents: Array[CbEvent]): Double = {
    val rewards = linUCB.computeReward(JavaConversions.seqAsJavaList(testEvents))
    val label = testEvents.map(_.getReward)
    label.zip(rewards).map(labelAndReward => Math.pow(labelAndReward._1 - labelAndReward._2, 2)).sum / label.length
  }
}
