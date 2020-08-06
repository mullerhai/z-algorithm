package com.zen.mleap.ops.mleap


import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import com.zen.mleap.model.MetaStorageModel
import com.zen.mleap.transformer.MetaStorage
import com.zen.spark.transformer.ModelType
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, Value}
import ml.combust.bundle.op.OpModel
import ml.combust.mleap.bundle.ops.MleapOp
import ml.combust.mleap.runtime.MleapContext

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 17:00
 * @description
 * @reviewer
 */
class MetaStorageOp extends MleapOp[MetaStorage, MetaStorageModel] {
  override val Model: OpModel[MleapContext, MetaStorageModel] = new OpModel[MleapContext, MetaStorageModel] {
    override val klazz: Class[MetaStorageModel] = classOf[MetaStorageModel]

    override def opName: String = "meta_storage"

    override def store(model: Model, obj: MetaStorageModel)(implicit context: BundleContext[MleapContext]): Model = {
      val bo = new ByteArrayOutputStream()
      val oo = new ObjectOutputStream(bo)
      oo.writeObject(obj.params)

      val bytes = bo.toByteArray

      bo.close()
      oo.close()
      model.withValue("modelType", Value.string(obj.modelType.toString))
        .withValue("params", Value.byteList(bytes))
        .withValue("fields", Value.stringList(obj.fields))
    }

    override def load(model: Model)(implicit context: BundleContext[MleapContext]): MetaStorageModel = {
      val modelType = model.value("modelType").getString match {
        case "Preprocessing" => ModelType.Preprocessing
        case "Feature" => ModelType.Feature
        case "Algorithm_Classification_Prob" => ModelType.Algorithm_Classification_Prob
        case "Algorithm_Regression" => ModelType.Algorithm_Regression
        case "Clustering" => ModelType.Clustering
      }
      val bytes = model.value("params").getByteList
      // bytearray to object
      val bi = new ByteArrayInputStream(bytes.toArray)
      val oi = new ObjectInputStream(bi)

      val obj = oi.readObject()
      bi.close()
      oi.close()
      val params = obj.asInstanceOf[Map[String, Any]]
      val fields = model.value("fields").getStringList
      val metaStorageModel = MetaStorageModel(modelType, params, fields.toArray)
      metaStorageModel
    }
  }

  override def model(node: MetaStorage): MetaStorageModel = node.model
}
