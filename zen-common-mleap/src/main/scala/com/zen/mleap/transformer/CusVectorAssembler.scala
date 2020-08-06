package com.zen.mleap.transformer
import com.zen.mleap.model.CusVectorAssemblerModel
import ml.combust.mleap.core.types._
import ml.combust.mleap.core.util.VectorConverters._
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}
import ml.combust.mleap.tensor.Tensor

import scala.util.Try
/**
 * @Author: morris
 * @Date: 2020/6/23 16:57
 * @description
 * @reviewer
 */


case class CusVectorAssembler(override val uid: String = Transformer.uniqueName("vector_assembler"),
                           override val shape: NodeShape,
                           override val model: CusVectorAssemblerModel) extends Transformer {
  private val f = (values: Row) => model(values.toSeq): Tensor[Double]
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
