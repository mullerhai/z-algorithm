package com.zen.mleap.ops.spark

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{ParamSpec, SimpleSparkOp, SparkBundleContext}
import org.apache.spark.ml.feature.CusVectorAssemblerModel

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:12
 * @description
 * @reviewer
 */
class CusVectorAssemblerOp extends SimpleSparkOp[CusVectorAssemblerModel] {
  override def sparkInputs(obj: CusVectorAssemblerModel): Seq[ParamSpec] = Seq(ParamSpec("input", obj.inputCols))

  override def sparkOutputs(obj: CusVectorAssemblerModel): Seq[ParamSpec] = Seq(ParamSpec("output", obj.outputCol))

  override def sparkLoad(uid: String, shape: NodeShape, model: CusVectorAssemblerModel): CusVectorAssemblerModel = {
    new CusVectorAssemblerModel(uid,model.inputVectorDims,model.outputVectorDim)
      .setInputDoubleCols(model.getInputDoubleCols())
      .setInputIntCols(model.getInputIntCols())
      .setInputVectorCols(model.getInputVectorCols())
      .setOutputCol(model.getOutputCol)

  }

  override val Model: OpModel[SparkBundleContext, CusVectorAssemblerModel] = new OpModel[SparkBundleContext, CusVectorAssemblerModel] {
    override val klazz: Class[CusVectorAssemblerModel] = classOf[CusVectorAssemblerModel]

    override def opName: String = "cus_vectorAssembler"

    override def store(model: Model, obj: CusVectorAssemblerModel)(implicit context: BundleContext[SparkBundleContext]): Model = {

      model
        .withValue("inputDoubleCols", Value.stringList(obj.getInputDoubleCols()))
        .withValue("inputIntCols", Value.stringList(obj.getInputIntCols()))
        .withValue("inputVectorCols", Value.stringList(obj.getInputVectorCols()))
        .withValue("outputCol", Value.string(obj.getOutputCol))
        .withValue("inputVectorDims", Value.intList(obj.inputVectorDims))
        .withValue("outputVectorDim", Value.int(obj.outputVectorDim))

    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): CusVectorAssemblerModel = {

      new CusVectorAssemblerModel(
        "",
        model.value("inputVectorDims").getIntList.toArray,
        model.value("outputVectorDim").getInt
      )
        .setInputDoubleCols(model.value("inputDoubleCols").getStringList.toArray)
        .setInputIntCols(model.value("inputIntCols").getStringList.toArray)
        .setInputVectorCols(model.value("inputVectorCols").getStringList.toArray)
        .setOutputCol(model.value("outputCol").getString)
    }
  }
}
