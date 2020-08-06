package com.zen.mleap.ops.spark

import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.CountEncoderModel

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class CountEncoderOp extends SimpleSparkOp[CountEncoderModel] {
  override def sparkInputs(obj: CountEncoderModel): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: CountEncoderModel): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCols))

  override def sparkLoad(uid: String, shape: NodeShape, model: CountEncoderModel): CountEncoderModel = {
    new CountEncoderModel(uid, model.dicts)
      .setHandleInvalid(model.getHandleInvalid)
      .setInputCols(model.getInputCols)
      .setOutputCols(model.getOutputCols)
      .setLabelCol(model.getLabelCol)
  }

  override val Model: OpModel[SparkBundleContext, CountEncoderModel] = new OpModel[SparkBundleContext, CountEncoderModel] {
    override val klazz: Class[CountEncoderModel] = classOf[CountEncoderModel]

    override def opName: String = "count_encoder"

    override def store(model: Model, obj: CountEncoderModel)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("keepInvalid", Value.boolean(obj.getHandleInvalid.equals("keep")))
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCols", Value.stringList(obj.getOutputCols))
        .withValue("dicts", Value.byteList(SerializationUtil.serialization(obj.dicts)))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): CountEncoderModel = {
      val dicts = SerializationUtil.deserialization(model.value("dicts").getByteList.toArray)
        .asInstanceOf[Array[Map[Int, Int]]]
      val handleInvalid = if (model.value("keepInvalid").getBoolean) {
        "keep"
      } else {
        "error"
      }
      new CountEncoderModel("", dicts)
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCols(model.value("outputCols").getStringList.toArray)
        .setHandleInvalid(handleInvalid)
    }
  }
}
