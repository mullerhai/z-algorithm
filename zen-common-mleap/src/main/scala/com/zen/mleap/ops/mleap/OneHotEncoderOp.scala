package com.zen.mleap.ops.mleap

import com.zen.mleap.model.OneHotEncoderModel
import com.zen.mleap.transformer.OneHotEncoder
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
class OneHotEncoderOp extends MleapOp[OneHotEncoder, OneHotEncoderModel] {
  override val Model: OpModel[MleapContext, OneHotEncoderModel] = new OpModel[MleapContext, OneHotEncoderModel] {
    override val klazz: Class[OneHotEncoderModel] = classOf[OneHotEncoderModel]

    override def opName: String = "one_hot_encoder"

    override def store(model: Model, obj: OneHotEncoderModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCols", Value.stringList(obj.outputCols))
        .withValue("handleInvalid", Value.string(obj.handleInvalid))
        .withValue("dropLast", Value.boolean(obj.dropLast))
        .withValue("categorySizes", Value.intList(obj.categorySizes))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): OneHotEncoderModel = {

      OneHotEncoderModel(
        model.value("inputCols").getStringList.toArray,
        model.value("outputCols").getStringList.toArray,
        model.value("handleInvalid").getString,
        model.value("dropLast").getBoolean,
        model.value("categorySizes").getIntList.toArray
      )
    }
  }

  override def model(node: OneHotEncoder): OneHotEncoderModel = node.model
}
