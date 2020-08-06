package com.zen.mleap.ops.mleap
import com.zen.mleap.model.ColSelectorModel
import com.zen.mleap.transformer.ColSelector
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext
/**
 * @Author: xiongjun
 * @Date: 2020/6/22 16:01
 * @description
 * @reviewer
 */
class ColSelectorOp extends MleapOp[ColSelector, ColSelectorModel] {
  override val Model: OpModel[MleapContext, ColSelectorModel] = new OpModel[MleapContext, ColSelectorModel] {
    override val klazz: Class[ColSelectorModel] = classOf[ColSelectorModel]

    override def opName: String = "col_selector"

    override def store(model: Model, obj: ColSelectorModel)(implicit context: BundleContext[MleapContext]): Model = {
      model.withValue("cols", Value.stringList(obj.cols))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): ColSelectorModel = {
      ColSelectorModel(model.value("cols").getStringList.toArray)
    }
  }

  override def model(node: ColSelector): ColSelectorModel = node.model
}

