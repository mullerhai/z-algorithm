package com.zen.mleap.ops.mleap

import com.zen.mleap.model.{BucketizerExtendModel, CountEncoderModel}
import com.zen.mleap.transformer.{BucketizerExtend, CountEncoder, MultiHotEncoder}
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
class BucketizerExtendOp extends MleapOp[BucketizerExtend, BucketizerExtendModel] {
  override val Model: OpModel[MleapContext, BucketizerExtendModel] = new OpModel[MleapContext, BucketizerExtendModel] {
    override val klazz: Class[BucketizerExtendModel] = classOf[BucketizerExtendModel]

    override def opName: String = "bucketizer_extend"

    override def store(model: Model, obj: BucketizerExtendModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("rule", Value.string(obj.rule))
        .withValue("numOfBucket", Value.int(obj.numOfBucket))
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCols", Value.stringList(obj.outputCols))
        .withValue("splitsArray", Value.byteList(SerializationUtil.serialization(obj.splitsArray)))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): BucketizerExtendModel = {
      /**
       * inputCols:Array[String],
       * outputCols:Array[String],
       * keepInvalid:Boolean,
       * delimiter:String,
       * dict:Map[Int,Map[String,Int]]
       */
      BucketizerExtendModel(model.value("inputCols").getStringList.toArray,
        model.value("outputCols").getStringList.toArray,
        SerializationUtil.deserialization(model.value("splitsArray").getByteList.toArray).asInstanceOf[Array[Array[Double]]],
        model.value("numOfBucket").getInt,
        model.value("rule").getString
      )
  }


}
  override def model(node: BucketizerExtend): BucketizerExtendModel = node.model
}
