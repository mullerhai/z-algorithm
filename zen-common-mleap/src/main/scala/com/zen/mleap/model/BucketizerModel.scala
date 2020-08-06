package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}
import org.apache.spark.SparkException
import org.apache.spark.ml.linalg.{Vector, Vectors}
import java.{util => ju}

/**
 * @Author: morris
 * @Date: 2020/6/22 11:42
 * @description
 * @reviewer
 */
case class BucketizerModel(inputCols: Array[String],
                           outputCols: Array[String],
                           handleInvalid: String,
                           splitsArray: Array[Array[Double]]) extends Model{

  val KEEP_INVALID: String = "keep"

  override def inputSchema: StructType = {
    val structFields = inputCols.indices.map(idx => {
      StructField(s"input$idx", ScalarType.Double)
    })
    StructType(structFields).get
  }

  override def outputSchema: StructType = {
    val structFields = outputCols.indices.map(idx => {
      StructField(s"output$idx", ScalarType.Double)
    })
    StructType(structFields).get
  }

  def binarySearchForBuckets(
                                               splits: Array[Double],
                                               feature: Double,
                                               keepInvalid: Boolean): Double = {
    if (feature.isNaN) {
      if (keepInvalid) {
        splits.length - 1
      } else {
        throw new SparkException("Bucketizer encountered NaN value. To handle or skip NaNs," +
          " try setting Bucketizer.handleInvalid.")
      }
    } else if (feature == splits.last) {
      splits.length - 2
    } else {
      val idx = ju.Arrays.binarySearch(splits, feature)
      if (idx >= 0) {
        idx
      } else {
        val insertPos = -idx - 1
        if (insertPos == 0 || insertPos == splits.length) {
          throw new SparkException(s"Feature value $feature out of Bucketizer bounds" +
            s" [${splits.head}, ${splits.last}].  Check your features, or loosen " +
            s"the lower/upper bound constraints.")
        } else {
          insertPos - 1
        }
      }
    }
  }

  def apply(values: Array[Double]): Array[Double] = {
    values.zipWithIndex.map {
      case(label:Double,colIdx:Int) ⇒ binarySearchForBuckets(splitsArray(colIdx),label,handleInvalid.equals(KEEP_INVALID))
    }
  }
}
