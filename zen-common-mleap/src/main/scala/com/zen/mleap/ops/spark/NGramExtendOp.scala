package com.zen.mleap.ops.spark

import com.zen.spark.transformer.NGramExtend
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 11:36
 * @description
 * @reviewer
 */
class NGramExtendOp extends SimpleSparkOp[NGramExtend] {
  override def sparkInputs(obj: NGramExtend): Seq[ParamSpec] = Seq("input" -> obj.inputCol)

  override def sparkOutputs(obj: NGramExtend): Seq[ParamSpec] = Seq("output" -> obj.outputCol)

  override def sparkLoad(uid: String, shape: NodeShape, model: NGramExtend): NGramExtend = new NGramExtend(uid)

  override val Model: OpModel[SparkBundleContext, NGramExtend] = new OpModel[SparkBundleContext, NGramExtend] {
    override val klazz: Class[NGramExtend] = classOf[NGramExtend]

    override def opName: String = "ngram_extend"

    override def store(model: Model, obj: NGramExtend)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model.withValue("n", Value.long(obj.getN)).withValue("blank", Value.boolean(obj.getBlank))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): NGramExtend = {
      new NGramExtend(uid = "").setN(model.value("n").getLong.toInt).setBlank(model.value("blank").getBoolean)
    }
  }
}
