package com.zen.mleap.ops.mleap

import com.zen.mleap.model.CusVectorAssemblerModel
import com.zen.mleap.transformer.{CusVectorAssembler, OneHotEncoder}
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:20
 * @description
 * @reviewer
 */
class CusVectorAssemblerOp extends MleapOp[CusVectorAssembler, CusVectorAssemblerModel] {
  override val Model: OpModel[MleapContext, CusVectorAssemblerModel] = new OpModel[MleapContext, CusVectorAssemblerModel] {
    override val klazz: Class[CusVectorAssemblerModel] = classOf[CusVectorAssemblerModel]

    override def opName: String = "cus_vectorAssembler"

    override def store(model: Model, obj: CusVectorAssemblerModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("inputDoubleCols", Value.stringList(obj.inputDoubleCols))
        .withValue("inputIntCols", Value.stringList(obj.inputIntCols))
        .withValue("inputVectorCols", Value.stringList(obj.inputVectorCols))
        .withValue("outputCol", Value.string(obj.outputCol))
        .withValue("inputVectorDims", Value.intList(obj.inputVectorDims))
        .withValue("outputVectorDim", Value.int(obj.outputVectorDim))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): CusVectorAssemblerModel = {

      CusVectorAssemblerModel(
        model.value("inputDoubleCols").getStringList.toArray,
        model.value("inputIntCols").getStringList.toArray,
        model.value("inputVectorCols").getStringList.toArray,
        model.value("outputCol").getString,
        model.value("inputVectorDims").getIntList.toArray,
        model.value("outputVectorDim").getInt
      )
    }
  }

  override def model(node: CusVectorAssembler): CusVectorAssemblerModel = node.model
}
