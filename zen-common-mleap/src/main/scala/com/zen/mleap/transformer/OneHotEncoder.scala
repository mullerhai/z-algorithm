package com.zen.mleap.transformer

import com.zen.mleap.model.OneHotEncoderModel
import ml.combust.mleap.core.types.{NodeShape, SchemaSpec}
import ml.combust.mleap.core.util.VectorConverters
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}

import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 17:33
 * @description
 * @reviewer
 */
case class OneHotEncoder(override val uid: String =
                           Transformer.uniqueName("one_hot_encoder"),
                         override val shape: NodeShape,
                         override val model: OneHotEncoderModel)
  extends SimpleTransformer {
  private val f = (values: Row) => {
    val v = values.toSeq.asInstanceOf[Seq[Double]].toArray
    val res = model(v).map(VectorConverters.sparkVectorToMleapTensor)
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
