package com.zen.mleap.ops.mleap

import ml.combust.bundle.op.OpModel
import com.zen.mleap.transformer.SplitCounter
import com.zen.mleap.model.SplitCounterModel
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: morris
 * @Date: 2020/6/18 11:53
 * @description
 * @reviewer
 */
class SplitCounterOp extends MleapOp[SplitCounter,SplitCounterModel]{
  override val Model: OpModel[MleapContext, SplitCounterModel] = new OpModel[MleapContext, SplitCounterModel] {
    override val klazz: Class[SplitCounterModel] = classOf[SplitCounterModel]

    override def opName: String = "split_counter"

    override def store(model: Model, obj: SplitCounterModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCols", Value.stringList(obj.outputCols))
        .withValue("delimiterArray", Value.stringList(obj.delimiterArray))

    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): SplitCounterModel = {
      SplitCounterModel(model.value("inputCols").getStringList.toArray,
        model.value("outputCols").getStringList.toArray,
        model.value("delimiterArray").getStringList.toArray
      )
    }
  }

  override def model(node: SplitCounter): SplitCounterModel = node.model
}
