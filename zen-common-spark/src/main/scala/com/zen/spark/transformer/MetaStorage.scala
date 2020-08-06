package com.zen.spark.transformer

import com.zen.spark.transformer.ModelType.ModelType
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.{Param, ParamMap, StringArrayParam}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization._

/**
 * @author xiongjun
 * @date 2020/6/1 10:00
 * @description
 * @reviewer 存储模型元数据，在使用一个模型是可以根据其元信息做出更加灵活的处理
 */
class MetaStorage(override val uid: String) extends Transformer with DefaultParamsWritable {

  implicit val formats = DefaultFormats

  final val modelType = new Param[String](this, "modelType", "model type")
  final val parameters = new Param[String](this, "params", "parameters")
  final val fields = new StringArrayParam(this, "fields", "field structure of training data")

  def setModelType(value: ModelType): this.type = {
    value match {
      case ModelType.Preprocessing => set(modelType, "Preprocessing")
      case ModelType.Feature => set(modelType, "Feature")
      case ModelType.Algorithm_Classification_Prob => set(modelType, "Algorithm_Classification_Prob")
      case ModelType.Algorithm_Regression => set(modelType, "Algorithm_Regression")
      case ModelType.Clustering => set(modelType, "Clustering")
    }
  }

  def getModelType: ModelType = {
    $(modelType) match {
      case "Preprocessing" => ModelType.Preprocessing
      case "Feature" => ModelType.Feature
      case "Algorithm_Classification_Prob" => ModelType.Algorithm_Classification_Prob
      case "Algorithm_Regression" => ModelType.Algorithm_Regression
      case "Clustering" => ModelType.Clustering
      case "FPM" => ModelType.FPM
    }
  }

  def setParameters(value: Map[String, Any]): this.type = {
    set(parameters, writePretty(value))
  }

  def getParameters: Map[String, Any] = {
    read[Map[String, Any]]($(parameters))
  }

  def setFields(value: Array[String]): this.type = set(fields, value)

  def getFields: Array[String] = $(fields)

  setDefault(modelType, "Preprocessing")
  setDefault(parameters, "{}")
  setDefault(fields, new Array[String](0))

  def this() = this(Identifiable.randomUID("metastorage"))

  override def transform(dataset: Dataset[_]): DataFrame = dataset.toDF()

  override def copy(extra: ParamMap): Transformer = {
    defaultCopy(extra)
  }

  override def transformSchema(schema: StructType): StructType = {
    schema
  }

}

object MetaStorage extends DefaultParamsReadable[MetaStorage] {}

object ModelType extends Enumeration {
  type ModelType = Value
  val Preprocessing, Feature, Algorithm_Classification_Prob, Algorithm_Regression, Clustering, FPM = Value
}

object FieldInfo {
  final val INT = "int"
  final val DOUBLE = "double"
  final val STRING = "string"
  final val SEQUENCE_INT = "sequence_int"
  final val SEQUENCE_FLOAT = "sequence_float"
  final val SEQUENCE = "sequence"
  final val ARRAY = "array"
  final val LONG = "long"
  final val VECTOR = "vector"
}

