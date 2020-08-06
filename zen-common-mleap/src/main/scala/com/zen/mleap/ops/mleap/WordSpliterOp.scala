package com.zen.mleap.ops.mleap

import com.zen.mleap.model.WordSpliterModel
import com.zen.mleap.transformer.WordSpliter
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl._
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: xiongjun
 * @Date: 2020/6/2 17:34
 * @description
 * @reviewer
 */
class WordSpliterOp extends MleapOp[WordSpliter, WordSpliterModel] {
  override val Model: OpModel[MleapContext, WordSpliterModel] = new OpModel[MleapContext, WordSpliterModel] {
    override val klazz: Class[WordSpliterModel] = classOf[WordSpliterModel]

    override def opName: String = "word_spliter"

    override def store(model: Model, obj: WordSpliterModel)(implicit context: BundleContext[MleapContext]): Model = {
      model.withValue("delimiter", Value.string(obj.delimiter)).withValue("toType", Value.string(obj.toType))
      //        .withValue("input_shapes", Value.dataShape(obj.inputShape))

    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): WordSpliterModel = {
      WordSpliterModel(model.value("delimiter").getString, model.value("toType").getString /*,
        model.value("input_shapes").getDataShape*/)
    }
  }

  override def model(node: WordSpliter): WordSpliterModel = node.model
}
