package com.zen.mleap.ops.spark

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.CusOneHotEncoderModel

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class OneHotEncoderOp extends SimpleSparkOp[CusOneHotEncoderModel] {
  override def sparkInputs(obj: CusOneHotEncoderModel): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: CusOneHotEncoderModel): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCols))

  override def sparkLoad(uid: String, shape: NodeShape, model: CusOneHotEncoderModel): CusOneHotEncoderModel = {
    new CusOneHotEncoderModel(uid,model.categorySizes)
      .setHandleInvalid(model.getHandleInvalid)
      .setInputCols(model.getInputCols)
      .setOutputCols(model.getOutputCols)
      .setDropLast(model.getDropLast)

  }

  override val Model: OpModel[SparkBundleContext, CusOneHotEncoderModel] = new OpModel[SparkBundleContext, CusOneHotEncoderModel] {
    override val klazz: Class[CusOneHotEncoderModel] = classOf[CusOneHotEncoderModel]

    override def opName: String = "one_hot_encoder"

    override def store(model: Model, obj: CusOneHotEncoderModel)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("handleInvalid", Value.string(obj.getHandleInvalid))
        .withValue("dropLast", Value.boolean(obj.getDropLast))
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCols", Value.stringList(obj.getOutputCols))
        .withValue("categorySizes", Value.intList(obj.categorySizes))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): CusOneHotEncoderModel = {
      val categorySizes = model.value("categorySizes").getIntList.toArray

      new CusOneHotEncoderModel("", categorySizes)
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCols(model.value("outputCols").getStringList.toArray)
        .setHandleInvalid(model.value("handleInvalid").getString)
        .setDropLast(model.value("dropLast").getBoolean)
    }
  }
}
