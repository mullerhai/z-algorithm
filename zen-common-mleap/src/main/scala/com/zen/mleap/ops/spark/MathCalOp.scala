package com.zen.mleap.ops.spark

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.MathCal

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class MathCalOp extends SimpleSparkOp[MathCal] {
  override def sparkInputs(obj: MathCal): Seq[ParamSpec] = Seq("input" -> obj.inputCol)

  override def sparkOutputs(obj: MathCal): Seq[ParamSpec] = Seq("output" -> obj.outputCol)

  override def sparkLoad(uid: String, shape: NodeShape, model: MathCal): MathCal = {
    new MathCal(uid).setInputCol(model.getInputCol)
      .setOutputCol(model.getOutputCol)
      .setOperation(model.getOperation)

  }

  override val Model: OpModel[SparkBundleContext, MathCal] = new OpModel[SparkBundleContext, MathCal] {
    override val klazz: Class[MathCal] = classOf[MathCal]

    override def opName: String = "math_cal"

    override def store(model: Model, obj: MathCal)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("operation", Value.string(obj.getOperation))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): MathCal = {
      new MathCal()
        .setOperation(model.value("operation").getString)
    }
  }
}
