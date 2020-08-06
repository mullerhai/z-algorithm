package com.zen.mleap.transformer

import com.zen.mleap.model.TimestampParserModel
import com.zen.spark.transformer.ParseDateType
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.{FrameBuilder, Transformer}
import ml.combust.mleap.runtime.function.FieldSelector
import org.apache.commons.lang.StringUtils

import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 16:42
 * @description
 * @reviewer
 */
case class TimestampParser(override val uid: String = Transformer.uniqueName("parse_date"),
                           override val shape: NodeShape,
                           override val model: TimestampParserModel
                          ) extends Transformer {
  val inputCol: String = inputSchema.fields.head.name
  private val f_dayOfWeek = (value: Long) => {
    val timestamp = if (model.unit == 0) {
      value * 1000
    } else value
    model.apply(timestamp, ParseDateType.DAY_OF_WEEK)
  }
  private val f_dayOfMonth = (value: Long) => {
    val timestamp = if (model.unit == 0) {
      value * 1000
    } else value
    model.apply(timestamp, ParseDateType.DAY_OF_MONTH)
  }
  private val f_monthOfYear = (value: Long) => {
    val timestamp = if (model.unit == 0) {
      value * 1000
    } else value
    model.apply(timestamp, ParseDateType.MONTH_OF_YEAR)
  }
  private val f_hourOfDay = (value: Long) => {
    val timestamp = if (model.unit == (0)) {
      value * 1000
    } else value
    model.apply(timestamp, ParseDateType.HOUR_OF_DAY)
  }
  private val f_daysToCurrent = (value: Long) => {
    val timestamp = if (model.unit == 0) {
      value * 1000
    } else value
    model.apply(timestamp, ParseDateType.DAY_2_CURRENT)
  }
  private val inputSelector: FieldSelector = inputCol

  override def transform[FB <: FrameBuilder[FB]](builder: FB): Try[FB] = {
    var builder_ = builder
    if (StringUtils.isNotEmpty(model.dayOfWeekCol)) {
      builder_ = builder_.withColumn(model.dayOfWeekCol, inputSelector)(f_dayOfWeek).get
    }
    if (StringUtils.isNotEmpty(model.dayOfMonthCol)) {
      builder_ = builder_.withColumn(model.dayOfMonthCol, inputSelector)(f_dayOfMonth).get
    }
    if (StringUtils.isNotEmpty(model.monthOfYearCol)) {
      builder_ = builder_.withColumn(model.monthOfYearCol, inputSelector)(f_monthOfYear).get
    }
    if (StringUtils.isNotEmpty(model.hourOfDayCol)) {
      builder_ = builder_.withColumn(model.hourOfDayCol, inputSelector)(f_hourOfDay).get
    }
    if (StringUtils.isNotEmpty(model.daysToCurrentCol)) {
      builder_ = builder_.withColumn(model.daysToCurrentCol, inputSelector)(f_daysToCurrent).get
    }
    Try(builder_)
  }
}
