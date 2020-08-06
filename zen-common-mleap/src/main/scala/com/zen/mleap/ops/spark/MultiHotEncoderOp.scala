package com.zen.mleap.ops.spark

import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.MultiHotEncoderModel

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class MultiHotEncoderOp extends SimpleSparkOp[MultiHotEncoderModel] {
  override def sparkInputs(obj: MultiHotEncoderModel): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: MultiHotEncoderModel): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCols))

  override def sparkLoad(uid: String, shape: NodeShape, model: MultiHotEncoderModel): MultiHotEncoderModel = {
    new MultiHotEncoderModel(uid, model.dict)
      .setHandleInvalid(model.getHandleInvalid)
      .setInputCols(model.getInputCols)
      .setOutputCols(model.getOutputCols)
      .setDelimiter(model.getDelimiter)
  }

  override val Model: OpModel[SparkBundleContext, MultiHotEncoderModel] = new OpModel[SparkBundleContext, MultiHotEncoderModel] {
    override val klazz: Class[MultiHotEncoderModel] = classOf[MultiHotEncoderModel]

    override def opName: String = "multi_hot_encoder"

    override def store(model: Model, obj: MultiHotEncoderModel)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("keepInvalid", Value.boolean(obj.getHandleInvalid.equals("keep")))
        .withValue("delimiter", Value.string(obj.getDelimiter))
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCols", Value.stringList(obj.getOutputCols))
        .withValue("dict", Value.byteList(SerializationUtil.serialization(obj.dict)))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): MultiHotEncoderModel = {
      val dict = SerializationUtil.deserialization(model.value("dict").getByteList.toArray)
        .asInstanceOf[Map[Int, Map[String, Int]]]
      val handleInvalid = if (model.value("keepInvalid").getBoolean) {
        "keep"
      } else {
        "error"
      }
      new MultiHotEncoderModel("", dict)
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCols(model.value("outputCols").getStringList.toArray)
        .setHandleInvalid(handleInvalid)
        .setDelimiter(model.value("delimiter").getString)
    }
  }
}
