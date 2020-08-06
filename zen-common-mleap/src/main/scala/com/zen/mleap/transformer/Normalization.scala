package com.zen.mleap.transformer

import ml.combust.mleap.core.Model
import ml.combust.mleap.core.types.{NodeShape, SchemaSpec}
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}
import com.zen.mleap.model.NormalizedModel

import scala.util.Try

/**
 * @Author: morris
 * @Date: 2020/6/15 17:21
 * @description
 * @reviewer
 */
case class Normalization(override val uid: String = Transformer.uniqueName("Normalization"),
                                override val shape: NodeShape,
                                override val model: NormalizedModel) extends SimpleTransformer {
  private val f = (values: Row) => {
    val v = values.toSeq.asInstanceOf[Seq[Double]].toArray
    val res = model(v)
    Row(res: _*)
  }
  val exec: UserDefinedFunction =
    UserDefinedFunction(f, outputSchema, Seq(SchemaSpec(inputSchema)))

  val outputCols: Seq[String] = outputSchema.fields.map(_.name)
  val inputCols: Seq[String] = inputSchema.fields.map(_.name)
  private val inputSelector: StructSelector = StructSelector(inputCols)

  override def transform[TB <: FrameBuilder[TB]](builder: TB): Try[TB] = {
    builder.withColumns(outputCols, inputSelector)(exec)
  }
}
