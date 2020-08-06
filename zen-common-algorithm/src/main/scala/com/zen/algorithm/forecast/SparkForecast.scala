package com.zen.algorithm.forecast

import java.io.File

import com.zen.algorithm.util.{ForecastUtil, HdfsUtils, Log}
import com.zen.spark.transformer.{ColSelector, MetaStorage, ModelType}
import ml.combust.bundle.BundleFile
import ml.combust.mleap.spark.SparkSupport._
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.feature.{IndexToString, StringIndexerModel}
import org.apache.spark.ml.{Pipeline, PipelineModel, Transformer}
import org.apache.spark.sql.{DataFrame, SparkSession}
import resource.managed

import scala.collection.mutable.ArrayBuffer

/**
 * @Author: xiongjun
 * @Date: 2020/6/16 12:14
 * @description spark预测类
 * @reviewer
 */
class SparkForecast(val spark: SparkSession, val fieldInfo: Map[String, Array[String]], override val paths: String*)
  extends Forecast[PipelineModel] with Log {
  var models: Array[PipelineModel] = load

  /**
   * 加载模型
   *
   * @return
   */
  override def load(): Array[PipelineModel] = {
    require(paths.nonEmpty, "paths can't be null")
    // 依次从本地加载模型
    val reader = PipelineModel.read
    paths.map(reader.load).toArray
  }

  /**
   * 把加载的 Spark PipelineModel 转成 MLeap 模型文件并保存到指定 HDFS 目录
   *
   * @param destPath
   * @return 是否转换成功并上传到 HDFS 指定路径
   */
  def convertSparkToBoundle(inputDataPath: String, destPath: String): Boolean = {
    require(models.nonEmpty, "please load spark model(s) first")
    require(inputDataPath.nonEmpty, "input data path is null")

    require(HdfsUtils.fileExists(inputDataPath), s"invalid input data path: $inputDataPath")
    val inputData = spark.read.load(inputDataPath)


    var transformers: ArrayBuffer[Transformer] = new ArrayBuffer[Transformer]()
    models.foreach(_.stages.foldLeft(transformers)((cur, t) => cur += t))
    transformers = transformers.filter(tr => !tr.isInstanceOf[ColSelector])
    val pipelineModel = new Pipeline().setStages(transformers.toArray).fit(inputData)

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

    val localMleapPath = s"${System.getProperty("java.io.tmpdir")}/" +
      s"${ForecastUtil.getFileName(destPath)}_${System.currentTimeMillis}.zip"
    val sbc = SparkBundleContext().withDataset(pipelineModel.transform(inputData))
    for (bf <- managed(BundleFile(s"jar:file:$localMleapPath"))) {
      pipelineModel.writeBundle.save(bf)(sbc).get
    }
    HdfsUtils.uploadModel(destPath, localMleapPath)
    require(HdfsUtils.fileExists(destPath), "MLEAP 模型文件上传到 HDFS 失败")

    // 删除临时文件
    if (!new File(localMleapPath).delete()) {
      log.warn(s"删除本地临时 MLEAP 模型文件失败: $localMleapPath")
    }
    log.info(s"已将 Spark Pipeline 模型转置成 MLEAP 模型文件格式：$destPath")
    true
  }

  override def predict(maps: Seq[Map[String, Any]]): Array[Map[String, Any]] = ???

  /**
   * 返回预测标签 topn 项和对应概率值
   *
   * @param prediction 已经预测好的原始 DataFrame
   * @param topn       as it indicates
   * @return
   */
  def computeProbability(prediction: DataFrame, topn: Int): DataFrame = ???

  /**
   * 用加载好的模型预测并返回预测概率值
   *
   * @param maps
   * @param topn
   * @return
   */
  override def predictProbabilityMaps(maps: Seq[Map[String, Any]], topn: Int, preModels: Array[Forecast[Any]]): Array[Map[String, Double]] = ???

  /**
   * 用加载好的回归模型预测并返回实数值
   *
   * @param maps
   * @return
   */
  override def getPredictionMaps(maps: Seq[Map[String, Any]]): Array[Double] = ???
}
