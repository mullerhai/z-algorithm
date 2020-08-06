package com.zen.mleap.ops.mleap

import com.zen.mleap.model.MathCalModel
import com.zen.mleap.transformer.MathCal
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
class MathCalOp extends MleapOp[MathCal, MathCalModel] {
  override val Model: OpModel[MleapContext, MathCalModel] = new OpModel[MleapContext, MathCalModel] {
    override val klazz: Class[MathCalModel] = classOf[MathCalModel]

    override def opName: String = "math_cal"

    override def store(model: Model, obj: MathCalModel)(implicit context: BundleContext[MleapContext]): Model = {
      model.withValue("operation", Value.string(obj.operation))

    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): MathCalModel = {
      MathCalModel(model.value("operation").getString)
    }
  }

  override def model(node: MathCal): MathCalModel = node.model
}
