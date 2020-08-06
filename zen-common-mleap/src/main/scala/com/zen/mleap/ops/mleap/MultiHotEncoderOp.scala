package com.zen.mleap.ops.mleap

import com.zen.mleap.model.MultiHotEncoderModel
import com.zen.mleap.transformer.MultiHotEncoder
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
class MultiHotEncoderOp extends MleapOp[MultiHotEncoder, MultiHotEncoderModel] {
  override val Model: OpModel[MleapContext, MultiHotEncoderModel] = new OpModel[MleapContext, MultiHotEncoderModel] {
    override val klazz: Class[MultiHotEncoderModel] = classOf[MultiHotEncoderModel]

    override def opName: String = "multi_hot_encoder"

    override def store(model: Model, obj: MultiHotEncoderModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("keepInvalid", Value.boolean(obj.keepInvalid))
        .withValue("delimiter", Value.string(obj.delimiter))
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCols", Value.stringList(obj.outputCols))
        .withValue("dict", Value.byteList(SerializationUtil.serialization(obj.dict)))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): MultiHotEncoderModel = {
      /**
       * inputCols:Array[String],
       * outputCols:Array[String],
       * keepInvalid:Boolean,
       * delimiter:String,
       */
      MultiHotEncoderModel(model.value("inputCols").getStringList.toArray,
        model.value("outputCols").getStringList.toArray,
        model.value("keepInvalid").getBoolean,
        model.value("delimiter").getString,
        SerializationUtil.deserialization(model.value("dict").getByteList.toArray)
          .asInstanceOf[Map[Int, Map[String, Int]]]
      )
    }
  }

  override def model(node: MultiHotEncoder): MultiHotEncoderModel = node.model
}
