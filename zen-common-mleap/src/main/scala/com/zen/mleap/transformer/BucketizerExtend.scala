package com.zen.mleap.transformer

import com.zen.mleap.model.BucketizerExtendModel
import ml.combust.mleap.core.types.{NodeShape, SchemaSpec}
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}

import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 18:06
 * @description
 * @reviewer
 */
case class BucketizerExtend(override val uid: String =
                            Transformer.uniqueName("bucketizer_extend"),
                            override val shape: NodeShape,
                            override val model: BucketizerExtendModel)
  extends SimpleTransformer {
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
