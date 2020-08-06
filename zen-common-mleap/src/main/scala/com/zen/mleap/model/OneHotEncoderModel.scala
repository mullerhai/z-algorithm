package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType, TensorType}
import org.apache.spark.SparkException
import org.apache.spark.ml.linalg.{Vector, Vectors}

/**
 * @Author: morris
 * @Date: 2020/6/22 11:42
 * @description
 * @reviewer
 */
case class OneHotEncoderModel(
                               inputCols: Array[String],
                               outputCols: Array[String],
                               handleInvalid: String,
                               dropLast: Boolean,
                               categorySizes: Array[Int]) extends Model {

  val KEEP_INVALID: String = "keep"

  override def inputSchema: StructType = {
    val structFields = inputCols.indices.map(idx => {
      StructField(s"input$idx", ScalarType.Double)
    })
    StructType(structFields).get
  }

  override def outputSchema: StructType = {
    val configedCategorySizes = getConfigedCategorySizes
    val structFields = outputCols.indices.map(idx => {
      StructField(s"output$idx", TensorType.Double(configedCategorySizes(idx)))
    })
    StructType(structFields).get
  }

  def encoder(label: Double, colIdx: Int): Vector = {

    val configedCategorySizes = getConfigedCategorySizes
    val keepInvalid = handleInvalid == KEEP_INVALID //无效数据处理
    val localCategorySizes = configedCategorySizes(colIdx) //分类大小
    val idx = if (label >= 0 && label < localCategorySizes) {
      label
    } else {
      if (keepInvalid) {
        0
      } else {
        if (label < 0) {
          throw new SparkException(s"Negative value: $label. Input can't be negative. " +
            s"To handle invalid values, set Param handleInvalid to " +
            s"$KEEP_INVALID")
        } else {
          throw new SparkException(s"Unseen value: $label. To handle unseen values, " +
            s"set Param handleInvalid to $KEEP_INVALID.")
        }
      }
    }

    if (idx < localCategorySizes) {
      Vectors.sparse(localCategorySizes, Array(idx.toInt), Array(1.0))
    } else {
      Vectors.sparse(localCategorySizes, Array.empty[Int], Array.empty[Double])
    }


  }

  def apply(values: Array[Double]): Array[Vector] = {
    values.zipWithIndex.map {
      case (label: Double, colIdx: Int) ⇒ encoder(label, colIdx)
    }
  }

  private def getConfigedCategorySizes: Array[Int] = {

//    val keepInvalid = handleInvalid == KEEP_INVALID
    /*if (!dropLast && keepInvalid) {
      // When `handleInvalid` is "keep", an extra category is added as last category
      // for invalid data.
      categorySizes.map(_ + 1)
    } else if (dropLast && !keepInvalid) {
      // When `dropLast` is true, the last category is removed.
      categorySizes.map(_ - 1)
    } else {
      // When `dropLast` is true and `handleInvalid` is "keep", the extra category for invalid
      // data is removed. Thus, it is the same as the plain number of categories.
      categorySizes
    }*/
    categorySizes
  }
}
