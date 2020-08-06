package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType, TensorType}
import org.apache.spark.SparkException
import org.apache.spark.ml.linalg.{Vector, Vectors}

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 17:10
 * @description
 * @reviewer
 */
case class MultiHotEncoderModel(inputCols: Array[String],
                                outputCols: Array[String],
                                keepInvalid: Boolean,
                                delimiter: String,
                                dict: Map[Int, Map[String, Int]]) extends Model {
  override def inputSchema: StructType = {
    val inputFields = inputCols.indices.map(idx => {
      StructField(s"input$idx", ScalarType.String)
    })
    StructType(inputFields).get
  }

  override def outputSchema: StructType = {
    val outputSizes = dict.map(idx2map => {
      if (keepInvalid) {
        idx2map._2.size + 1
      } else {
        idx2map._2.size
      }
    }).toArray
    val outputFields = outputCols.indices.map(idx => {
      StructField(s"output$idx", TensorType.Double(outputSizes(idx)))
    })
    StructType(outputFields).get
  }

  def apply(values: Array[String]): Array[Vector] = {
    values.zipWithIndex.map {
      case (label: String, colIdx: Int) ⇒ encoder(label, colIdx)
    }
  }

  private def encoder(label: String, colIdx: Int): Vector = {
    val valueAndIdx = dict(colIdx)
    val values = label.split(delimiter).distinct
    val indices = values.map(value => {
      if (valueAndIdx.contains(value)) {
        valueAndIdx(value)
      } else {
        if (keepInvalid) {
          valueAndIdx.size
        } else {
          throw new SparkException(s"Unseen value: $value. To handle unseen values, " +
            s"set Param handleInvalid to ${
              if (keepInvalid) {
                "keep"
              } else {
                "error"
              }
            }.")
        }
      }
    })
    val vectorValues = indices.map(_ => 1.0)
    if (keepInvalid) {
      Vectors.sparse(valueAndIdx.size + 1, indices.sorted, vectorValues)
    } else {
      Vectors.sparse(valueAndIdx.size, indices.sorted, vectorValues)
    }
  }

}
