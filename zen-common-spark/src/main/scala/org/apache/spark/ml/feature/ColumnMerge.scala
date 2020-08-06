package org.apache.spark.ml.feature

import jdk.nashorn.internal.codegen.types.NumericType
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.shared.{HasInputCols, HasOutputCol}
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable, SchemaUtils}
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: morris
 * @Date: 2020/7/15 18:43
 * @description 列合并
 * @reviewer
 */
class ColumnMerge(override val uid: String) extends Transformer
  with DefaultParamsWritable with HasInputCols with HasOutputCol {
  def this() = this(Identifiable.randomUID("col_merge"))

  def setInputCols(values: Array[String]): this.type = set(inputCols, values)

  def setOutputCol(value: String): this.type = set(outputCol, value)


  /** 分隔符数组，与inputCol的index对应 */
  val delimiter = new Param[String](this, "delimiter",
    "delimiter Array")
  setDefault(delimiter, "")

  def getDelimiter: String = $(delimiter)

  def setDelimiter(value: String): this.type = set(delimiter, value)


  override def transform(dataset: Dataset[_]): DataFrame = {
    val inputColNames = $(inputCols)
    val outputColName = $(outputCol)
    val function = udf { (value: Any, res: String) => {
      val delimiter = getDelimiter
      value.asInstanceOf[String]
      res + delimiter + value
    }
    }
    var columnMergeRes = col(inputColNames.head).cast(StringType)
    inputColNames.indices.foreach(index => {
      val inputColName = inputColNames(index)
      if (index != 0) {
        columnMergeRes = function(col(inputColName).cast(StringType), columnMergeRes).as(outputColName)
      }
    })
    dataset.withColumn(outputColName, columnMergeRes)
  }


  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = validateAndTransformSchema(schema)

  protected def validateAndTransformSchema(schema: StructType): StructType = {
    //支持String double int类型
    $(inputCols).foreach(SchemaUtils.checkColumnTypes(schema,_,Seq(StringType,IntegerType,DoubleType)))
    val outputFields = schema.fields ++ Array(StructField(getOutputCol, StringType, nullable = true))
    StructType(outputFields)
  }


}

object ColumnMerge extends DefaultParamsReadable[ColumnMerge]

