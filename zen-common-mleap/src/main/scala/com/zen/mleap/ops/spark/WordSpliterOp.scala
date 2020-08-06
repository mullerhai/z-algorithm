package com.zen.mleap.ops.spark

import com.zen.spark.transformer.{ToTypes, WordSpliter}
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{BundleHelper, ParamSpec, SimpleSparkOp, SparkBundleContext}

/**
 * @Author: xiongjun
 * @Date: 2020/6/2 16:36
 * @description
 * @reviewer
 */
class WordSpliterOp extends SimpleSparkOp[WordSpliter] {
  override def sparkInputs(obj: WordSpliter): Seq[ParamSpec] = Seq("input" -> obj.inputCol)

  override def sparkOutputs(obj: WordSpliter): Seq[ParamSpec] = Seq("output" -> obj.outputCol)

  override def sparkLoad(uid: String, shape: NodeShape, model: WordSpliter): WordSpliter = {
    new WordSpliter(uid = uid)
  }

  override val Model: OpModel[SparkBundleContext, WordSpliter] = new OpModel[SparkBundleContext, WordSpliter] {
    override val klazz: Class[WordSpliter] = classOf[WordSpliter]

    override def opName: String = "word_spliter"

    override def store(model: Model, obj: WordSpliter)(implicit context: BundleContext[SparkBundleContext]): Model = {
      assert(context.context.dataset.isDefined, BundleHelper.sampleDataframeMessage(klazz))
      //      val dataset = context.context.dataset.get
      model.withValue("delimiter", Value.string(obj.getDelimiter))
        .withValue("toType", Value.string(obj.getToType.toString))
      //        .withValue("input_shapes", Value.dataShape(sparkToMleapDataShape(dataset.schema(obj.getInputCol), dataset)))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): WordSpliter = {
      val toType = model.value("toType").getString match {
        case "String" => ToTypes.String
        case "Array" => ToTypes.Array
      }

      new WordSpliter(uid = " ").setDelimiter(model.value("delimiter").getString)
        .setToType(toType)
    }
  }
}
