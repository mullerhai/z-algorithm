package com.zen.spark.transformer

import java.util.{Calendar, Date}

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.attribute.NumericAttribute
import org.apache.spark.ml.param.{IntParam, Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/1 9:32
 * @description 时间解析组件,根据指定的日期格式解析出对应特征
 * @reviewer
 */
class TimestampParser(override val uid: String) extends Transformer with DefaultParamsWritable {

  def this() = this(Identifiable.randomUID("parse_timestamp"))

  final val inputCol = new Param[String](this, "inputCol", "column who holds datetime info")

  def setInputCol(value: String): this.type = set(inputCol, value)

  def getInputCol: String = $(inputCol)

  /**
   * 与format 2选1，两个必须选择一个
   */
  final val unit = new IntParam(this, "timestamp unit", "0:s,1:ms")

  def setUnit(value: Int): this.type = set(unit, value)

  def getUnit: Int = $(unit)

  final val hourOfDayCol = new Param[String](this, "hourOfDayCol", "column who saves hourOfDay")

  def setHourOfDayCol(value: String): this.type = set(hourOfDayCol, value)

  def getHourOfDayCol: String = $(hourOfDayCol)

  final val dayOfWeekCol = new Param[String](this, "dayOfWeekCol", "column who saves dayOfWeek")

  def setDayOfWeekCol(value: String): this.type = set(dayOfWeekCol, value)

  def getDayOfWeekCol: String = $(dayOfWeekCol)

  final val dayOfMonthCol = new Param[String](this, "dayOfMonthCol", "column who saves dayOfMonth")

  def setDayOfMonthCol(value: String): this.type = set(dayOfMonthCol, value)

  def getDayOfMonthCol: String = $(dayOfMonthCol)

  final val monthOfYearCol = new Param[String](this, "monthOfYearCol", "column who saves monthOfYear")

  def setMonthOfYearCol(value: String): this.type = set(monthOfYearCol, value)

  def getMonthOfYearCol: String = $(monthOfYearCol)

  /**
   * 传入的时间距当前多少天
   */
  final val daysToCurrentCol = new Param[String](this, "daysToCurrentCol", "how many days from now")

  def setDaysToCurrentCol(value: String): this.type = set(daysToCurrentCol, value)

  def getDaysToCurrentCol: String = $(daysToCurrentCol)


  setDefault(inputCol, "input")

  override def transform(dataset: Dataset[_]): DataFrame = {

    transformSchema(dataset.schema)
    var result = dataset

    val cal = Calendar.getInstance
    val curDate = new Date()
    val parseTimestamp = (signal: Int) => udf { value: Long => {
      if ($(unit) == 1) {
        cal.setTime(new Date(value))
      } else cal.setTime(new Date(value * 1000))
      signal match {
        case ParseDateType.MONTH_OF_YEAR | ParseDateType.HOUR_OF_DAY =>
          cal.get(signal)
        case ParseDateType.DAY_OF_MONTH | ParseDateType.DAY_OF_WEEK =>
          cal.get(signal) - 1
        case ParseDateType.DAY_2_CURRENT =>
          ((curDate.getTime - cal.getTimeInMillis) / (1000 * 3600 * 24)).asInstanceOf[Int]
        case _ =>
          throw new Exception("match failure,please check timestamp value")
      }
    }

    }

    if (isDefined(hourOfDayCol)) {
      result = result.withColumn($(hourOfDayCol), parseTimestamp(ParseDateType.HOUR_OF_DAY)(dataset($(inputCol))))
    }
    if (isDefined(dayOfWeekCol)) {
      result = result.withColumn($(dayOfWeekCol), parseTimestamp(ParseDateType.DAY_OF_WEEK)(dataset($(inputCol))))
    }
    if (isDefined(dayOfMonthCol)) {
      result = result.withColumn($(dayOfMonthCol), parseTimestamp(ParseDateType.DAY_OF_MONTH)(dataset($(inputCol))))

    }
    if (isDefined(monthOfYearCol)) {
      result = result.withColumn($(monthOfYearCol), parseTimestamp(ParseDateType.MONTH_OF_YEAR)(dataset($(inputCol))))
    }
    if (isDefined(daysToCurrentCol)) {
      result = result.withColumn($(daysToCurrentCol), parseTimestamp(ParseDateType.DAY_2_CURRENT)(dataset($(inputCol))))
    }
    result.toDF
  }

  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    val inputColName = $(inputCol)
    require(schema.fieldNames.contains(inputColName), s"inputCol $inputColName doesn't exist")
    require(isDefined(unit), s"must set unit")
    require(Array(0, 1).indexOf($(unit)) >= 0, "unit value must set 0 or 1")
    // 如果不设置输出那些日期信息，则全部输出，且输出字段名字为 inputCol_对应信息
    if (!isDefined(dayOfWeekCol) && !isDefined(dayOfMonthCol) && !isDefined(monthOfYearCol) &&
      !isDefined(hourOfDayCol) && !isDefined(daysToCurrentCol)) {
      setDayOfWeekCol(s"${getInputCol}_dayOfWeek")
      setDayOfMonthCol(s"${getInputCol}_dayOfMonth")
      setMonthOfYearCol(s"${getInputCol}_monthOfYear")
      setHourOfDayCol(s"${getInputCol}_hourOfDay")
      setDaysToCurrentCol(s"${getInputCol}_daysToCurrent")
    }
    validateAndAddSchema(schema)
  }

  protected def validateAndAddSchema(schema: StructType): StructType = {
    val inputFields = schema.fields
    var outputFields = inputFields
    if (isDefined(hourOfDayCol)) {
      require(!schema.fieldNames.contains(getHourOfDayCol), s"column $hourOfDayCol already existed")
      outputFields = outputFields :+ StructField(getHourOfDayCol, IntegerType)
    }
    if (isDefined(dayOfWeekCol)) {
      require(!schema.fieldNames.contains(getDayOfWeekCol), s"column $getDayOfWeekCol already existed")
      outputFields = outputFields :+ StructField(getDayOfWeekCol, IntegerType)
    }
    if (isDefined(dayOfMonthCol)) {
      require(!schema.fieldNames.contains(getDayOfMonthCol), s"column $getDayOfMonthCol already existed")
      outputFields = outputFields :+ StructField(getDayOfMonthCol, IntegerType)
    }
    if (isDefined(monthOfYearCol)) {
      require(!schema.fieldNames.contains(getMonthOfYearCol), s"column $getMonthOfYearCol already existed")
      outputFields = outputFields :+ StructField(getMonthOfYearCol, IntegerType)
    }
    if (isDefined(daysToCurrentCol)) {
      require(!schema.fieldNames.contains(getDaysToCurrentCol), s"column $getDaysToCurrentCol already existed")
      outputFields = outputFields :+ StructField(getDaysToCurrentCol, IntegerType)
    }
    StructType(outputFields)
  }
}

object TimestampParser extends DefaultParamsReadable[TimestampParser] {

}
