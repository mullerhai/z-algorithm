package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.StructType

/**
 * @Author: xiongjun
 * @Date: 2020/6/22 15:59
 * @description
 * @reviewer
 */
case class ColSelectorModel(cols: Array[String]) extends Model {

  override def inputSchema: StructType = StructType.empty.get

  override def outputSchema: StructType = StructType.empty.get
}
