package com.zen.mleap.transformer

import com.zen.mleap.model.SplitCounterModel
import ml.combust.mleap.core.types.{NodeShape, SchemaSpec}
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}

import scala.util.Try

/**
 * @Author: morris
 * @Date: 2020/6/18 11:38
 * @description
 * @reviewer
 */
class SplitCounter (override val uid: String = Transformer.uniqueName("split_counter"),
                    override val shape: NodeShape,
                    override val model: SplitCounterModel) extends SimpleTransformer {

  private val f = (values: Row) => {
    val v = values.toSeq.asInstanceOf[Seq[String]].toArray
    val res = model(v)
    Row(res: _*)
  }
  override val exec: UserDefinedFunction = UserDefinedFunction(f, outputSchema, Seq(SchemaSpec(inputSchema)))

  val outputCols: Seq[String] = outputSchema.fields.map(_.name)
  val inputCols: Seq[String] = inputSchema.fields.map(_.name)
  private val inputSelector: StructSelector = StructSelector(inputCols)


  override def transform[TB <: FrameBuilder[TB]](builder: TB): Try[TB] = {
    builder.withColumns(outputCols, inputSelector)(exec)
  }

}
