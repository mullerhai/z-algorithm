package com.zen.spark.transformer

import com.zen.spark.transformer.ToTypes.ToType
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.{DataTypes, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/2 14:20
 * @description 列数据分词器，将原始列数据按分隔符分割，再用空格分隔成 "word word..." 格式
 * @reviewer
 */
class WordSpliter(override val uid: String) extends Transformer with DefaultParamsWritable {

  final val inputCol = new Param[String](this, "inputCol", "input column name")
  final val outputCol = new Param[String](this, "outputCol", "output column name")
  final val delimiter = new Param[String](this, "delimiter", "delimiter for features")
  final val toType = new Param[String](this, "toType", "type to be converted")

  def setInputCol(value: String): this.type = set(inputCol, value)

  def getInputCol: String = $(inputCol)

  def setOutputCol(value: String): this.type = set(outputCol, value)

  def getOutputCol: String = $(outputCol)

  def setDelimiter(value: String): this.type = set(delimiter, value)

  def getDelimiter: String = $(delimiter)

  def setToType(value: ToType): this.type = {
    value match {
      case ToTypes.String => set(toType, "String")
      case ToTypes.Array => set(toType, "Array")
    }
  }

  def getToType: ToType = {
    $(toType) match {
      case "String" => ToTypes.String
      case "Array" => ToTypes.Array
    }
  }

  setDefault(inputCol, "inputCol")
  setDefault(outputCol, "outputCol")
  setDefault(delimiter, " ")
  setDefault(toType, "String")

  def this() = this(Identifiable.randomUID("wordspliter"))

  override def transform(dataset: Dataset[_]): DataFrame = {
    val splittedFeatures = getToType match {
      case ToTypes.String => udf { words: String => {
        // 结果保存为 String 类型
        if (!" ".equals($(delimiter))) {
          // 各特征不用空格分隔时，将其转成空格分隔
          words.split($(delimiter)).mkString(" ")
        } else {
          // 用空格分隔时，不做任何处理
          words
        }
      }
      }
      case ToTypes.Array => udf { words: String => {
        // 结果保存为 Array 类型
        words.split($(delimiter))
      }
      }
    }

    dataset.withColumn($(outputCol), splittedFeatures(dataset.col($(inputCol))))
  }

  override def copy(extra: ParamMap): Transformer = {
    defaultCopy(extra)
  }

  override def transformSchema(schema: StructType): StructType = {
    // 分词列必修为字符串类型
    val input = schema.fields(schema.fieldIndex($(inputCol)))
    require(input.dataType == StringType, "inputCol must be a StringType")
    getToType match {
      case ToTypes.String => schema.add(StructField($(outputCol), DataTypes.StringType))
      case ToTypes.Array => schema.add(StructField($(outputCol), DataTypes.createArrayType(DataTypes.StringType)))
    }
  }

}

object WordSpliter extends DefaultParamsReadable[WordSpliter] {}

object ToTypes extends Enumeration {
  type ToType = Value
  val String, Array = Value
}
