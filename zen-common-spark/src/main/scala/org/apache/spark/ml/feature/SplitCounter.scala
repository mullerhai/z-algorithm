package org.apache.spark.ml.feature

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.shared.{HasInputCols, HasOutputCols}
import org.apache.spark.ml.param.{ParamMap, StringArrayParam}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable, SchemaUtils}
import org.apache.spark.sql.functions.{col, lit, udf}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: morris
 * @Date: 2020/6/18 11:10
 * @description 分词词数统计
 * @reviewer
 */
class SplitCounter(override val uid: String) extends Transformer
  with DefaultParamsWritable with HasInputCols with HasOutputCols {
  def this() = this(Identifiable.randomUID("split_counter"))

  def setInputCols(values: Array[String]): this.type = set(inputCols, values)

  def setOutputCols(values: Array[String]): this.type = set(outputCols, values)


  /** 分隔符数组，与inputCol的index对应 */
  val delimiterArray = new StringArrayParam(this, "min",
    "delimiter Array")

  def getDelimiterArray: Array[String] = $(delimiterArray)

  def setDelimiterArray(value: Array[String]): this.type = set(delimiterArray, value)


  override def transform(dataset: Dataset[_]): DataFrame = {
    val inputColNames = $(inputCols)
    val outputColNames = $(outputCols)
    val function = udf { (value: String, index: Int) => {
      val delimiter = getDelimiterArray(index)
      val length = value.split("\\" + delimiter).length
      length
    }
    }
    val outFeatureCol = inputColNames.indices.map(index => {
      val inputColName = inputColNames(index)
      val outputColName = outputColNames(index)
      val column = function(col(inputColName).cast(StringType), lit(index)).as(outputColName)
      column
    })
    dataset.withColumns(outputColNames, outFeatureCol)
  }


  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = validateAndTransformSchema(schema)

  protected def validateAndTransformSchema(schema: StructType): StructType = {
    require($(delimiterArray).length == $(inputCols).length, s"The specified min(${$(delimiterArray)}).length is not equal to max(${$(inputCols)}).length")

    $(inputCols).foreach(SchemaUtils.checkColumnType(schema, _, StringType))
    val contains = $(outputCols).filter(schema.fieldNames.contains(_))
    require(contains.length == 0,
      s"Output columns ${contains.mkString(",", "[", "]")} already exists.")
    val outputFields = schema.fields ++ $(outputCols).map(colName => StructField(colName, DoubleType, nullable = true))
    StructType(outputFields)
  }


}

object SplitCounter extends DefaultParamsReadable[SplitCounter]


