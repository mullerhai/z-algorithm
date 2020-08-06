package com.zen.mleap.ops.mleap

import com.zen.mleap.model.DateParserModel
import com.zen.mleap.transformer.DateParser
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:20
 * @description
 * @reviewer
 */
class DateParserOp extends MleapOp[DateParser, DateParserModel] {
  override val Model: OpModel[MleapContext, DateParserModel] = new OpModel[MleapContext, DateParserModel] {
    override val klazz: Class[DateParserModel] = classOf[DateParserModel]

    override def opName: String = "date_parser"

    override def store(model: Model, obj: DateParserModel)(implicit context: BundleContext[MleapContext]): Model = {
      var model_ = model
      model_ = model_.withValue("format", Value.string(obj.format))
        .withValue("inputCol", Value.string(obj.inputCol))
      if (obj.hourOfDayCol.nonEmpty) {
        model_ = model_.withValue("hourOfDayCol", Value.string(obj.hourOfDayCol))
      }
      if (obj.dayOfWeekCol.nonEmpty) {
        model_ = model_.withValue("dayOfWeekCol", Value.string(obj.dayOfWeekCol))
      }
      if (obj.dayOfMonthCol.nonEmpty) {
        model_ = model_.withValue("dayOfMonthCol", Value.string(obj.dayOfMonthCol))
      }
      if (obj.monthOfYearCol.nonEmpty) {
        model_ = model_.withValue("monthOfYearCol", Value.string(obj.monthOfYearCol))
      }
      if (obj.daysToCurrentCol.nonEmpty) {
        model_ = model_.withValue("daysToCurrentCol", Value.string(obj.daysToCurrentCol))
      }
      model_
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): DateParserModel = {
      val hourOfDayCol = if (model.getValue("hourOfDayCol").nonEmpty) {
        model.value("hourOfDayCol").getString
      } else {
        null
      }
      val dayOfWeekCol = if (model.getValue("dayOfWeekCol").nonEmpty) {
        model.value("dayOfWeekCol").getString
      } else {
        null
      }
      val dayOfMonthCol = if (model.getValue("dayOfMonthCol").nonEmpty) {
        model.value("dayOfMonthCol").getString
      } else {
        null
      }
      val monthOfYearCol = if (model.getValue("monthOfYearCol").nonEmpty) {
        model.value("monthOfYearCol").getString
      } else {
        null
      }
      val daysToCurrentCol = if (model.getValue("daysToCurrentCol").nonEmpty) {
        model.value("daysToCurrentCol").getString
      } else {
        null
      }
      DateParserModel(model.value("inputCol").getString, model.value("format").getString,
        dayOfWeekCol, dayOfMonthCol, monthOfYearCol, hourOfDayCol, daysToCurrentCol)
    }
  }

  override def model(node: DateParser): DateParserModel = node.model
}
