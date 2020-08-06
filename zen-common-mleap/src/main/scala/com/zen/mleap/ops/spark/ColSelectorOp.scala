package com.zen.mleap.ops.spark
import com.zen.spark.transformer.ColSelector
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
/**
 * @Author: xiongjun
 * @Date: 2020/6/22 16:01
 * @description
 * @reviewer
 */
class ColSelectorOp extends SimpleSparkOp[ColSelector] {
  override def sparkInputs(obj: ColSelector): Seq[ParamSpec] = Seq()

  override def sparkOutputs(obj: ColSelector): Seq[ParamSpec] = Seq()

  override def sparkLoad(uid: String, shape: NodeShape, model: ColSelector): ColSelector = {
    new ColSelector(uid = uid)
  }

  override val Model: OpModel[SparkBundleContext, ColSelector] = new OpModel[SparkBundleContext, ColSelector] {
    override val klazz: Class[ColSelector] = classOf[ColSelector]

    override def opName: String = "col_selector"

    override def store(model: Model, obj: ColSelector)(implicit context: BundleContext[SparkBundleContext]): Model = {
      model.withValue("cols", Value.stringList(obj.getCols))
    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): ColSelector = {
      new ColSelector().setCols(model.value("cols").getStringList.toArray)
    }
  }
}
