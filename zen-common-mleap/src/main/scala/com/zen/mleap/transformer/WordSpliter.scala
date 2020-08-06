package com.zen.mleap.transformer

import com.zen.mleap.model.WordSpliterModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.{SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.UserDefinedFunction

/**
 * @Author: xiongjun
 * @Date: 2020/6/2 13:30
 * @description
 * @reviewer
 */
case class WordSpliter(override val uid: String = Transformer.uniqueName("word_spliter"),
                       override val shape: NodeShape,
                       override val model: WordSpliterModel) extends SimpleTransformer {
  override val exec: UserDefinedFunction = if (model.toType.equals("Array")) {
    doc: String => model(doc).asInstanceOf[Seq[String]]
  } else {
    doc: String => model(doc).asInstanceOf[String]
  }
}
