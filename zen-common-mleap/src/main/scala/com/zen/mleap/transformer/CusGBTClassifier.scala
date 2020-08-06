package com.zen.mleap.transformer

import com.zen.mleap.model.CusGBTClassificationModel
import ml.combust.mleap.core.types.NodeShape
import ml.combust.mleap.runtime.frame.SimpleTransformer
import ml.combust.mleap.runtime.function.UserDefinedFunction
import org.apache.spark.ml.linalg.Vector

/**
 * @Author: xiongjun
 * @Date: 2020/7/20 11:53
 * @description
 * @reviewer
 */
case class CusGBTClassifier(override val uid: String,
                            override val shape: NodeShape,
                            override val model: CusGBTClassificationModel) extends SimpleTransformer {
  override val exec: UserDefinedFunction = (value: Vector) => model(value)
}
