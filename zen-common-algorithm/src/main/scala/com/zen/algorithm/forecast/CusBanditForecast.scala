package com.zen.algorithm.forecast

import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import com.zen.bandit.cb.LinUCB
import com.zen.bandit.event.CbEvent
import com.zen.spark.transformer.FieldInfo
import org.apache.commons.math3.linear.{ArrayRealVector, RealVector}
import org.apache.spark.ml.linalg.Vector

import scala.collection.JavaConversions
import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

/**
 * @Author: xiongjun
 * @Date: 2020/7/28 10:32
 * @description
 * @reviewer
 */
class CusBanditForecast(val fieldInfo: Map[String, Array[String]], override val paths: String*) extends Forecast[LinUCB] {
  val transform: Array[LinUCB] = load()
  def setAlpha(value:Double)= transform.head.setAlpha(value)

  /**
   * 加载模型
   *
   * @return
   */
  override def load(): Array[LinUCB] = {
    paths.map(path => {

      val fos = new FileInputStream(path)

      val stream = new ObjectInputStream(fos)
      val linUCB =stream.readObject().asInstanceOf[LinUCB]
      linUCB.init()
      stream.close()
      linUCB
    }).toArray
  }

  def save(path:String) = {
    val file = new File(path)
    val fileParent = file.getParentFile
    if (!fileParent.exists) {
      fileParent.mkdirs
      file.createNewFile
    }
    val fos = new FileOutputStream(path)
    val objectOutputStream=new ObjectOutputStream(fos)
    objectOutputStream.writeObject(transform.head)
    objectOutputStream.close()
  }

  override def predict(maps: Seq[Map[String, Any]]): Array[Map[String, Any]] = {
    predictProbabilitiesWithBandit(maps, 5, null).map(kv => kv.map(f => f._1 -> f._2.asInstanceOf[Any]))
  }

  def receiveReward(requestId: String, adId: String, materialId: String, reward: Double): Unit = {
    transform.head.replaceReward(requestId, adId, materialId, reward)
  }


  /**
   * 用加载好的模型预测并返回预测概率值
   *
   * @param maps
   * @param topn
   * @return
   */
  override def predictProbabilityMaps(maps: Seq[Map[String, Any]], topn: Int, preModels: Array[Forecast[Any]])
  : Array[Map[String, Double]] = predictProbabilitiesWithBandit(maps, topn, preModels)


  def predictProbabilitiesWithBandit(maps: Seq[Map[String, Any]], topn: Int, preModels: Array[Forecast[Any]]): Array[Map[String, Double]] = {
    val singletonStart = System.currentTimeMillis()
    var dataMapsVar = maps
    //feature transform for the RealVector
    if (preModels.nonEmpty) {
      for (model <- preModels) {
        dataMapsVar = model.predict(dataMapsVar)
      }
    }
    //get the event object
    val events = dataMapsVar.map(mapToEvent).toArray
    val eventList = scala.collection.JavaConversions.seqAsJavaList(events)
    val list = transform.head.predict(eventList)
    //build return
    val result = JavaConversions.asScalaBuffer(
      list.map(
        JavaConversions.mapAsScalaMap).map(_.map(kv =>kv._1 ->kv._2.toDouble).toMap)).toArray
    val singletonEnd = System.currentTimeMillis()
    val time = singletonEnd - singletonStart
    logInfo(s"predict total time: $time  ms")
    result
  }

  /**
   * 用加载好的回归模型预测并返回实数值
   *
   * @param maps
   * @return
   */
  override def getPredictionMaps(maps: Seq[Map[String, Any]]): Array[Double] = ???


  def vector2RealVector(dataMap: Map[String, Any], kv: (String, Array[String])): RealVector = {

    val value = dataMap.getOrElse(kv._1, null)
    if (value == null) {
      val strs = fieldInfo.get(kv._1).asInstanceOf[String]
      println(strs)
      val ds = ArrayBuffer[Double]()
      strs.split(",").foreach(str => {
        println(str.toDouble)
        ds += str.toDouble
      })
      new ArrayRealVector(ds.toArray)
    } else {
      new ArrayRealVector(value.asInstanceOf[ml.combust.mleap.tensor.SparseTensor[Double]].toArray)
    }
  }


  def str2String(dataMap: Map[String, Any], kv: (String, Array[String])): String = {
    val value = dataMap.getOrElse(kv._1, null)
    if (value == null) {
      fieldInfo(kv._1)(1)
    } else {
      value.toString
    }

  }

  def mapToEvent(dataMap: Map[String, Any]): CbEvent = {
    val event = new CbEvent()
    val map = fieldInfo.map(kv => {
      val value = kv._2.head match {
        case FieldInfo.VECTOR =>
          event.setContext(vector2RealVector(dataMap, kv))
        case FieldInfo.STRING =>
          str2String(dataMap, kv)
        case _ =>
          throw new Exception(s"${kv._1} field info error,please check field info")
      }
      kv._1 -> value
    })
    event.setProperty(
      map("request_id").toString,
      map("ad_id").toString,
      map("material_id").toString)
    event
  }

}
