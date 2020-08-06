package com.zen.mleap.ops.spark

import com.zen.spark.transformer.DateParser
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
class DateParserOp extends SimpleSparkOp[DateParser] {
  override def sparkInputs(obj: DateParser): Seq[ParamSpec] = Seq("input" -> obj.inputCol)

  override def sparkOutputs(obj: DateParser): Seq[ParamSpec] = {
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

  override def sparkLoad(uid: String, shape: NodeShape, model: DateParser): DateParser = {
    val dateParser = new DateParser(uid).setInputCol(model.getInputCol)
      .setFormat(model.getFormat)
    if (model.isDefined(model.hourOfDayCol)) {
      dateParser.setHourOfDayCol(model.getHourOfDayCol)
    }
    if (model.isDefined(model.dayOfWeekCol)) {
      dateParser
        .setDayOfWeekCol(model.getDayOfWeekCol)
    }
    if (model.isDefined(model.dayOfMonthCol)) {
      dateParser
        .setDayOfMonthCol(model.getDayOfMonthCol)
    }
    if (model.isDefined(model.monthOfYearCol)) {
      dateParser
        .setMonthOfYearCol(model.getMonthOfYearCol)
    }
    if (model.isDefined(model.daysToCurrentCol)) {
      dateParser
        .setMonthOfYearCol(model.getDaysToCurrentCol)
    }
    dateParser
  }

  override val Model: OpModel[SparkBundleContext, DateParser] = new OpModel[SparkBundleContext, DateParser] {
    override val klazz: Class[DateParser] = classOf[DateParser]

    override def opName: String = "date_parser"

    override def store(model: Model, obj: DateParser)(implicit context: BundleContext[SparkBundleContext]): Model = {
      var model_ = model
      model_ = model_
        .withValue("inputCol", Value.string(obj.getInputCol))
        .withValue("format", Value.string(obj.getFormat))

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

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): DateParser = {
      val inputCol = model.value("inputCol").getString
      val dateParser = new DateParser()
        .setInputCol(inputCol).setFormat(model.value("format").getString)

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
