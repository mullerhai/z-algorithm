package com.zen.mleap.transformer

import com.zen.mleap.model.{ColumnMergeModel, WordSpliterModel}
import ml.combust.mleap.core.types.{NodeShape, SchemaSpec}
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}
import ml.combust.mleap.tensor.Tensor

import scala.util.Try

/**
 * @Author: morris
 * @Date: 2020/7/16
 * @description
 * @reviewer
 */
case class ColumnMerge(override val uid: String = Transformer.uniqueName("col_merge"),
                       override val shape: NodeShape,
                       override val model: ColumnMergeModel) extends SimpleTransformer {


  private val f = (values: Row) => model(values.toArray)

  val exec: UserDefinedFunction = UserDefinedFunction(f,
    outputSchema.fields.head.dataType,
    Seq(SchemaSpec(inputSchema)))

  val outputCol: String = outputSchema.fields.head.name
  val inputCols: Seq[String] = inputSchema.fields.map(_.name)
  private val inputSelector: StructSelector = StructSelector(inputCols)

  override def transform[TB <: FrameBuilder[TB]](builder: TB): Try[TB] = {
    builder.withColumn(outputCol, inputSelector)(exec)
  }
}
