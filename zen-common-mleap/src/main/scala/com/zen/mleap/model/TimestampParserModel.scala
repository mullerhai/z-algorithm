package com.zen.mleap.model

import java.util.Calendar

import com.zen.spark.transformer.ParseDateType
import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}

import scala.collection.mutable.ArrayBuffer

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 16:25
 * @description
 * @reviewer
 */
case class TimestampParserModel(inputCol: String,
                                unit: Int,
                                dayOfWeekCol: String = null,
                                dayOfMonthCol: String = null,
                                monthOfYearCol: String = null,
                                hourOfDayCol: String = null,
                                daysToCurrentCol: String = null
                               ) extends Model {

  override def inputSchema: StructType = StructType("input" -> ScalarType.Long).get

  override def outputSchema: StructType = {
    val array = new ArrayBuffer[String]
    if (dayOfWeekCol.nonEmpty) {
      array += dayOfWeekCol
    }
    if (dayOfMonthCol.nonEmpty) {
      array += dayOfMonthCol
    }
    if (monthOfYearCol.nonEmpty) {
      array += monthOfYearCol
    }
    if (hourOfDayCol.nonEmpty) {
      array += hourOfDayCol
    }
    if (daysToCurrentCol.nonEmpty) {
      array += daysToCurrentCol
    }
    val outputFields = array.indices.map(idx => {
      StructField(s"output${idx + 1}", ScalarType.Int)
    })
    StructType(outputFields).get
  }

  def apply(value: Long, timeType: Int): Int = {
    val calendar = Calendar.getInstance()
    calendar.setTimeInMillis(value)
    timeType match {
      case ParseDateType.DAY_OF_WEEK => calendar.get(Calendar.DAY_OF_WEEK) - 1
      case ParseDateType.DAY_OF_MONTH => calendar.get(Calendar.DAY_OF_MONTH) - 1
      case ParseDateType.MONTH_OF_YEAR => calendar.get(Calendar.MONTH)
      case ParseDateType.HOUR_OF_DAY => calendar.get(Calendar.HOUR_OF_DAY)
      case ParseDateType.DAY_2_CURRENT => ((System.currentTimeMillis() - value) / (1000 * 3600 * 24)).asInstanceOf[Int]
    }
  }
}

