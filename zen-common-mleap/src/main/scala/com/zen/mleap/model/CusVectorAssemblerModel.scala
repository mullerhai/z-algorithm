package com.zen.mleap.model

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType, TensorType}
import ml.combust.mleap.tensor.{DenseTensor, SparseTensor}
import org.apache.spark.ml.linalg.{Vector, Vectors}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * @Author: morris
 * @Date: 2020/6/23 16:46
 * @description
 * @reviewer
 */
case class CusVectorAssemblerModel(
                                    inputDoubleCols: Array[String],
                                    inputIntCols: Array[String],
                                    inputVectorCols: Array[String],
                                    outputCol: String,
                                    inputVectorDims: Array[Int],
                                    outputVectorDim: Int
                                  ) extends Model {
  val INVALID_KEY = "##"

  //todo 向量计算
  override def inputSchema: StructType = {
    var startingAmount = 0

    val inputDoubleField :Seq[StructField]= if (!inputDoubleCols.head.equals(INVALID_KEY)) {
      val inputDoubleField = inputDoubleCols.indices.map(idx => {
        StructField(s"input${idx + startingAmount}", ScalarType.Double)
      })
      startingAmount += inputDoubleCols.length //排在double列索引之后
      inputDoubleField
    }else null

    val intStructFields:Seq[StructField] = if (!inputIntCols.head.equals(INVALID_KEY)) {
      val intStructFields = inputIntCols.indices.map(idx => {
        StructField(s"input${idx + startingAmount}", ScalarType.Int)
      })
      startingAmount += inputIntCols.length
      intStructFields
    }else null

    val inputVectorFields = if (!inputVectorCols.head.equals(INVALID_KEY)) {
      inputVectorCols.indices.map(idx => {
        StructField(s"input${idx + startingAmount}", TensorType.Double(inputVectorDims(idx)))
      })
    }else null
    var fields  = new ArrayBuffer[StructField]()
    if(inputDoubleField!= null){
      fields = fields ++ inputDoubleField
    }
    if (intStructFields != null){
      fields ++= intStructFields
    }
    if (inputVectorFields != null){
      fields ++= inputVectorFields
    }

    StructType(fields).get

  }

  override def outputSchema: StructType = StructType("output" -> TensorType.Double(outputVectorDim)).get


  def apply(vv: Seq[Any]): Vector = {
    val indices = mutable.ArrayBuilder.make[Int]
    val values = mutable.ArrayBuilder.make[Double]
    var cur = 0
    vv.foreach {
      case v: Double =>
        if (v != 0.0) {
          indices += cur
          values += v
        }
        cur += 1
      case v: Int =>
        if (v != 0.0) {
          indices += cur
          values += v
        }
        cur += 1
      case tensor: DenseTensor[_] if tensor.dimensions.size == 1 =>
        val dTensor = tensor.asInstanceOf[DenseTensor[Double]]
        dTensor.values.indices.foreach {
          i =>
            val v = dTensor.values(i)
            if (v != 0.0) {
              indices += cur + i
              values += v
            }
        }
        cur += dTensor.values.length
      case tensor: SparseTensor[_] if tensor.dimensions.size == 1 =>
        val dTensor = tensor.asInstanceOf[SparseTensor[Double]]
        var idx = 0
        dTensor.indices.map(_.head).foreach {
          i =>
            val v = dTensor.values(idx)
            if (v != 0.0) {
              indices += cur + i
              values += v
            }
            idx += 1
        }
        cur += dTensor.dimensions.head
      case vec: Vector =>
        vec.foreachActive { case (i, v) =>
          if (v != 0.0) {
            indices += cur + i
            values += v
          }
        }
        cur += vec.size
      case v: java.math.BigDecimal =>
        val d = v.doubleValue()
        if (d != 0.0) {
          indices += cur
          values += d
        }
        cur += 1
      case Some(v: Double) =>
        if (v != 0.0) {
          indices += cur
          values += v
        }
        cur += 1
    }
    Vectors.sparse(cur, indices.result(), values.result()).compressed
  }


}
