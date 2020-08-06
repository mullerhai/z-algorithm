package com.zen.mleap.ops.mleap

import com.zen.mleap.model.CusGBTClassificationModel
import com.zen.mleap.transformer.CusGBTClassifier
import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel

/**
 * @Author: xiongjun
 * @Date: 2020/7/20 10:20
 * @description
 * @reviewer
 */
class CusGBTClassifierOp extends MleapOp[CusGBTClassifier, CusGBTClassificationModel] {
  override val Model: OpModel[MleapContext, CusGBTClassificationModel] = new OpModel[MleapContext, CusGBTClassificationModel] {
    override val klazz: Class[CusGBTClassificationModel] = classOf[CusGBTClassificationModel]

    override def opName: String = "cus_gbt_classifer"

    override def store(model: Model, obj: CusGBTClassificationModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("gbdt", Value.byteList(SerializationUtil.serialization(obj.gbdtModel)))
        .withValue("numFeatures", Value.int(obj.inputNumFeatures))
        .withValue("outputNumFeatures", Value.int(obj.outputNumFeatures))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): CusGBTClassificationModel = {
      /**
       * inputCols:Array[String],
       * outputCols:Array[String],
       * keepInvalid:Boolean,
       * delimiter:String,
       * dict:Map[Int,Map[String,Int]]
       */
      CusGBTClassificationModel(
        SerializationUtil.deserialization(model.value("splitsArray").getByteList.toArray).asInstanceOf[GradientBoostedTreesModel],
        model.value("numFeatures").getInt,
        model.value("outputNumFeatures").getInt
      )
    }


  }

  override def model(node: CusGBTClassifier): CusGBTClassificationModel = node.model
}
