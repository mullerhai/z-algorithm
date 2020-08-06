package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}


/**
 * @Author: morris
 * @Date: 2020/6/15 16:08
 * @description 归一化模型
 * @reviewer
 */
case class NormalizedModel(inputCols: Array[String],
                           outputCols: Array[String],
                           dict: Map[Int, Seq[Double]],
                           algorithmType: Int, //归一化=1  标准化=2
                           min: Array[Double],
                           max: Array[Double]) extends Model {

  /** 归一化：最大最小值 */
  val Normalization = 1
  /** 标准化 */
  val Standardization = 2

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

  def apply(values: Array[Double]): Array[Double] = {
    //算法选择 归一化=1  标准化=2
    if (algorithmType == Normalization){
      values.zipWithIndex.map {
        case (value:Double,index:Int) ⇒ normalizationEncoder(value,index)
      }
    }else{
      values.zipWithIndex.map {
        case (value:Double,index:Int) ⇒ standardizationEncoder(value,index)
      }
    }
  }



  def standardizationEncoder(value: Double,index:Int):Double = {
      val scoreParameter = dict.get(index)
      val avg = scoreParameter.get.head
      val std = scoreParameter.get.last
      //边界处理
      borderProcessor(index, (value - avg) / std)

    }

   def normalizationEncoder(value: Double,index:Int):Double = {

      val scoreParameter = dict.get(index)
      val max = scoreParameter.get.head
      val min = scoreParameter.get.last
      //边界处理
      borderProcessor(index, (value - min) / (max - min))
    }



  /**
   * 边界处理器
   *
   * @param index 特征索引
   * @param x     算法输出的值
   * @return
   */
  private def borderProcessor(index: Int, x: Double) = {
    //边界处理
    val minBorder = min(index)
    val maxBorder = max(index)
    if (x > maxBorder) {
      maxBorder
    } else if (x < minBorder) {
      minBorder
    } else {
      x
    }
  }

}
