package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructType}
import org.apache.spark.ml.feature.Operation

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 11:10
 * @description
 * @reviewer
 */
case class MathCalModel(operation: String) extends Model {
  override def inputSchema: StructType = StructType("input" -> ScalarType.Double).get

  override def outputSchema: StructType = StructType("output" -> ScalarType.Double).get

  def apply(value: Double): Double = {
    operation match {
      case Operation.log10 =>
        if(value<=1){
          0.0
        }else Math.log10(value)
      case Operation.loge =>
        if (value<=1){
          0.0
        }else Math.log(value)
      case Operation.sin =>
        Math.sin(value)
      case Operation.cos =>
        Math.cos(value)
      case Operation.sigmod =>
        1 / (1 + Math.exp(-value))
      case _ =>
        throw new IllegalArgumentException(s"not support operation $operation")
    }
  }
}

