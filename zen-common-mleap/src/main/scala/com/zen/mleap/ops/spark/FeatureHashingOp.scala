package com.zen.mleap.ops.spark

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.FeatureHashing

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class FeatureHashingOp extends SimpleSparkOp[FeatureHashing] {
  override def sparkInputs(obj: FeatureHashing): Seq[ParamSpec] = Seq("input" -> obj.inputCol)

  override def sparkOutputs(obj: FeatureHashing): Seq[ParamSpec] = Seq("output" -> obj.outputCol)

  override def sparkLoad(uid: String, shape: NodeShape, model: FeatureHashing): FeatureHashing = {
    new FeatureHashing(uid).setInputCol(model.getInputCol)
      .setOutputCol(model.getOutputCol)
      .setSingle(model.isSingle)
      .setNumOfBucket(model.getNumOfBucket)
      .setDelimiter(model.getDelimiter)

  }

  override val Model: OpModel[SparkBundleContext, FeatureHashing] = new OpModel[SparkBundleContext, FeatureHashing] {
    override val klazz: Class[FeatureHashing] = classOf[FeatureHashing]

    override def opName: String = "feature_hashing"

    override def store(model: Model, obj: FeatureHashing)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("single", Value.boolean(obj.isSingle))
        .withValue("numOfBucket", Value.int(obj.getNumOfBucket))
        .withValue("delimiter", Value.string(obj.getDelimiter))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): FeatureHashing = {
      new FeatureHashing()
        .setSingle(model.value("single").getBoolean)
        .setDelimiter(model.value("delimiter").getString)
        .setNumOfBucket(model.value("numOfBucket").getInt)
    }
  }
}
