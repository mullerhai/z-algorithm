package com.zen.mleap.transformer

import com.zen.mleap.model.ColSelectorModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.{FrameBuilder, Transformer}

import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/22 16:00
 * @description
 * @reviewer
 */
case class ColSelector(override val uid: String = Transformer.uniqueName("col_selector"),
                       override val shape: NodeShape, override val model: ColSelectorModel) extends Transformer {


  override def transform[FB <: FrameBuilder[FB]](builder: FB): Try[FB] = {
    Try(builder)
  }
}
