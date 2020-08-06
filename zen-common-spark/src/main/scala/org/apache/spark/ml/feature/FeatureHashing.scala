package org.apache.spark.ml.feature

import java.util

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.linalg.{VectorUDT, Vectors}
import org.apache.spark.ml.param.shared.{HasInputCol, HasOutputCol}
import org.apache.spark.ml.param.{BooleanParam, IntParam, Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable, SchemaUtils}
import org.apache.spark.mllib.feature.HashingTF.seed
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.unsafe.hash.Murmur3_x86_32.hashUnsafeBytes
import org.apache.spark.unsafe.types.UTF8String

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 10:08
 * @description 对大规模离散特征进行特征哈希编码，可以是onehot或者是multi hot
 * @reviewer
 */
class FeatureHashing(override val uid: String) extends Transformer
  with DefaultParamsWritable with HasInputCol with HasOutputCol {

  def this() = this(Identifiable.randomUID("feature_hashing"))

  def setInputCol(value: String): this.type = set(inputCol, value)

  def setOutputCol(value: String): this.type = set(outputCol, value)

  /**
   * 分桶数强制要求为2的幂，可以略微提升性能
   */
  final val numOfBucket: IntParam = new IntParam(this, "numOfBucket", "number of bucket,must be power of 2",
    (value: Int) => (value & (value - 1)) == 0
  )

  def getNumOfBucket: Int = $(numOfBucket)

  def setNumOfBucket(value: Int): this.type = set(numOfBucket, value)


  final val single: BooleanParam = new BooleanParam(this, "single", "is single value")

  def isSingle: Boolean = $(single)

  def setSingle(value: Boolean): this.type = set(single, value)

  setDefault(single, true)

  /**
   * 如果是多值特征，则需要明确分割符
   */
  val delimiter = new Param[String](this, "delimiter", "delimiter for feature")

  def getDelimiter: String = $(delimiter)

  def setDelimiter(value: String): this.type = set(delimiter, value)

  setDefault(delimiter, "")

  override def transform(dataset: Dataset[_]): DataFrame = {
    transformSchema(dataset.schema)
    new util.HashMap()
    val hashUDF = udf { value: String => {
      val indices = if ($(single)) {
        val index = murmur3Hash(value) & ($(numOfBucket) - 1)
        Array(index)
      } else {
        value.split($(delimiter)).map(v => murmur3Hash(v)).map(_ & ($(numOfBucket) - 1)).sorted
      }
      val vectorValues = indices.map(_ => 1.0)
      Vectors.sparse($(numOfBucket), indices, vectorValues)
    }
    }
    dataset.withColumn($(outputCol), hashUDF(dataset($(inputCol))))
  }

  def murmur3Hash(term: String): Int = {
    val utf8 = UTF8String.fromString(term)
    hashUnsafeBytes(utf8.getBaseObject, utf8.getBaseOffset, utf8.numBytes(), seed)
  }


  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }

  protected def validateAndTransformSchema(schema: StructType): StructType = {
    val inputColName = $(inputCol)
    val outputColName = $(outputCol)
    require(schema.fieldNames.contains(inputColName), s"inputCol $inputColName doesn't exist")
    require(!schema.fieldNames.contains(outputColName), s"outputCol $outputColName alreay exist")
    SchemaUtils.checkColumnType(schema, inputColName, StringType)
    SchemaUtils.appendColumn(schema, StructField(outputColName, new VectorUDT, nullable = true))
  }

}

object FeatureHashing extends DefaultParamsReadable[FeatureHashing] {}
