package com.zen.mleap.ops.spark

import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.CusBucketizer

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class BucketizerOp extends SimpleSparkOp[CusBucketizer] {
  override def sparkInputs(obj: CusBucketizer): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: CusBucketizer): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCols))

  override def sparkLoad(uid: String, shape: NodeShape, model: CusBucketizer): CusBucketizer = {
    new CusBucketizer(uid)
      .setHandleInvalid(model.getHandleInvalid)
      .setInputCols(model.getInputCols)
      .setOutputCols(model.getOutputCols)
      .setSplitsArray(model.getSplitsArray)

  }

  override val Model: OpModel[SparkBundleContext, CusBucketizer] = new OpModel[SparkBundleContext, CusBucketizer] {
    override val klazz: Class[CusBucketizer] = classOf[CusBucketizer]

    override def opName: String = "Bucketizer_encoder"

    override def store(model: Model, obj: CusBucketizer)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("handleInvalid", Value.string(obj.getHandleInvalid))
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCols", Value.stringList(obj.getOutputCols))
        .withValue("splitsArray", Value.byteList(SerializationUtil.serialization(obj.getSplitsArray)))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): CusBucketizer = {

      new CusBucketizer("")
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCols(model.value("outputCols").getStringList.toArray)
        .setHandleInvalid(model.value("handleInvalid").getString)
        .setSplitsArray(SerializationUtil.deserialization(model.value("splitsArray").getByteList.toArray)
          .asInstanceOf[Array[Array[Double]]])
    }
  }
}
