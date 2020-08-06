package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructType, TensorType}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.unsafe.hash.Murmur3_x86_32.hashUnsafeBytes
import org.apache.spark.unsafe.types.UTF8String

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 11:10
 * @description
 * @reviewer
 */
case class FeatureHashingModel(numOfBucket: Int, single: Boolean, delimiter: String) extends Model {
  override def inputSchema: StructType = StructType("input" -> ScalarType.String).get

  override def outputSchema: StructType = StructType("output" -> TensorType.Double(numOfBucket)).get

  final val seed = 42

  def apply(value: String): Vector = {
    val indices = if (single) {
      val index = murmur3Hash(value) & (numOfBucket-1)
      Array(index)
    } else {
      value.split(delimiter).map(v => murmur3Hash(v)).map(_ & (numOfBucket-1)).sorted
    }
    val vectorValues = indices.map(_ => 1.0)
    Vectors.sparse(numOfBucket, indices, vectorValues)
  }

  def murmur3Hash(term: String): Int = {
    val utf8 = UTF8String.fromString(term)
    hashUnsafeBytes(utf8.getBaseObject, utf8.getBaseOffset, utf8.numBytes(), seed)
  }
}

