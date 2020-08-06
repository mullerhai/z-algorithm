package com.zen.mleap.ops.mleap

import com.zen.mleap.model.BucketizerModel
import com.zen.mleap.transformer.Bucketizer
import com.zen.mleap.util.SerializationUtil
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
class BucketizerOp extends MleapOp[Bucketizer, BucketizerModel] {
  override val Model: OpModel[MleapContext, BucketizerModel] = new OpModel[MleapContext, BucketizerModel] {
    override val klazz: Class[BucketizerModel] = classOf[BucketizerModel]

    override def opName: String = "Bucketizer_encoder"

    override def store(model: Model, obj: BucketizerModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("handleInvalid", Value.string(obj.handleInvalid))
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCols", Value.stringList(obj.outputCols))
        .withValue("splitsArray", Value.byteList(SerializationUtil.serialization(obj.splitsArray)))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): BucketizerModel = {

      BucketizerModel(model.value("inputCols").getStringList.toArray,
        model.value("outputCols").getStringList.toArray,
        model.value("handleInvalid").getString,
        SerializationUtil.deserialization(model.value("splitsArray").getByteList.toArray)
          .asInstanceOf[Array[Array[Double]]])
    }


  }



  override def model(node: Bucketizer): BucketizerModel = node.model
}
