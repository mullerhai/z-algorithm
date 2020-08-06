package com.zen.mleap.ops.spark

import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.classification.CusGBTClassificationModel
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel

/**
 * @Author: xiongjun
 * @Date: 2020/7/20 9:12
 * @description
 * @reviewer
 */
class CusGBTClassifierOp extends SimpleSparkOp[CusGBTClassificationModel] {
  override def sparkInputs(obj: CusGBTClassificationModel): Seq[ParamSpec] = Seq(ParamSpec("input", obj.gbtGeneratedFeaturesCol))

  override def sparkOutputs(obj: CusGBTClassificationModel): Seq[ParamSpec] = Seq(ParamSpec("output", obj.featuresCol))

  override def sparkLoad(uid: String, shape: NodeShape, model: CusGBTClassificationModel): CusGBTClassificationModel = {
    new CusGBTClassificationModel(uid, model.gbdtModel, model.numFeatures, model.outputNumFeatures)
      .setFeaturesCol(model.getFeaturesCol)
      .setGbtGeneratedFeaturesCol(model.getGbtGeneratedFeaturesCol)
  }

  override val Model: OpModel[SparkBundleContext, CusGBTClassificationModel] =
    new OpModel[SparkBundleContext, CusGBTClassificationModel] {

      override val klazz: Class[CusGBTClassificationModel] = classOf[CusGBTClassificationModel]

      override def opName: String = "cus_gbt_classifer"

      override def store(model: Model, obj: CusGBTClassificationModel)(implicit context: BundleContext[SparkBundleContext]): Model = {
        model
          .withValue("gbdt", Value.byteList(SerializationUtil.serialization(obj.gbdtModel)))
          .withValue("numFeatures", Value.int(obj.numFeatures))
          .withValue("outputNumFeatures", Value.int(obj.outputNumFeatures))
      }

      override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): CusGBTClassificationModel = {
        val gbdtModel = SerializationUtil.deserialization(model.value("gbdt").getByteList.toArray)
          .asInstanceOf[GradientBoostedTreesModel]

        new CusGBTClassificationModel("", gbdtModel,
          model.value("numFeatures").getInt,
          model.value("outputNumFeatures").getInt)
      }
    }
}
