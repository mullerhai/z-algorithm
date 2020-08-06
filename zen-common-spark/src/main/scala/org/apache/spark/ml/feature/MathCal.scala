package org.apache.spark.ml.feature

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.shared.{HasInputCol, HasOutputCol}
import org.apache.spark.ml.param.{Param, ParamMap, ParamValidators}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable, SchemaUtils}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 12:17
 * @description 数学计算组件，如取对数，正弦变换，余弦变换
 * @reviewer
 */
class MathCal(override val uid: String) extends Transformer
  with DefaultParamsWritable with HasInputCol with HasOutputCol {
  def this() = this(Identifiable.randomUID("math_cal"))

  def setInputCol(value: String): this.type = set(inputCol, value)

  def setOutputCol(value: String): this.type = set(outputCol, value)

  final val operation = new Param[String](this, "operation", "operation symbol",
    ParamValidators.inArray(Array(Operation.log10, Operation.loge, Operation.sin, Operation.cos, Operation.sigmod))
  )

  def getOperation: String = $(operation)

  def setOperation(value: String): this.type = set(operation, value.toLowerCase)


  override def transform(dataset: Dataset[_]): DataFrame = {
    val operationUDF = (op: String) => udf { value: Double => {
      op match {
        case Operation.log10 =>
          if (value <= 1) {
            0.0
          } else Math.log10(value)
        case Operation.loge =>
          if (value <= 1) {
            0.0
          } else Math.log(value)
        case Operation.sin =>
          Math.sin(value)
        case Operation.cos =>
          Math.cos(value)
        case Operation.sigmod =>
          1 / (1 + Math.exp(-value))
        case _ =>
          throw new IllegalArgumentException(s"not support operation $op")
      }
    }
    }
    dataset.withColumn($(outputCol), operationUDF($(operation))(dataset($(inputCol)).cast(DoubleType)))
  }

  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = validateAndTransformSchema(schema)

  protected def validateAndTransformSchema(schema: StructType): StructType = {
    val inputColName = $(inputCol)
    val outputColName = $(outputCol)
    require(schema.fieldNames.contains(inputColName), s"inputCol $inputColName doesn't exist")
    require(!schema.fieldNames.contains(outputColName), s"outputCol $outputColName alreay exist")
    SchemaUtils.checkNumericType(schema, inputColName)
    SchemaUtils.appendColumn(schema, StructField(outputColName, DoubleType, nullable = true))
  }
}

object MathCal extends DefaultParamsReadable[MathCal] {}

object Operation {
  final val log10 = "log10"
  final val loge = "loge"
  final val sin = "sin"
  final val cos = "cos"
  final val sigmod = "sigmod"
}
