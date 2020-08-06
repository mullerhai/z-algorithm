package com.zen.mleap.ops.spark

import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.BucketizerExtendModel

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class BucketizerExtendOp extends SimpleSparkOp[BucketizerExtendModel] {
  override def sparkInputs(obj: BucketizerExtendModel): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: BucketizerExtendModel): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCols))

  override def sparkLoad(uid: String, shape: NodeShape, model: BucketizerExtendModel): BucketizerExtendModel = {
    new BucketizerExtendModel(uid, model.splitsArray)
      .setInputCols(model.getInputCols)
      .setOutputCols(model.getOutputCols)
      .setRule(model.getRule)
      .setNumOfBucket(model.getNumOfBucket)
  }

  override val Model: OpModel[SparkBundleContext, BucketizerExtendModel] = new OpModel[SparkBundleContext, BucketizerExtendModel] {
    override val klazz: Class[BucketizerExtendModel] = classOf[BucketizerExtendModel]

    override def opName: String = "bucketizer_extend"

    override def store(model: Model, obj: BucketizerExtendModel)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("rule", Value.string(obj.getRule))
        .withValue("numOfBucket", Value.int(obj.getNumOfBucket))
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCols", Value.stringList(obj.getOutputCols))
        .withValue("splitsArray", Value.byteList(SerializationUtil.serialization(obj.splitsArray)))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): BucketizerExtendModel = {
      val splitsArray = SerializationUtil.deserialization(model.value("splitsArray").getByteList.toArray)
        .asInstanceOf[Array[Array[Double]]]

      new BucketizerExtendModel("", splitsArray)
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCols(model.value("outputCols").getStringList.toArray)
        .setRule(model.value("rule").getString)
        .setNumOfBucket(model.value("numOfBucket").getInt)
    }
  }
}
