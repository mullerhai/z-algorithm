package com.zen.mleap.ops.mleap

import ml.combust.mleap.bundle.ops.MleapOp
import com.zen.mleap.model.NormalizedModel
import com.zen.mleap.transformer.Normalization
import com.zen.mleap.util.SerializationUtil
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: morris
 * @Date: 2020/6/15 18:06
 * @description
 * @reviewer
 */
class NormalizationOp  extends MleapOp[Normalization,NormalizedModel]{
  override val Model: OpModel[MleapContext, NormalizedModel] = new OpModel[MleapContext, NormalizedModel]{

    override val klazz: Class[NormalizedModel] = classOf[NormalizedModel]

    override def opName: String = "normalization_scaler"

    override def store(model: Model, obj: NormalizedModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCols", Value.stringList(obj.outputCols))
        .withValue("dict", Value.byteList(SerializationUtil.serialization(obj.dict)))
        .withValue("min",Value.doubleList(obj.min))
        .withValue("max",Value.doubleList(obj.max))
        .withValue("algorithmType",Value.int(obj.algorithmType))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): NormalizedModel = {
      /**
       * inputCols: Array[String],
       * outputCols: Array[String],
       * algorithmType: Int, //归一化=1  标准化=2
       * min: Array[Double],
       * max: Array[Double]) extends Model
       */

      NormalizedModel(model.value("inputCols").getStringList.toArray,
        model.value("outputCols").getStringList.toArray,
        SerializationUtil.deserialization(model.value("dict").getByteList.toArray)
          .asInstanceOf[ Map[Int,Seq[Double]]],
        model.value("algorithmType").getInt,
        model.value("min").getDoubleList.toArray,
        model.value("max").getDoubleList.toArray
      )
    }
  }

  override def model(node: Normalization): NormalizedModel = node.model
}
