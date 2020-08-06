package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}

/**
 * @Author: morris
 * @Date: 2020/6/18 11:29
 * @description
 * @reviewer
 */
case class SplitCounterModel (inputCols: Array[String],
                         outputCols: Array[String],
                         delimiterArray: Array[String]) extends Model{
  override def inputSchema: StructType = {
    val structFields = inputCols.indices.map(idx => {
      StructField(s"input$idx", ScalarType.String)
    })
    StructType(structFields).get
  }

  override def outputSchema: StructType = {
    val structFields = outputCols.indices.map(idx => {
      StructField(s"output$idx", ScalarType.Int)
    })
    StructType(structFields).get
  }

  def apply(values: Array[String]): Array[Int] = {
    values.zipWithIndex.map {index=>{
        val delimiter = delimiterArray(index._2)
        val length = index._1.split("\\"+delimiter).length
        length
    }
    }

  }
}
