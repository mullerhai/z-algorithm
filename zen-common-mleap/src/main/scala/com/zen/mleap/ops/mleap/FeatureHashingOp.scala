package com.zen.mleap.ops.mleap

import com.zen.mleap.model.FeatureHashingModel
import com.zen.mleap.transformer.FeatureHashing
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:20
 * @description
 * @reviewer
 */
class FeatureHashingOp extends MleapOp[FeatureHashing, FeatureHashingModel] {
  override val Model: OpModel[MleapContext, FeatureHashingModel] = new OpModel[MleapContext, FeatureHashingModel] {
    override val klazz: Class[FeatureHashingModel] = classOf[FeatureHashingModel]

    override def opName: String = "feature_hashing"

    override def store(model: Model, obj: FeatureHashingModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("single", Value.boolean(obj.single))
        .withValue("numOfBucket", Value.int(obj.numOfBucket))
        .withValue("delimiter", Value.string(obj.delimiter))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): FeatureHashingModel = {
      FeatureHashingModel(model.value("numOfBucket").getInt,
        model.value("single").getBoolean, model.value("delimiter").getString)
    }
  }

  override def model(node: FeatureHashing): FeatureHashingModel = node.model
}
