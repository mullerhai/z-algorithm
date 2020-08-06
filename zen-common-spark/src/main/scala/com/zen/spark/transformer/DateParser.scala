package com.zen.spark.transformer

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import org.apache.spark.SparkException
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.attribute.{NominalAttribute, NumericAttribute}
import org.apache.spark.ml.param.{IntParam, Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.{IntegerType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/1 9:32
 * @description 时间解析组件,根据指定的日期格式解析出对应特征
 * @reviewer
 */
class DateParser(override val uid: String) extends Transformer with DefaultParamsWritable {

  def this() = this(Identifiable.randomUID("parse_date"))

  final val inputCol = new Param[String](this, "inputCol", "column who holds datetime info")

  def setInputCol(value: String): this.type = set(inputCol, value)

  def getInputCol: String = $(inputCol)

  final val format = new Param[String](this, "format", "datetime format")

  def setFormat(value: String): this.type = set(format, value)

  def getFormat: String = $(format)


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
  // 默认日期格式 yyyy-MM-dd HH:mm:ss
  //  setDefault(format, "yyyy-MM-dd HH:mm:ss")

  override def transform(dataset: Dataset[_]): DataFrame = {
    transformSchema(dataset.schema)
    var result = dataset
    val cal = Calendar.getInstance
    val curDate = new Date()
    val parseDate = (signal: Int) => udf { value: String => {
      var dateFormat: SimpleDateFormat = null
      try {
        dateFormat = new SimpleDateFormat($(format))
        if (value.trim.equals("unknown") || value.trim.isEmpty) {
          cal.setTime(new Date())
        } else {
          cal.setTime(dateFormat.parse(value))
        }
      } catch {
        case _: Exception => throw new SparkException(s"用 $getFormat 解析 $value 出错")
      }
      signal match {
        case ParseDateType.MONTH_OF_YEAR | ParseDateType.HOUR_OF_DAY =>
          cal.get(signal)
        case ParseDateType.DAY_OF_MONTH | ParseDateType.DAY_OF_WEEK =>
          cal.get(signal) - 1
        case ParseDateType.DAY_2_CURRENT =>
          ((curDate.getTime - cal.getTimeInMillis) / (1000 * 3600 * 24)).asInstanceOf[Int]
        case _ =>
          throw new Exception("match failure,please check date format")
      }
    }
    }

    if (isDefined(hourOfDayCol)) {
      result = result.withColumn($(hourOfDayCol), parseDate(ParseDateType.HOUR_OF_DAY)(dataset($(inputCol))))

    }
    if (isDefined(dayOfWeekCol)) {
      result = result.withColumn($(dayOfWeekCol), parseDate(ParseDateType.DAY_OF_WEEK)(dataset($(inputCol))))
    }
    if (isDefined(dayOfMonthCol)) {
      result = result.withColumn($(dayOfMonthCol), parseDate(ParseDateType.DAY_OF_MONTH)(dataset($(inputCol))))
    }
    if (isDefined(monthOfYearCol)) {
      result = result.withColumn($(monthOfYearCol), parseDate(ParseDateType.MONTH_OF_YEAR)(dataset($(inputCol))))
    }
    if (isDefined(daysToCurrentCol)) {
      result = result.withColumn($(daysToCurrentCol), parseDate(ParseDateType.DAY_2_CURRENT)(dataset($(inputCol))))
    }
    result.toDF
  }

  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    val inputColName = $(inputCol)
    require(schema.fieldNames.contains(inputColName), s"inputCol $inputColName doesn't exist")
    require(isDefined(format), s"must set format")
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

object DateParser extends DefaultParamsReadable[DateParser] {

}

object ParseDateType extends Enumeration {
  val HOUR_OF_DAY = Calendar.HOUR_OF_DAY
  val DAY_OF_WEEK = Calendar.DAY_OF_WEEK
  val DAY_OF_MONTH = Calendar.DAY_OF_MONTH
  val MONTH_OF_YEAR = Calendar.MONTH
  val DAY_2_CURRENT = -1
}
