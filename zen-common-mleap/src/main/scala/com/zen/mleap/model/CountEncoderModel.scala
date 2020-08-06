package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}

/**
 * @Author: xiongjun
 * @Date: 2020/6/11 9:46
 * @description
 * @reviewer
 */
case class CountEncoderModel(inputCols: Array[String],
                             outputCols: Array[String],
                             keepInvalid: Boolean,
                             dicts: Array[Map[Int, Int]]) extends Model {
  override def inputSchema: StructType = {
    val inputFields = inputCols.indices.map(idx => {
      StructField(s"input$idx", ScalarType.Int)
    })
    StructType(inputFields).get
  }

  override def outputSchema: StructType = {
    val outputFields = outputCols.indices.map(idx => {
      StructField(s"output$idx", ScalarType.Int)
    })
    StructType(outputFields).get
  }

  def apply(values: Array[Int]): Array[Int] = {
    values.zipWithIndex.map(valAndIdx => {
      val map = dicts(valAndIdx._2)
      if (map.contains(valAndIdx._1)) {
        map(valAndIdx._1)
      } else {
        if (keepInvalid) {
          0
        } else {
          throw new Exception(s"Unseen value: ${valAndIdx._1}. To handle unseen values, " +
            s"set Param handleInvalid to keep.")
        }
      }
    })
  }
}
