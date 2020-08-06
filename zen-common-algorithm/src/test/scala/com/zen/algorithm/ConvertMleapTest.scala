package com.zen.algorithm

import com.zen.spark.transformer.{ColSelector, MetaStorage, ModelType}
import ml.combust.bundle.BundleFile
import ml.combust.mleap.spark.SparkSupport._
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.feature.{IndexToString, StringIndexerModel}
import org.apache.spark.ml.{Pipeline, PipelineModel, Transformer}
import org.apache.spark.sql.SparkSession
import resource.managed

import scala.collection.mutable.ArrayBuffer

/**
 * @Author: xiongjun
 * @Date: 2020/6/17 9:45
 * @description
 * @reviewer
 */
object ConvertMleapTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").appName("ConvertMleapTest").getOrCreate()
    //    val inputDataPath = "F:\\03-ML\\05-train-data\\02-TestData\\nor.csv"
    //    val inputData = spark.read.parquet("F:\\03-ML\\05-train-data\\02-TestData\\split")
    //    val inputData = spark.read.parquet("F:\\03-ML\\05-train-data\\03-2020-6-19\\lrData")
    //    val inputData = spark.read.parquet("F:\\03-ML\\05-train-data\\02-TestData\\nor1592554196795")
    //    val inputData = spark.read.parquet("F:\\03-ML\\05-train-data\\02-TestData\\Vector")
    val inputData = spark.read.parquet("F:\\03-ML\\05-train-data\\02-TestData\\ColumnMerge")


    val modelPaths: Array[String] = Array[String]("F:\\03-ML\\01-model\\ColumnMerge")
    //    val modelPaths: Array[String] = Array[String]("F:\\03-ML\\01-model\\StandAndNor")
    //    val modelPaths: Array[String] = Array[String]("F:\\03-ML\\01-model\\Bucketizer")

    val reader = PipelineModel.read
    val models = modelPaths.map(reader.load).toArray
    var transformers: ArrayBuffer[Transformer] = new ArrayBuffer[Transformer]()
    models.foreach(_.stages.foldLeft(transformers)((cur, t) => cur += t))
    transformers = transformers.filter(tr => !tr.isInstanceOf[ColSelector])
    val pipelineModel = new Pipeline().setStages(transformers.toArray).fit(inputData)
    val frame = pipelineModel.transform(inputData)
    frame.show()
    var labels = Array.empty[String]
    var fieldName = ""
    var isMlComponent = false
    val mlComponents = Seq(ModelType.Algorithm_Classification_Prob, ModelType.Algorithm_Regression, ModelType.Clustering)
    for (transformer <- pipelineModel.stages) {
      transformer match {
        case meta: MetaStorage =>
          if (mlComponents.contains(meta.getModelType)) {
            isMlComponent = true
            fieldName = transformer.asInstanceOf[MetaStorage].getParameters("labelCol").toString
          }
        case sim: StringIndexerModel =>
          if (isMlComponent) {
            labels = sim.labels
          }
        case its: IndexToString =>
          if (isMlComponent && labels.length > 0) {
            its.setLabels(labels)
            isMlComponent = false
          }
        case _ =>
      }
    }
    val destPath = "/03-ML/03-mleap"

    /*   val transformer = (for (bf <- managed(BundleFile(s"jar:file:${destPath}"))) yield {
         bf.loadMleapBundle().get.root
       }).tried.get*/

    val sbc = SparkBundleContext().withDataset(pipelineModel.transform(inputData))
    for (bf <- managed(BundleFile(s"jar:file:$destPath/${System.currentTimeMillis()}_data.zip"))) {
      pipelineModel.writeBundle.save(bf)(sbc).get
    }
    //    HdfsUtils.uploadModel(destPath, localMleapPath)
  }
}
