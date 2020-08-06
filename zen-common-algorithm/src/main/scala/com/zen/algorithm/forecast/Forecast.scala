package com.zen.algorithm.forecast

import java.{lang, util}

import com.zen.algorithm.util.Log

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import scala.collection.{JavaConversions, mutable}
/**
 * @Author: xiongjun
 * @Date: 2020/6/8 10:16
 * @description 实时预测trait，任意类型的实时预测模型都要继承Forecast，A表示模型的类型，如mleap，spark，tensorflow
 * @reviewer
 */
trait Forecast[A] extends Log {
  val paths: Seq[String]
  val fieldInfo: Map[String, Array[String]]

  /**
   * 隐式转换
   *
   * @param map
   * @return
   */
  protected implicit def toMap(map: mutable.Map[String, Any]): Map[String, Any] = map.toMap

  protected implicit def toMaps(maps: Seq[mutable.Map[String, Any]]): Seq[Map[String, Any]] = maps.map(_.toMap)

  /**
   * 加载模型
   *
   * @return
   */
  def load(): Array[A]

  def predict(maps: Seq[Map[String, Any]]): Array[Map[String, Any]]

  /**
   * 预测单条样本
   *
   * @param javaList
   * @return
   */
  def predictJavaList(javaList: util.List[util.Map[String, Any]]): util.List[util.Map[String, Any]] = {
    val scalaRes = predict(JavaConversions.asScalaBuffer(javaList.map(JavaConversions.mapAsScalaMap)).toSeq)
    scalaRes.map(JavaConversions.mapAsJavaMap(_)).toList
  }

  def predictJavaMap(javaMap: util.Map[String, util.Map[String, Any]]): util.Map[String, util.Map[String, Any]] = {
    val keys = new ArrayBuffer[String](javaMap.size())
    val maps = new ArrayBuffer[util.Map[String, Any]](javaMap.size())

    javaMap.entrySet().foreach(e => {
      keys += e.getKey
      maps += JavaConversions.mapAsScalaMap(e.getValue)
    })

    val resList = predictJavaList(maps)
    var resMap = Map[String, util.Map[String, Any]]()

    for (i <- resList.indices) {
      resMap += (keys(i) -> resList(i))
    }

    JavaConversions.mapAsJavaMap(resMap)
  }

  /**
   * 用加载好的模型预测并返回预测概率值
   *
   * @param maps
   * @param topn
   * @return
   */
  def predictProbabilityMaps(maps: Seq[Map[String, Any]], topn: Int, preModels: Array[Forecast[Any]]): Array[Map[String, Double]]

  /**
   * 用加载好的回归模型预测并返回实数值
   *
   * @param maps
   * @return
   */
  def getPredictionMaps(maps: Seq[Map[String, Any]]): Array[Double]

  /**
   * 提供给外部 Java 程序调用的预测方法，接收 Java 类型参数，并将预测结果转成 Java 类型
   *
   * @param javaMaps
   * @return
   */
  def predictProbabilityJavaList(javaMaps: util.List[util.Map[String, Any]], topn: Int, preModels: Array[Forecast[Any]]): util.List[util.Map[lang.String, lang.Double]] = {
    val scalaRes = predictProbabilityMaps(
      JavaConversions.asScalaBuffer(
        javaMaps.map(
          JavaConversions.mapAsScalaMap)).toSeq, topn, preModels)
    val result = new util.ArrayList[util.HashMap[lang.String, lang.Double]]()
    scalaRes.foreach(kvMap=>{
      val map = new util.HashMap[lang.String,lang.Double]()
      kvMap.foreach(kv=>map.put(kv._1,kv._2))
      result.add(map)
    })
    result.asInstanceOf[util.List[util.Map[lang.String, lang.Double]]]
  }

  def predictProbabilityJavaMap(javaMap: util.Map[String, util.Map[String, Any]], topn: Int = 0, preModels: Array[Forecast[Any]]):
  util.Map[String, util.Map[lang.String, lang.Double]] = {
    val keys = new ArrayBuffer[String](javaMap.size())
    val maps = new ArrayBuffer[Map[String, Any]](javaMap.size())

    javaMap.entrySet().foreach(e => {
      keys += e.getKey
      maps += JavaConversions.mapAsScalaMap(e.getValue)
    })

    val scalaResList = predictProbabilityMaps(maps, topn, preModels)

    val result = new util.HashMap[lang.String,util.HashMap[lang.String,lang.Double]]()
    scalaResList.zipWithIndex.foreach(mapAndIdx=>{
      val map = new util.HashMap[lang.String,lang.Double]()
      mapAndIdx._1.foreach(kv=>map.put(kv._1,kv._2))
      result.put(keys(mapAndIdx._2),map)
    })
    result.asInstanceOf[util.Map[lang.String, util.Map[lang.String, lang.Double]]]
  }


  def getPredictionJavaList(javaMaps: util.List[util.Map[String, Any]]): util.List[lang.Double] = {
    val scalaRes = getPredictionMaps(
      JavaConversions.asScalaBuffer(
        javaMaps.map(
          JavaConversions.mapAsScalaMap)).toSeq)

    scalaRes.toList.map(r => lang.Double.valueOf(r))
  }

  def warmup(javaMaps: util.List[util.Map[String, Any]]): util.List[util.Map[lang.String, lang.String]] = {
    val scalaRes = predict(
      JavaConversions.asScalaBuffer(
        javaMaps.map(
          JavaConversions.mapAsScalaMap)).toSeq)

    JavaConversions.seqAsJavaList(
      scalaRes.map(r => {
        JavaConversions.mapAsJavaMap(r.map(m => {
          (m._1, m._2.toString)
        }))
      }))
  }

  def intermediateResult(javaMaps: util.List[util.Map[String, Any]]): util.List[util.Map[lang.String, lang.Object]] = {
    val scalaRes = predict(
      JavaConversions.asScalaBuffer(
        javaMaps.map(
          JavaConversions.mapAsScalaMap)).toSeq)
    val result = JavaConversions.seqAsJavaList(
      scalaRes.map(r => {
        JavaConversions.mapAsJavaMap(r.map(m => {
          if (m._2.isInstanceOf[Stream[Any]]) {
            (m._1, m._2.asInstanceOf[Stream[Any]].toArray)
          } else {
            (m._1, m._2.asInstanceOf[Object])
          }
        }))
      }))
    result
  }

}

object Forecast {
  final val LABEL2PROBABILITY_COL = "label2Probability"
  final val PROBABILITY_REG = "^probability\\((.*)\\)$".r
  final val MODEL_TYPE_SPARK = "spark"
  final val MODEL_TYPE_PMML = "pmml" //暂时不用
  final val MODEL_TYPE_MLEAP = "mleap"
  final val MODEL_TYPE_TF = "tensorflow"
  final val SUPPORTED_MODEL_TYPES = Array(MODEL_TYPE_SPARK, MODEL_TYPE_PMML, MODEL_TYPE_MLEAP, MODEL_TYPE_TF)
}
