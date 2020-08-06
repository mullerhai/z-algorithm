package com.zen.mleap.ops.mleap

import com.zen.mleap.model.CountEncoderModel
import com.zen.mleap.transformer.CountEncoder
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
class CountEncoderOp extends MleapOp[CountEncoder, CountEncoderModel] {
  override val Model: OpModel[MleapContext, CountEncoderModel] = new OpModel[MleapContext, CountEncoderModel] {
    override val klazz: Class[CountEncoderModel] = classOf[CountEncoderModel]

    override def opName: String = "count_encoder"

    override def store(model: Model, obj: CountEncoderModel)(implicit context: BundleContext[MleapContext]): Model = {
      model
        .withValue("keepInvalid", Value.boolean(obj.keepInvalid))
        .withValue("inputCols", Value.stringList(obj.inputCols))
        .withValue("outputCols", Value.stringList(obj.outputCols))
        .withValue("dicts", Value.byteList(SerializationUtil.serialization(obj.dicts)))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): CountEncoderModel = {
      /**
       * inputCols:Array[String],
       * outputCols:Array[String],
       * keepInvalid:Boolean,
       * delimiter:String,
       * dict:Map[Int,Map[String,Int]]
       */
      CountEncoderModel(model.value("inputCols").getStringList.toArray,
        model.value("outputCols").getStringList.toArray,
        model.value("keepInvalid").getBoolean,
        SerializationUtil.deserialization(model.value("dict").getByteList.toArray)
          .asInstanceOf[Array[Map[Int, Int]]]
      )
    }
  }

  override def model(node: CountEncoder): CountEncoderModel = node.model
}
