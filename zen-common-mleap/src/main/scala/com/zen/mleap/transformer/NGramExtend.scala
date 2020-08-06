package com.zen.mleap.transformer

import com.zen.mleap.model.NGramExtendModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.{SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.UserDefinedFunction

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 11:35
 * @description
 * @reviewer
 */
case class NGramExtend(override val uid: String = Transformer.uniqueName("ngram"),
                       override val shape: NodeShape,
                       override val model: NGramExtendModel) extends SimpleTransformer {
  override val exec: UserDefinedFunction = (value: Seq[String]) => model(value)
}
