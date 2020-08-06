package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}
import org.apache.spark.ml.feature.BucketizerExtendUtil

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 17:46
 * @description
 * @reviewer
 */
case class BucketizerExtendModel(inputCols: Array[String],
                                 outputCols: Array[String],
                                 splitsArray: Array[Array[Double]],
                                 numOfBucket: Int,
                                 rule: String
                                ) extends Model {
  override def inputSchema: StructType = {
    val inputFields = inputCols.indices.map(idx => {
      StructField(s"input$idx", ScalarType.Double)
    })
    StructType(inputFields).get
  }

  override def outputSchema: StructType = {
    val outputFields = outputCols.indices.map(idx => {
      StructField(s"output$idx", ScalarType.Double)
    })
    StructType(outputFields).get
  }

  def apply(values: Array[Double]): Array[Double] = {
    values.zipWithIndex.map(valAndIdx => {
      val splits = splitsArray(valAndIdx._2)
      if (valAndIdx._1 < splits.head) {
        0.0
      } else if (valAndIdx._1 >= splits.last) {
        (numOfBucket - 1).toDouble
      } else {
        BucketizerExtendUtil.binarySearch(splits, valAndIdx._1).toDouble
      }
    })

  }
}
