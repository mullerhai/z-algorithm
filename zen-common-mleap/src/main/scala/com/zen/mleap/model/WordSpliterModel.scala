package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types._

/**
 * @Author: xiongjun
 * @Date: 2020/6/2 13:16
 * @description
 * @reviewer
 */
case class WordSpliterModel(delimiter: String, toType: String /*, inputShape: DataShape*/) extends Model {
  //  assert(inputShape.isScalar, "Must provide a scalar shape")

  def apply(value: String): Any = {
    toType match {
      case "String" => value.split(delimiter).mkString(" ")
      case "Array" => value.split(delimiter).toSeq
    }
  }

  override def inputSchema: StructType = {
    //    StructType("input" -> DataType(BasicType.String, inputShape).setNullable(!inputShape.isScalar)).get
    StructType("input" -> ScalarType.String).get
  }

  override def outputSchema: StructType = {
    toType match {
      case "String" => StructType("output" -> ScalarType.String).get
      case "Array" => StructType("output" -> ListType(BasicType.String)).get
    }
  }
}
