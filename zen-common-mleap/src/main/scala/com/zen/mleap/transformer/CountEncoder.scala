package com.zen.mleap.transformer

import com.zen.mleap.model.CountEncoderModel
import ml.combust.mleap.core.types.{NodeShape, SchemaSpec}
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}

import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/11 17:09
 * @description
 * @reviewer
 */
case class CountEncoder(override val uid: String =
                        Transformer.uniqueName("count_encoder"),
                        override val shape: NodeShape,
                        override val model: CountEncoderModel)
  extends SimpleTransformer {
  private val f = (values: Row) => {
    val v = values.toSeq.asInstanceOf[Seq[Int]].toArray
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
