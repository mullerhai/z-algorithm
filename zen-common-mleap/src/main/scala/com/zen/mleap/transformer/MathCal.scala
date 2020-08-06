package com.zen.mleap.transformer

import com.zen.mleap.model.MathCalModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.{SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.UserDefinedFunction

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 11:22
 * @description
 * @reviewer
 */
case class MathCal(override val uid: String = Transformer.uniqueName("math_cal"),
                   override val shape: NodeShape,
                   override val model: MathCalModel) extends SimpleTransformer {

  override val exec: UserDefinedFunction = (value: Double) => model(value)

}
