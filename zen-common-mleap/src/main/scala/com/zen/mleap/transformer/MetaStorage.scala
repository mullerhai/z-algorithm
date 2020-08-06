package com.zen.mleap.transformer

import com.zen.mleap.model.MetaStorageModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.{FrameBuilder, Transformer}

import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/2 17:56
 * @description
 * @reviewer
 */
case class MetaStorage(override val uid: String = Transformer.uniqueName("meta_storage"),
                       override val shape: NodeShape,
                       override val model: MetaStorageModel) extends Transformer {
  override def transform[FB <: FrameBuilder[FB]](builder: FB): Try[FB] = {
    Try(builder)
  }
}
