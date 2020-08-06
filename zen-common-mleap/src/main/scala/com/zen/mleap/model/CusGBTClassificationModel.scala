package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{StructType, TensorType}
import org.apache.spark.ml.classification.GBDTUitl.getGBDTFeatures
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.mllib.tree.model.{GradientBoostedTreesModel => OldGBTModel}

/** Class for a gradient boost classifier model.
 */
/**
 * @Author: xiongjun
 * @Date: 2020/7/20 11:53
 * @description
 * @reviewer
 */
case class CusGBTClassificationModel(gbdtModel: OldGBTModel,
                                     inputNumFeatures: Int,
                                     outputNumFeatures: Int) extends Model with Serializable {
  override def inputSchema: StructType = StructType("input" -> TensorType.Double(inputNumFeatures)).get

  override def outputSchema: StructType = StructType("output" -> TensorType.Double(outputNumFeatures)).get

  def apply(features: Vector): Vector = {
    val gbdtFeatures = getGBDTFeatures(gbdtModel, features)
    Vectors.dense(features.toArray ++ gbdtFeatures.toArray)
  }

}
