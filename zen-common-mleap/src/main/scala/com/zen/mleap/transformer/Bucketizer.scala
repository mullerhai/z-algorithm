package com.zen.mleap.transformer

import com.zen.mleap.model.BucketizerModel
import ml.combust.mleap.core.types.{NodeShape, SchemaSpec}
import ml.combust.mleap.runtime.frame.{FrameBuilder, Row, SimpleTransformer, Transformer}
import ml.combust.mleap.runtime.function.{StructSelector, UserDefinedFunction}

import scala.util.Try

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 17:33
 * @description
 * @reviewer
 */
case class Bucketizer(override val uid: String =
                      Transformer.uniqueName("bucketizer_encoder"),
                      override val shape: NodeShape,
                      override val model: BucketizerModel)
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
object BucketizerUtil {
  def restoreSplits(splits : Array[Double]): Array[Double] = {
    splits.update(0, update(splits.head, Double.NegativeInfinity))
    splits.update(splits.length - 1, update(splits.last, Double.PositiveInfinity))
    splits
  }

  private def update(orig: Double, updated: Double) = if (orig.isNaN) updated else orig
}