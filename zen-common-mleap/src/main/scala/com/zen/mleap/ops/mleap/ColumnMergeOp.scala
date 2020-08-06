package com.zen.mleap.ops.mleap

import com.zen.mleap.model.ColumnMergeModel
import com.zen.mleap.transformer.ColumnMerge
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: morris
 * @Date: 2020/7/16 11:53
 * @description
 * @reviewer
 */
class ColumnMergeOp extends MleapOp[ColumnMerge,ColumnMergeModel]{
  override val Model: OpModel[MleapContext, ColumnMergeModel] = new OpModel[MleapContext, ColumnMergeModel] {
    override val klazz: Class[ColumnMergeModel] = classOf[ColumnMergeModel]

    override def opName: String = "col_merge"

    override def store(model: Model, obj: ColumnMergeModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCol", Value.string(obj.outputCol))
        .withValue("delimiter", Value.string(obj.delimiter))

    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): ColumnMergeModel = {
      ColumnMergeModel(model.value("inputCols").getStringList.toArray,
        model.value("outputCol").getString,
        model.value("delimiter").getString
      )
    }
  }

  override def model(node: ColumnMerge): ColumnMergeModel = node.model
}
