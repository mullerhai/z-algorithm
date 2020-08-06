package com.zen.mleap.transformer

import com.zen.mleap.model.FeatureHashingModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.{SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.UserDefinedFunction

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 11:22
 * @description
 * @reviewer
 */
case class FeatureHashing(override val uid: String = Transformer.uniqueName("word_spliter"),
                          override val shape: NodeShape,
                          override val model: FeatureHashingModel) extends SimpleTransformer {
  override val exec: UserDefinedFunction = (value: String) => model(value)
}
