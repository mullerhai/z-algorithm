package com.zen.algorithm.forecast

import java.util.concurrent._

import com.zen.algorithm.util.{ForecastUtil, Log}
import com.zen.mleap.model.MetaStorageModel
import com.zen.mleap.transformer.MetaStorage
import com.zen.spark.transformer.{FieldInfo, ModelType}
import ml.combust.bundle.BundleFile
import ml.combust.mleap.core
import ml.combust.mleap.core.types.{BasicType, ScalarType, StructField, StructType}
import ml.combust.mleap.runtime.MleapSupport._
import ml.combust.mleap.runtime.frame.{ArrayRow, DefaultLeapFrame, Transformer}
import ml.combust.mleap.runtime.transformer.PipelineModel
import ml.combust.mleap.runtime.transformer.feature.StringIndexer
import ml.combust.mleap.tensor.DenseTensor
import resource.managed

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/8 10:27
 * @description
 * @reviewer
 */
class MleapForecast(val threadNum: Int, val fieldInfo: Map[String, Array[String]], override val paths: String*) extends Forecast[Transformer] {

  /**
   * 禁止使用jdk提供的工具类
   */
  //  val threadPool: ExecutorService = new ThreadPoolExecutor(threadNum, threadNum,
  //    0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue[Runnable](1000))

  val mleapTransformer: Array[Transformer] = load
  val mleapStructType = constructMleapStructFromFieldInfo
  val probabilityCol = findProbabilityCol
  val predictionCol = findPredictionCol
  val labelNameAndvalue = completionLabelAndValue

  def findProbabilityCol: String = {
    val transformers = mleapTransformer.head.model.
      asInstanceOf[PipelineModel]
      .transformers
    val metaStorageModels = transformers.filter(_.isInstanceOf[MetaStorage])
    if (metaStorageModels.nonEmpty) {
      metaStorageModels.last.model.asInstanceOf[MetaStorageModel].params.getOrElse("probabilityCol", "probability").toString
    } else {
      "probability"
    }
  }

  def findPredictionCol: String = {
    val transformers = mleapTransformer.head.model.
      asInstanceOf[PipelineModel]
      .transformers
    val metaStorageModels = transformers.filter(_.isInstanceOf[MetaStorage])
    if (metaStorageModels.nonEmpty) {
      metaStorageModels.last.model.asInstanceOf[MetaStorageModel].params.getOrElse("predictionCol", "prediction").toString
    } else {
      "prediction"
    }
  }

  def constructMleapStructFromFieldInfo: Try[core.types.StructType] = {
    val structFields = new ArrayBuffer[ml.combust.mleap.core.types.StructField]()
    if (fieldInfo != null && fieldInfo.nonEmpty) {
      val pipelineModel = mleapTransformer.head.model.asInstanceOf[PipelineModel]

      pipelineModel.transformers.head match {
        case storage: MetaStorage =>
          val diff = storage.model.fields.filter(!fieldInfo.contains(_))
          require(fieldInfo.size == storage.model.fields.length &&
            diff.length > 0, s"upload fieldInfo ${diff.mkString(",", "[", "]")} with trainSet fieldInfo unmatch")
        case _ =>
      }
      // 根据fieldInfo来构建StructType
      fieldInfo.map(kv => {
        kv._2.head match {
          case FieldInfo.INT =>
            structFields += StructField(kv._1, ScalarType.Int)
          case FieldInfo.DOUBLE =>
            structFields += StructField(kv._1, ScalarType.Double)
          case FieldInfo.STRING | FieldInfo.SEQUENCE | FieldInfo.SEQUENCE_INT | FieldInfo.SEQUENCE_FLOAT =>
            structFields += StructField(kv._1, ScalarType.String)
          case FieldInfo.LONG =>
            structFields += StructField(kv._1, ScalarType.Long)
          case _ =>
            throw new Exception(s"system got data type is ${kv._2.head} ,but only accept int/double/string/sequence")
        }
      })
    }
    StructType(structFields)
  }

  /**
   * 仅供mleap模型补全label使用
   *
   * @return key为label的fieldName
   */
  def completionLabelAndValue: (String, Array[String]) = {
    var labels = Array.empty[String]
    val pipelineModel = mleapTransformer.head.model.asInstanceOf[PipelineModel]
    var labelName = ""
    var isMlComponent = false
    val mlComponents = Seq(ModelType.Algorithm_Classification_Prob, ModelType.Algorithm_Regression, ModelType.Clustering)
    for (transformer <- pipelineModel.transformers) {
      transformer match {
        case meta: MetaStorage =>
          if (mlComponents.contains(meta.model.modelType)) {
            isMlComponent = true
            labelName = meta.model.params("labelCol").toString
          }
        case si: StringIndexer =>
          if (isMlComponent) {
            labels = si.model.labels.toArray
          }
        case _ =>
      }
    }
    //    if(labelName.nonEmpty&&labels.nonEmpty){
    //      (labelName, labels)
    //    }else{
    //      ("label",Array("0","1"))
    //    }
    (labelName, labels)
  }

  /**
   * 根据 StructType 字段属性信息，把 Map 转成 mleap Row,并对数据进行补全
   *
   * @param dataMap
   * @return
   */
  private[algorithm] def mapToMleapRow(dataMap: Map[String, Any]):
  ml.combust.mleap.runtime.frame.Row = {
    val values = new ArrayBuffer[Any]()
    // 判断 value 类型，并以此构建 Row
    mleapStructType.get.fields.foreach(f => {
      val v = if (dataMap.contains(f.name)) {
        if (dataMap(f.name) == null || !ForecastUtil.isNotNull(dataMap(f.name).toString)) {
          if (fieldInfo(f.name).last == "") {
            throw new Exception("无默认填充值的字段不能为空")
          } else {
            fieldInfo(f.name).last
          }
        } else {
          dataMap(f.name)
        }
      } else {
        if (fieldInfo(f.name).last == "") {
          throw new Exception("无默认填充值的字段不能为空")
        } else {
          fieldInfo(f.name).last
        }
      }
      fieldInfo.getOrElse(f.name, null).head match {
        case FieldInfo.STRING | FieldInfo.SEQUENCE | FieldInfo.SEQUENCE_INT | FieldInfo.SEQUENCE_FLOAT =>
          values += v.toString
        case FieldInfo.INT =>
          values += v.toString.toInt
        case FieldInfo.DOUBLE =>
          values += v.toString.toDouble
        case FieldInfo.LONG =>
          values += v.toString.toLong
        case _ =>
          throw new Exception(s"not found field $f's data type")
      }
    })
    ArrayRow(values)
  }

  private[algorithm] def mapToMleapRow(dataMap: Map[String, Any],
                                       structType: StructType,
                                       fields: Array[String]):
  ml.combust.mleap.runtime.frame.Row = {

    val values = new ArrayBuffer[Any]()
    val structTypeFull = structType
    // 判断 value 类型，并以此构建 Row
    structTypeFull.fields.foreach(f => {
      val v = if (dataMap.contains(f.name)) {
        var value = dataMap(f.name)
        if (ForecastUtil.isNotNull(labelNameAndvalue._1) && f.name.equals(labelNameAndvalue._1)) {
          labelNameAndvalue._2.head
        } else {
          if (value == null) {
            ""
          } else {
            value
          }
        }
      } else {
        if (ForecastUtil.isNotNull(labelNameAndvalue._1) && f.name.equals(labelNameAndvalue._1)) {
          labelNameAndvalue._2.head
        } else {
          ""
        }
      }
      f.dataType.base match {
        case BasicType.String =>
          values += v.toString
        case BasicType.Int =>
          values += v.toString.toInt
        case BasicType.Double =>
          values += v.toString.toDouble
        case BasicType.Boolean =>
          values += v.toString.toBoolean
        case _ =>
          values += null
      }
    })
    ArrayRow(values)
  }

  /**
   * 根据 Map key 和 value 生成 mleap的StructType
   *
   * @param dataMap
   * @return
   */
  private[algorithm] def parseMleapStructFromMap(dataMap: Map[String, Any],
                                                 fields: Array[String]): Try[core.types.StructType] = {
    val structFields = new ArrayBuffer[StructField]()
    // 判断 value 类型，并以此构建 Row
    dataMap.keySet.foreach(k => {
      val v = dataMap(k)
      v match {
        case _: String =>
          structFields += StructField(k, ScalarType.String)
        case _: Int =>
          structFields += StructField(k, ScalarType.Int)
        case _: Double =>
          structFields += StructField(k, ScalarType.Double)
        case _: Boolean =>
          structFields += StructField(k, ScalarType.Boolean)
        case _ =>
          logWarning("format of value only supports String, Int, Double and Boolean," +
            s" thus convert $k to null")
          structFields += StructField(k, ScalarType.String)
      }
    })
    for (fieldName <- fields) {
      if (!dataMap.contains(fieldName)) {
        structFields += StructField(fieldName, ScalarType.String)
      }
    }
    StructType(structFields)
  }

  override def load: Array[Transformer] = {
    val startTime = System.currentTimeMillis()
    // 从 HDFS 上读取模型文件到本地临时文件
    /* logInfo(s"mleap paths is ${paths.mkString(",")}")
     var localTmpFile = new File(
       s"${System.getProperty("java.io.tmpdir")}/${ForecastUtil.getFileName(paths(0))}_${System.currentTimeMillis}")
     while (localTmpFile.exists()) {
       logWarning(s"mleap local tmp ${localTmpFile.getAbsolutePath} file existed")
       Thread.sleep(1)
       localTmpFile = new File(
         s"${System.getProperty("java.io.tmpdir")}/${ForecastUtil.getFileName(paths(0))}_${System.currentTimeMillis}")
     }
     HdfsUtils.get(paths(0), localTmpFile.getPath)*/

    // 加载并验证模型
    val transformer = (for (bf <- managed(BundleFile(s"jar:file:${paths.head}"))) yield {
      bf.loadMleapBundle().get.root
    }).tried.get

    // 删除临时文件
    /*if (!localTmpFile.delete()) {
      log.warn(s"在加载 mleap 模型后，删除临时文件失败: ${localTmpFile.getPath}")
    }*/
    logInfo(s"加载模型（mleap）耗时: ${System.currentTimeMillis() - startTime}ms")
    Array(transformer)
  }

  /**
   * 使用 MLEAP 模型文件进行预测
   *
   * @param frame
   * @return
   */
  private[algorithm] def predictWithMLEAP(frame: DefaultLeapFrame): DefaultLeapFrame = {
    val startTime = System.currentTimeMillis()
    val frameTr = mleapTransformer.head.transform(frame).get

    logInfo(s"MLEAP 模型预测耗时: ${System.currentTimeMillis() - startTime}ms")
    frameTr
  }


  /**
   * 用加载好的模型预测并返回预测概率值
   *
   * @param maps
   * @param topn
   * @return
   */
  override def predictProbabilityMaps(maps: Seq[Map[String, Any]], topn: Int, preModel: Array[Forecast[Any]] = null): Array[Map[String, Double]] = {
    predictProbabilitiesWithMLEAP(maps, topn)
  }

  private[algorithm] def predictProbabilitiesWithMLEAP(maps: Seq[Map[String, Any]],
                                                       topn: Int): Array[Map[String, Double]] = {
    val startTime = System.currentTimeMillis()
    val pipelineModel = mleapTransformer.head.model.asInstanceOf[PipelineModel]

    /*  val fields = pipelineModel.transformers.head
        .asInstanceOf[MetaStorage].model.fields
      val struct = if (mleapStructType.isSuccess && mleapStructType.get.fields.nonEmpty) {
        mleapStructType.get
      } else {
        parseMleapStructFromMap(maps.head, fields).get
      }
      val labels = labelNameAndvalue._2
      val rows = if (mleapStructType.isSuccess && mleapStructType.get.fields.nonEmpty) {
        maps.map(dataMap => {
          mapToMleapRow(dataMap)
        })
      } else {
        maps.map(dataMap => {
          mapToMleapRow(dataMap, struct, fields)
        })
      }*/


    /*val fields = pipelineModel.transformers.head
      .asInstanceOf[MetaStorage].model.fields*/
    val struct = mleapStructType.get
    var labels = labelNameAndvalue._2
    val rows = maps.map(dataMap => {
      mapToMleapRow(dataMap)
    })
    if (labels.isEmpty) {
      labels = Array("0", "1")
    }


    val preFrame = DefaultLeapFrame(struct, rows)
    val frame = mleapTransformer.head.transform(preFrame).get
    val result = frame.select(probabilityCol, predictionCol).get.dataset.map(row => {
      val values = row.getTensor(0).asInstanceOf[DenseTensor[Double]].values
      var map = Map[String, Double]()
      if (!labels.isEmpty) {
        for (i <- labels.indices) {
          map += (labels(i) -> values(i))
        }
      } else {
        val prediction = row.get(1).toString
        map += (prediction -> values.head)
      }
      map = ForecastUtil.sortMapValAndTakeTopn(map, topn)
      map.map(kv => (kv._1, kv._2.formatted("%.8f").toDouble))
    }).toArray

    //    val result = if (maps.size > 100) {
    //      val segmentRows = rows.sliding(20, 20).toArray
    //      segmentRows.map(rowSub => {
    //        val preFrame = DefaultLeapFrame(struct, rowSub)
    //        val future = new FutureTask[Array[Map[String, Double]]](new ProbabilityThread(null, mleapTransformer.head, preFrame, labels, tool, topn,
    //          probabilityCol, predictionCol))
    //        threadPool.submit(future)
    //        future
    //      }).flatMap(_.get)
    //    }else{
    //      val preFrame = DefaultLeapFrame(struct, rows)
    //      val frame = mleapTransformer.head.transform(preFrame).get
    //      frame.select(probabilityCol, predictionCol).get.dataset.map(row => {
    //        val values = row.getTensor(0).asInstanceOf[DenseTensor[Double]].values
    //        var map = Map[String, Double]()
    //        if (!labels.isEmpty) {
    //          for (i <- labels.indices) {
    //            map += (labels(i) -> values(i))
    //          }
    //        } else {
    //          val prediction = row.get(1).toString
    //          map += (prediction -> values.head)
    //        }
    //        map = tool.sortMapValAndTakeTopn(map, topn)
    //        map.map(kv=>(kv._1,kv._2.formatted("0.8f").toDouble))
    //      }).toArray
    //    }

    //    var totalRes = new ArrayBuffer[Map[String, Double]]()
    //    if (maps.size > 20) {
    //      val segmentRows = rows.sliding(10, 10).toArray
    //      segmentRows.foreach(rowSub => {
    //        val preFrame = DefaultLeapFrame(struct, rowSub)
    //        val future = new FutureTask[Array[Map[String, Double]]](new ProbabilityThread(null, mleapTransformer.head, preFrame, labels, tool, topn,
    //          probabilityCol, predictionCol))
    //        threadPool.submit(future)
    //        future.get().foreach(m => totalRes += m)
    //      })
    //    } else {
    //      val preFrame = DefaultLeapFrame(struct, rows)
    //      val frame = mleapTransformer.head.transform(preFrame).get
    //      val res = frame.select(probabilityCol, predictionCol).get.dataset.map(row => {
    //        val values = row.getTensor(0).asInstanceOf[DenseTensor[Double]].values
    //        var map = Map[String, Double]()
    //        if (!labels.isEmpty) {
    //          for (i <- labels.indices) {
    //            map += (labels(i) -> values(i))
    //          }
    //        } else {
    //          val prediction = row.get(1).toString
    //          map += (prediction -> values.head)
    //        }
    //        map = tool.sortMapValAndTakeTopn(map, topn)
    //        // 概率值精确到小数点后 8 位
    //        map.foreach(r => map += (r._1 -> r._2.formatted("%.8f").toDouble))
    //        map
    //      }).toArray
    //      res.foreach(m => totalRes += m)
    //    }
    //    logInfo(s"计算概率（MLEAP）总耗时: ${System.currentTimeMillis() - startTime}ms")
    //    totalRes.toArray

    logInfo(s"计算概率（MLEAP）总耗时: ${System.currentTimeMillis() - startTime}ms")
    result
  }

  /**
   * 用加载好的回归模型预测并返回实数值
   *
   * @param maps
   * @return
   */
  override def getPredictionMaps(maps: Seq[Map[String, Any]]): Array[Double] = {
    val startTime = System.currentTimeMillis()
    val pipelineModel = mleapTransformer.head.model.asInstanceOf[PipelineModel]

    val fields = pipelineModel.transformers.head
      .asInstanceOf[MetaStorage].model.fields
    val totalRes = maps.map(dataMap => {
      val struct = parseMleapStructFromMap(dataMap, fields).get

      val row = mapToMleapRow(dataMap, struct, fields)
      val preFrame = DefaultLeapFrame(struct, Seq(row))
      val frame = mleapTransformer.head.transform(preFrame).get
      val prediction = frame.select("prediction").get.dataset.head.getDouble(0)
      prediction
    }).toArray
    logInfo(s"计算概率（MLEAP）总耗时: ${System.currentTimeMillis() - startTime}ms")
    totalRes
  }

  override def predict(maps: Seq[Map[String, Any]]): Array[Map[String, Any]] = {
    val startTime = System.currentTimeMillis()
    val rows = maps.map(dataMap => {
      mapToMleapRow(dataMap)
    })
    val segmentRows = rows.sliding(100, 100).toArray
    val processedMaps = segmentRows.flatMap(rowSeg => {
      val preFrame = DefaultLeapFrame(mleapStructType.get, rowSeg)
      val frame = mleapTransformer.head.transform(preFrame).get
      val frameCols = frame.schema.fields.map(f => f.name)
      val value = frame.select(frameCols: _*).get.collect().map(row => {
        frameCols.zipWithIndex.map(field => {
          field._1 -> row.get(field._2)
        }).toMap
      })
      value
    })
    logInfo(s"预处理（MLEAP）总耗时: ${System.currentTimeMillis() - startTime}ms")
    processedMaps
  }
}

class ProbabilityThread(threadName: String, transformer: Transformer, preFrame: DefaultLeapFrame,
                        labels: Array[String], topn: Int,
                        probabilityCol: String,
                        predictionCol: String) extends Callable[Array[Map[String, Double]]] with Log {
  override def call(): Array[Map[String, Double]] = {
    val frame = transformer.transform(preFrame).get
    frame.select(probabilityCol, predictionCol).get.dataset.map(row => {
      val values = row.getTensor(0).asInstanceOf[DenseTensor[Double]].values
      var map = Map[String, Double]()
      if (!labels.isEmpty) {
        for (i <- labels.indices) {
          map += (labels(i) -> values(i))
        }
      } else {
        val prediction = row.get(1).toString
        map += (prediction -> values.head)
      }
      map = ForecastUtil.sortMapValAndTakeTopn(map, topn)
      // 概率值精确到小数点后 8 位
      map.foreach(r => map += (r._1 -> r._2.formatted("%.8f").toDouble))
      map
    }).toArray
  }
}

class predictionThread(threadName: String, transformer: Transformer, dataMaps: Seq[Map[String, Any]], mleapStructType: Try[core.types.StructType],
                       val fieldInfo: Map[String, Array[String]]) extends Callable[Array[Map[String, Any]]] {
  override def call(): Array[Map[String, Any]] = {
    val rows = dataMaps.map(data => {
      mapToMleapRow(data)
    })
    val preFrame = DefaultLeapFrame(mleapStructType.get, rows)
    val frame = transformer.transform(preFrame).get
    val frameCols = frame.schema.fields.map(f => f.name)
    frame.select(frameCols: _*).get.collect().map(row => {
      frameCols.zipWithIndex.map(field => {
        val fieldName = field._1
        val fieldIndex = field._2
        val value = row.get(fieldIndex)
        fieldName -> value
      }).toMap
    }).toArray
  }

  private[algorithm] def mapToMleapRow(dataMap: Map[String, Any]):
  ml.combust.mleap.runtime.frame.Row = {
    val values = new ArrayBuffer[Any]()
    // 判断 value 类型，并以此构建 Row
    mleapStructType.get.fields.foreach(f => {
      val v = if (dataMap.contains(f.name)) {
        if (dataMap(f.name) == null || !ForecastUtil.isNotNull(dataMap(f.name).toString)) {
          if (fieldInfo(f.name).last == "") {
            throw new Exception("无默认填充值的字段不能为空")
          } else {
            fieldInfo(f.name).last
          }
        } else {
          dataMap(f.name)
        }
      } else {
        if (fieldInfo(f.name).last == "") {
          throw new Exception("无默认填充值的字段不能为空")
        } else {
          fieldInfo(f.name).last
        }
      }
      fieldInfo.getOrElse(f.name, null).head match {
        case FieldInfo.STRING | FieldInfo.SEQUENCE | FieldInfo.SEQUENCE_INT | FieldInfo.SEQUENCE_FLOAT =>
          values += v.toString
        case FieldInfo.INT =>
          values += v.toString.toInt
        case FieldInfo.DOUBLE =>
          values += v.toString.toDouble
        case _ =>
          throw new Exception(s"not found field $f's data type")
      }
    })
    ArrayRow(values)
  }
}
