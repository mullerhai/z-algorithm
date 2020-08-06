package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{BasicType, ListType, StructType}

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 11:34
 * @description
 * @reviewer
 */
case class NGramExtendModel(n: Int, blank: Boolean) extends Model {
  def apply(value: Seq[String]): Seq[String] = {
    blank match {
      case true =>
        value.iterator.sliding(n).withPartial(false).map(_.mkString(" ")).toSeq
      case false =>
        value.iterator.sliding(n).withPartial(false).map(_.mkString).toSeq
    }

  }

  override def inputSchema: StructType = StructType("input" -> ListType(BasicType.String)).get

  override def outputSchema: StructType = StructType("output" -> ListType(BasicType.String)).get
}
