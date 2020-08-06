package com.zen.mleap.ops.spark

import com.zen.spark.transformer.TimestampParser
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}

import scala.collection.mutable.ArrayBuffer

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class TimestampParserOp extends SimpleSparkOp[TimestampParser] {
  override def sparkInputs(obj: TimestampParser): Seq[ParamSpec] = Seq("input" -> obj.inputCol)

  override def sparkOutputs(obj: TimestampParser): Seq[ParamSpec] = {
    val outputs = ArrayBuffer[ParamSpec]()
    var i = 1
    if (obj.isDefined(obj.dayOfWeekCol)) {
      outputs += (s"output${i}" -> obj.dayOfWeekCol)
      i += 1
    }
    if (obj.isDefined(obj.dayOfMonthCol)) {
      outputs += (s"output${i}" -> obj.dayOfMonthCol)
      i += 1
    }
    if (obj.isDefined(obj.monthOfYearCol)) {
      outputs += (s"output${i}" -> obj.monthOfYearCol)
      i += 1
    }
    if (obj.isDefined(obj.hourOfDayCol)) {
      outputs += (s"output${i}" -> obj.hourOfDayCol)
      i += 1
    }
    if (obj.isDefined(obj.daysToCurrentCol)) {
      outputs += (s"output${i}" -> obj.daysToCurrentCol)
      i += 1
    }
    outputs
  }

  override def sparkLoad(uid: String, shape: NodeShape, model: TimestampParser): TimestampParser = {
    val timestampParser = new TimestampParser(uid).setInputCol(model.getInputCol)
      .setUnit(model.getUnit)
    if (model.isDefined(model.hourOfDayCol)) {
      timestampParser.setHourOfDayCol(model.getHourOfDayCol)
    }
    if (model.isDefined(model.dayOfWeekCol)) {
      timestampParser
        .setDayOfWeekCol(model.getDayOfWeekCol)
    }
    if (model.isDefined(model.dayOfMonthCol)) {
      timestampParser
        .setDayOfMonthCol(model.getDayOfMonthCol)
    }
    if (model.isDefined(model.monthOfYearCol)) {
      timestampParser
        .setMonthOfYearCol(model.getMonthOfYearCol)
    }
    if (model.isDefined(model.daysToCurrentCol)) {
      timestampParser
        .setMonthOfYearCol(model.getDaysToCurrentCol)
    }
    timestampParser
  }

  override val Model: OpModel[SparkBundleContext, TimestampParser] = new OpModel[SparkBundleContext, TimestampParser] {
    override val klazz: Class[TimestampParser] = classOf[TimestampParser]

    override def opName: String = "timestamp_parser"

    override def store(model: Model, obj: TimestampParser)(implicit context: BundleContext[SparkBundleContext]): Model = {
      var model_ = model
      model_ = model_
        .withValue("inputCol", Value.string(obj.getInputCol))
      model_ = model_.withValue("unit", Value.int(obj.getUnit))
      if (obj.isDefined(obj.hourOfDayCol)) {
        model_ = model_.withValue("hourOfDayCol", Value.string(obj.getHourOfDayCol))
      }
      if (obj.isDefined(obj.dayOfWeekCol)) {
        model_ = model_.withValue("dayOfWeekCol", Value.string(obj.getDayOfWeekCol))
      }
      if (obj.isDefined(obj.dayOfMonthCol)) {
        model_ = model_.withValue("dayOfMonthCol", Value.string(obj.getDayOfMonthCol))
      }
      if (obj.isDefined(obj.monthOfYearCol)) {
        model_ = model_.withValue("monthOfYearCol", Value.string(obj.getMonthOfYearCol))
      }
      if (obj.isDefined(obj.daysToCurrentCol)) {
        model_ = model_.withValue("daysToCurrentCol", Value.string(obj.getDaysToCurrentCol))
      }
      model_
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): TimestampParser = {
      val inputCol = model.value("inputCol").getString
      val dateParser = new TimestampParser()
        .setInputCol(inputCol)
      dateParser.setUnit(model.value("unit").getInt)

      if (model.getValue("hourOfDayCol").nonEmpty) {
        dateParser.setHourOfDayCol(model.value("hourOfDayCol").getString)
      }
      if (model.getValue("dayOfWeekCol").nonEmpty) {
        dateParser.setDayOfWeekCol(model.value("dayOfWeekCol").getString)
      }
      if (model.getValue("dayOfMonthCol").nonEmpty) {
        dateParser.setDayOfMonthCol(model.value("dayOfMonthCol").getString)
      }
      if (model.getValue("monthOfYearCol").nonEmpty) {
        dateParser.setMonthOfYearCol(model.value("monthOfYearCol").getString)
      }
      if (model.getValue("daysToCurrentCol").nonEmpty) {
        dateParser.setDaysToCurrentCol(model.value("daysToCurrentCol").getString)
      }
      dateParser
    }
  }
}
