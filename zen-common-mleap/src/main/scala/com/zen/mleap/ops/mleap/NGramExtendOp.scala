package com.zen.mleap.ops.mleap

import com.zen.mleap.model.NGramExtendModel
import com.zen.mleap.transformer.NGramExtend
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 11:37
 * @description
 * @reviewer
 */
class NGramExtendOp extends MleapOp[NGramExtend, NGramExtendModel] {
  override val Model: OpModel[MleapContext, NGramExtendModel] = new OpModel[MleapContext, NGramExtendModel] {
    override val klazz: Class[NGramExtendModel] = classOf[NGramExtendModel]

    override def opName: String = "ngram_extend"

    override def store(model: Model, obj: NGramExtendModel)(implicit context: BundleContext[MleapContext]): Model = {
      model.withValue("n", Value.long(obj.n)).withValue("blank", Value.boolean(obj.blank))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): NGramExtendModel = {
      NGramExtendModel(n = model.value("n").getLong.toInt, blank = model.value("blank").getBoolean)
    }
  }

  override def model(node: NGramExtend): NGramExtendModel = node.model
}
