package com.zen.mleap.ops.spark

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import com.zen.spark.transformer.{MetaStorage, ModelType}
import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl.{Model, NodeShape, Value}
import ml.combust.bundle.op.OpModel
import org.apache.spark.ml.bundle.{BundleHelper, ParamSpec, SimpleSparkOp, SparkBundleContext}

/**
 * @Author: xiongjun
 * @Date: 2020/6/3 16:55
 * @description
 * @reviewer
 */
class MetaStorageOp extends SimpleSparkOp[MetaStorage] {
  override def sparkInputs(obj: MetaStorage): Seq[ParamSpec] = Seq()

  override def sparkOutputs(obj: MetaStorage): Seq[ParamSpec] = Seq()

  override def sparkLoad(uid: String, shape: NodeShape, model: MetaStorage): MetaStorage = {
    new MetaStorage(uid = uid)
  }

  override val Model: OpModel[SparkBundleContext, MetaStorage] = new OpModel[SparkBundleContext, MetaStorage] {
    override val klazz: Class[MetaStorage] = classOf[MetaStorage]

    override def opName: String = "meta_storage"

    override def store(model: Model, obj: MetaStorage)(implicit context: BundleContext[SparkBundleContext]): Model = {

      val bo = new ByteArrayOutputStream()
      val oo = new ObjectOutputStream(bo)
      oo.writeObject(obj.getParameters)

      val bytes = bo.toByteArray

      bo.close()
      oo.close()

      assert(context.context.dataset.isDefined, BundleHelper.sampleDataframeMessage(klazz))
      model.withValue("modelType", Value.string(obj.getModelType.toString))
        .withValue("params", Value.byteList(bytes))
        .withValue("fields", Value.stringList(obj.getFields))

    }

    override def load(model: Model)(implicit context: BundleContext[SparkBundleContext]): MetaStorage = {
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
      new MetaStorage().setModelType(modelType).setParameters(params)
        .setFields(model.value("fields").getStringList.toArray)
    }
  }
}
