package com.zen.mleap.ops.spark


import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.SplitCounter

/**
 * @Author: morris
 * @Date: 2020/6/18 11:40
 * @description
 * @reviewer
 */
class SplitCounterOp extends SimpleSparkOp[SplitCounter]{
  override def sparkInputs(obj: SplitCounter): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: SplitCounter): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCols))

  override def sparkLoad(uid: String, shape: NodeShape, model: SplitCounter): SplitCounter = {
    new SplitCounter(uid)
      .setInputCols(model.getInputCols)
      .setOutputCols(model.getOutputCols)
      .setDelimiterArray(model.getDelimiterArray)

  }

  override val Model: OpModel[SparkBundleContext, SplitCounter] = new OpModel[SparkBundleContext, SplitCounter] {
    override val klazz: Class[SplitCounter] = classOf[SplitCounter]

    override def opName: String = "split_counter"

    override def store(model: Model, obj: SplitCounter)(implicit context: BundleContext[SparkBundleContext]): Model = {

      model
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCols", Value.stringList(obj.getOutputCols))
        .withValue("delimiterArray", Value.stringList(obj.getDelimiterArray))

    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): SplitCounter = {
      new SplitCounter("")
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCols(model.value("outputCols").getStringList.toArray)
        .setDelimiterArray(model.value("delimiterArray").getStringList.toArray)
    }
  }
}
