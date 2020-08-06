package com.zen.mleap.ops.spark

import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.NormalizationScalerModel
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel

/**
 * @Author: morris
 * @Date: 2020/6/15 17:31
 * @description
 * @reviewer
 */
class NormalizationOp extends SimpleSparkOp[NormalizationScalerModel]{
  override def sparkInputs(obj: NormalizationScalerModel): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: NormalizationScalerModel): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCols))

  override def sparkLoad(uid: String, shape: NodeShape, model: NormalizationScalerModel): NormalizationScalerModel = {
    new NormalizationScalerModel(uid,model.dict)
      .setInputCols(model.getInputCols)
      .setOutputCols(model.getOutputCols)
      .setMin(model.getMin)
      .setMax(model.getMax)
      .setAlgorithmType(model.getAlgorithmType)

  }

  override val Model: OpModel[SparkBundleContext, NormalizationScalerModel] = new OpModel[SparkBundleContext, NormalizationScalerModel] {
    override val klazz: Class[NormalizationScalerModel] = classOf[NormalizationScalerModel]

    override def opName: String = "normalization_scaler"

    override def store(model: Model, obj: NormalizationScalerModel)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model
        .withValue("inputCols", Value.stringList(obj.getInputCols))
        .withValue("outputCols", Value.stringList(obj.getOutputCols))
        .withValue("dict", Value.byteList(SerializationUtil.serialization(obj.dict)))
        .withValue("min",Value.doubleList(obj.getMin))
        .withValue("max",Value.doubleList(obj.getMax))
        .withValue("algorithmType",Value.int(obj.getAlgorithmType))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): NormalizationScalerModel = {
      val dict = SerializationUtil.deserialization(model.value("dict").getByteList.toArray)
        .asInstanceOf[Map[Int, Seq[Double]]]
      new NormalizationScalerModel("",dict)
        .setAlgorithmType(model.getValue("algorithmType").get.getInt)
        .setInputCols(model.value("inputCols").getStringList.toArray)
        .setOutputCols(model.value("outputCols").getStringList.toArray)
        .setMax(model.value("max").getDoubleList.toArray)
        .setMin(model.value("min").getDoubleList.toArray)

    }
  }
}
