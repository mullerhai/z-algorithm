package com.zen.mleap.model

import com.zen.spark.transformer.ModelType.ModelType
import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.StructType

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 16:51
 * @description
 * @reviewer
 */
case class MetaStorageModel(modelType: ModelType, params: Map[String, Any], fields: Array[String]) extends Model {
  override def inputSchema: StructType = StructType.empty.get

  override def outputSchema: StructType = StructType.empty.get
}
