package com.zen.algorithm.forecast

import com.google.protobuf.ByteString
import com.zen.algorithm.util.ForecastUtil
import com.zen.spark.transformer.FieldInfo
import com.zen.tensorflow.example.{BytesList, Example, Feature, Features, FloatList, Int64List}
import com.zen.tensorflow.framework.{DataType, TensorProto, TensorShapeProto}
import tensorflow.serving.Predict
import zen.common.tensorflow.wapper.TFGrpcPredict
import scala.collection.JavaConversions

/**
 * @Author: xiongjun
 * @Date: 2020/6/28 14:58
 * @description
 * @reviewer
 */
class TensorflowForecast(val hosts: Array[String], val port: Int, val modelName: String, val version: Int
                         ,override val fieldInfo: Map[String, Array[String]],override val paths: Seq[String] = null)
  extends Forecast[TFGrpcPredict]{

  val grpcPredict: TFGrpcPredict = load().head

  def str2feature(dataMap:Map[String,Any],field:(String,Array[String])): Feature = {
    val value = dataMap.getOrElse(field._1, null)
    val listBuilder = BytesList.newBuilder
    val featureBuilder = Feature.newBuilder
    if (value != null) {
      val bytesList = listBuilder.addValue(ByteString.copyFromUtf8(value.toString)).build
      featureBuilder.setBytesList(bytesList).build
    } else {
      val bytesList = listBuilder.addValue(ByteString.copyFromUtf8(field._2.last)).build
      featureBuilder.setBytesList(bytesList).build
    }
  }
  def double2feature(dataMap:Map[String,Any],field:(String,Array[String])): Feature = {
    val value = dataMap.getOrElse(field._1, null)
    val listBuilder = FloatList.newBuilder
    val featureBuilder = Feature.newBuilder
    if (value != null) {
      val floatList = listBuilder.addValue(value.toString.toFloat).build
      featureBuilder.setFloatList(floatList).build
    } else {
      val floatList = listBuilder.addValue(field._2.last.toFloat).build
      featureBuilder.setFloatList(floatList).build
    }
  }
  def seq2feature(dataMap:Map[String,Any],field:(String,Array[String])): Feature = {
    val value = dataMap.getOrElse(field._1, null)
    val listBuilder = BytesList.newBuilder
    val featureBuilder = Feature.newBuilder
    if (value != null) {
      val valueSeq = value.toString.split(",")
      valueSeq.foreach(v => {
        listBuilder.addValue(ByteString.copyFromUtf8(v))
      })
      val bytesList = listBuilder.build()
      featureBuilder.setBytesList(bytesList).build
    } else {
      val values = field._2.last
      val valueSeq = values.split(",")
      valueSeq.foreach(v => {
        listBuilder.addValue(ByteString.copyFromUtf8(v))
      })
      val bytesList = listBuilder.build()
      featureBuilder.setBytesList(bytesList).build
    }
  }
  def seqint2feature(dataMap:Map[String,Any],field:(String,Array[String])): Feature = {
    val value = dataMap.getOrElse(field._1, null)
    val listBuilder = Int64List.newBuilder
    val featureBuilder = Feature.newBuilder
    if (value != null) {
      val valueSeq = value.toString.split(",")
      valueSeq.foreach(v => {
        listBuilder.addValue(v.toInt)
      })
      val intList = listBuilder.build()
      featureBuilder.setInt64List(intList).build
    } else {
      val values = field._2.last
      val valueSeq = values.split(",")
      valueSeq.foreach(v => {
        listBuilder.addValue(v.toInt)
      })
      val intList = listBuilder.build()
      featureBuilder.setInt64List(intList).build
    }
  }
  def seqfloat2feature(dataMap:Map[String,Any],field:(String,Array[String])): Feature = {
    val value = dataMap.getOrElse(field._1, null)
    val listBuilder = FloatList.newBuilder
    val featureBuilder = Feature.newBuilder
    if (value != null) {
      val valueSeq = value.toString.split(",")
      valueSeq.foreach(v => {
        listBuilder.addValue(v.toFloat)
      })
      val floatList = listBuilder.build()
      featureBuilder.setFloatList(floatList).build
    } else {
      val values = field._2.last
      val valueSeq = values.split(",")
      valueSeq.foreach(v => {
        listBuilder.addValue(v.toFloat)
      })
      val floatList = listBuilder.build()
      featureBuilder.setFloatList(floatList).build
    }
  }

  def arr2feature(dataMap:Map[String,Any],field:(String,Array[String])): Feature = {
    var value = dataMap.getOrElse(field._1, null)
    val listBuilder = BytesList.newBuilder
    val featureBuilder = Feature.newBuilder
    if (value != null) {
      if (value.isInstanceOf[String]) {
        value = value.toString.split(",").foreach(v => {
          listBuilder.addValue(ByteString.copyFromUtf8(v))
        })
        val bytesList = listBuilder.build()
        featureBuilder.setBytesList(bytesList).build
      } else {
        val valueSeq = value.asInstanceOf[scala.collection.AbstractSeq[String]]
        valueSeq.foreach(v => {
          listBuilder.addValue(ByteString.copyFromUtf8(v))
        })
        val bytesList = listBuilder.build()
        featureBuilder.setBytesList(bytesList).build
      }
    } else {
      val values = field._2.last
      val valueSeq = values.split(",")
      valueSeq.foreach(v => {
        listBuilder.addValue(ByteString.copyFromUtf8(v))
      })
      val bytesList = listBuilder.build()
      featureBuilder.setBytesList(bytesList).build
    }
  }
  def int2feature(dataMap:Map[String,Any],field:(String,Array[String])): Feature = {
    val value = dataMap.getOrElse(field._1, null)
    val listBuilder = Int64List.newBuilder
    val featureBuilder = Feature.newBuilder
    if (value != null) {
      val int64List = listBuilder.addValue(value.toString.toInt).build
      featureBuilder.setInt64List(int64List).build
    } else {
      val int64List = listBuilder.addValue(field._2.last.toInt).build
      featureBuilder.setInt64List(int64List).build
    }
  }
  def mapToFeatureMap(dataMap: Map[String, Any]): Map[String, Feature] = {
    fieldInfo.map(kv => {
      val feature = kv._2.head match {
        case FieldInfo.STRING =>
          str2feature(dataMap,kv)
        case FieldInfo.DOUBLE =>
          double2feature(dataMap,kv)
        case FieldInfo.INT =>
          arr2feature(dataMap,kv)
        case FieldInfo.SEQUENCE =>
          seq2feature(dataMap,kv)
        case FieldInfo.SEQUENCE_INT =>
          seqint2feature(dataMap,kv)
        case FieldInfo.SEQUENCE_FLOAT =>
          seqfloat2feature(dataMap,kv)
        case FieldInfo.ARRAY =>
          arr2feature(dataMap,kv)
        case _ =>
          throw new Exception(s"${kv._1} field info error,please check $modelName $version's field info")
      }
      kv._1 -> feature
    })
  }

  def createExample(data: Map[String, Any]): Example = {
    val featureMap = mapToFeatureMap(data)
    val featuresBuilder = Features.newBuilder()
    val features = featuresBuilder.putAllFeature(JavaConversions.mapAsJavaMap(
      featureMap.map(r => {
        (r._1, r._2)
      }))).build()
    Example.newBuilder().setFeatures(features).build()
  }

  def predictProbabilitiesWithTF(dataMaps: Seq[Map[String, Any]], topn: Int, preModels: Array[Forecast[Any]]): Array[Map[String, Double]] = {
    val singletonStart = System.currentTimeMillis()
    var dataMapsVar = dataMaps
    if (preModels.nonEmpty) {
      for (forecast <- preModels) {
        dataMapsVar = forecast.predict(dataMapsVar)
      }
    }
    //单线程
    val exampleByteList = dataMapsVar.map(data => createExample(data).toByteString)

    val exampleListJava = JavaConversions.seqAsJavaList(exampleByteList)
    val featureDim = TensorShapeProto.Dim.newBuilder().setSize(exampleListJava.size()).build()
    val shapeProto = TensorShapeProto.newBuilder().addDim(featureDim).build()
    val tensorProto = TensorProto.newBuilder
      .addAllStringVal(exampleListJava)
      .setDtype(DataType.DT_STRING)
      .setTensorShape(shapeProto).build
    val request = Predict.PredictRequest.newBuilder()
      .setModelSpec(grpcPredict.getModelSpec)
      .putInputs("inputs", tensorProto)
      .build
    val response = grpcPredict.predict(request)
    val result = if (response != null) {
      val outMap = response.getOutputsMap
      val classTensor = outMap.get("classes")
      val labelNum = classTensor.getTensorShape.getDim(1).getSize.toInt
      val bytesList = if(classTensor.getStringValList==null||classTensor.getStringValList.size()==0){
        JavaConversions.seqAsJavaList(dataMaps.indices.flatMap(_ => {
          Range(0, labelNum).map(i => ByteString.copyFromUtf8(i + ""))
        }))
      }else{
        classTensor.getStringValList
      }
      val scoresTensor = outMap.get("scores")
      val floatList = scoresTensor.getFloatValList
      val bytesListScala = JavaConversions.asScalaBuffer(bytesList)
      val bytesListSeg = bytesListScala.sliding(labelNum, labelNum)

      val floatListScala = JavaConversions.asScalaBuffer(floatList)
      val floatListSeg = floatListScala.sliding(labelNum, labelNum)
      val resMaps = bytesListSeg.zip(floatListSeg).map(f => {
        var resMap = f._1.zip(f._2).map(bf => {
          bf._1.toStringUtf8 -> bf._2.toDouble
        }).toMap
        resMap = ForecastUtil.sortMapValAndTakeTopn(resMap, topn)
        resMap.map(r => r._1 -> r._2.formatted("%.8f").toDouble)
      }).toArray
      resMaps
    } else {
      Array[Map[String, Double]]()
    }
    val SinglePredictTime = System.currentTimeMillis() - singletonStart
    logInfo(s"${hosts.head}:$port predict total time: $SinglePredictTime ms")
    result
  }

  /**
   * 加载模型
   *
   * @return
   */
  override def load(): Array[TFGrpcPredict] = Array(new TFGrpcPredict(hosts, port, modelName, version))

  override def predict(maps: Seq[Map[String, Any]]): Array[Map[String, Any]] = {
    predictProbabilitiesWithTF(maps, 5, null).map(kv => kv.map(f => f._1 -> f._2.asInstanceOf[Any]))
  }

  /**
   * 用加载好的模型预测并返回预测概率值
   *
   * @param maps
   * @param topn
   * @return
   */
  override def predictProbabilityMaps(maps: Seq[Map[String, Any]], topn: Int, preModels: Array[Forecast[Any]])
  : Array[Map[String, Double]] = predictProbabilitiesWithTF(maps,topn,preModels)

  /**
   * 用加载好的回归模型预测并返回实数值
   *
   * @param maps
   * @return
   */
  override def getPredictionMaps(maps: Seq[Map[String, Any]]): Array[Double] = ???
}
