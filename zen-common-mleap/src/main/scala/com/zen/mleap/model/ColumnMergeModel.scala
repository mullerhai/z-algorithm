package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}

/**
 * @Author: morris
 * @Date: 2020/7/16
 * @description
 * @reviewer
 */
case class ColumnMergeModel(inputCols: Array[String],
                            outputCol: String,
                            delimiter: String
                           ) extends Model {

  override def inputSchema: StructType = {
    val inputFields = inputCols.indices.map(idx => {
      StructField(s"input$idx", ScalarType.String)
    })
    StructType(inputFields).get
  }

  override def outputSchema: StructType = StructType("output" -> ScalarType.String).get


  def apply(values: Array[Any]): String = {
    var res = ""
    values.foreach(word => {
      word.asInstanceOf[String]
      res +=  word + delimiter
    })
    //delimiter default ""
    if ("".equals(delimiter)){
      res
    }else{
      res.substring(0,res.length-1)
    }

  }
}
