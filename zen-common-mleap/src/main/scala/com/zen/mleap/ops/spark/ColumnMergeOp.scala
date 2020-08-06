package com.zen.mleap.ops.spark

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.ColumnMerge

/**
 * @Author: morris
 * @Date: 2020/6/18 11:40
 * @description
 * @reviewer
 */
class ColumnMergeOp extends SimpleSparkOp[ColumnMerge]{
  override def sparkInputs(obj: ColumnMerge): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: ColumnMerge): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCol))

  override def sparkLoad(uid: String, shape: NodeShape, model: ColumnMerge): ColumnMerge = {
    new ColumnMerge(uid)
      .setInputCols(model.getInputCols)
      .setOutputCol(model.getOutputCol)
      .setDelimiter(model.getDelimiter)

  }

  override val Model: OpModel[SparkBundleContext, ColumnMerge] = new OpModel[SparkBundleContext, ColumnMerge] {
    override val klazz: Class[ColumnMerge] = classOf[ColumnMerge]

    override def opName: String = "col_merge"

    override def store(model: Model, obj: ColumnMerge)(implicit context: BundleContext[SparkBundleContext]): Model = {

      model
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCol", Value.string(obj.getOutputCol))
        .withValue("delimiter", Value.string(obj.getDelimiter))

    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): ColumnMerge = {
      new ColumnMerge("")
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCol(model.value("outputCol").getString)
        .setDelimiter(model.value("delimiter").getString)
    }
  }
}
