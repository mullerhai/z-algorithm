/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.ml.classification

import org.apache.hadoop.fs.Path
import org.apache.spark.annotation.Since
import org.apache.spark.internal.Logging
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.{DenseVector, Vector, Vectors}
import org.apache.spark.ml.param.{Param, ParamMap, Params}
import org.apache.spark.ml.tree._
import org.apache.spark.ml.tree.impl.GradientBoostedTrees
import org.apache.spark.ml.util._
import org.apache.spark.ml.{PredictionModel, Predictor}
import org.apache.spark.mllib.tree.configuration.{FeatureType, Algo => OldAlgo}
import org.apache.spark.mllib.tree.model.{DecisionTreeModel, GradientBoostedTreesModel => OldGBTModel, Node => OldNode}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row}

/**
 * @Author: xiongjun
 * @Date: 2020/7/17 11:10
 * @description 选择保留指定的列，节约资源
 * @reviewer
 */
trait CusGBTClassifierParams extends Params {
  /**
   * gbdt生成的特征列的名称
   *
   * @group param
   */
  val gbtGeneratedFeaturesCol: Param[String] = new Param[String](this, "gbtGeneratedCol",
    "gbt generated features column name")

  /** @group getParam */
  def getGbtGeneratedFeaturesCol: String = $(gbtGeneratedFeaturesCol)

  def setGbtGeneratedFeaturesCol(value: String): this.type = set(gbtGeneratedFeaturesCol, value)
}

@Since("1.4.0")
class CusGBTClassifier @Since("1.4.0")(
                                        @Since("1.4.0") override val uid: String)
  extends Predictor[Vector, CusGBTClassifier, CusGBTClassificationModel]
    with GBTClassifierParams with DefaultParamsWritable with CusGBTClassifierParams with Logging {

  @Since("1.4.0")
  def this() = this(Identifiable.randomUID("gbtc"))

  // Override parameter setters from parent trait for Java API compatibility.

  // Parameters from TreeClassifierParams:

  /** @group setParam */
  @Since("1.4.0")
  override def setMaxDepth(value: Int): this.type = set(maxDepth, value)

  /** @group setParam */
  @Since("1.4.0")
  override def setMaxBins(value: Int): this.type = set(maxBins, value)

  /** @group setParam */
  @Since("1.4.0")
  override def setMinInstancesPerNode(value: Int): this.type = set(minInstancesPerNode, value)

  /** @group setParam */
  @Since("1.4.0")
  override def setMinInfoGain(value: Double): this.type = set(minInfoGain, value)

  /** @group expertSetParam */
  @Since("1.4.0")
  override def setMaxMemoryInMB(value: Int): this.type = set(maxMemoryInMB, value)

  /** @group expertSetParam */
  @Since("1.4.0")
  override def setCacheNodeIds(value: Boolean): this.type = set(cacheNodeIds, value)

  /**
   * Specifies how often to checkpoint the cached node IDs.
   * E.g. 10 means that the cache will get checkpointed every 10 iterations.
   * This is only used if cacheNodeIds is true and if the checkpoint directory is set in
   * [[org.apache.spark.SparkContext]].
   * Must be at least 1.
   * (default = 10)
   *
   * @group setParam
   */
  @Since("1.4.0")
  override def setCheckpointInterval(value: Int): this.type = set(checkpointInterval, value)

  /**
   * The impurity setting is ignored for GBT models.
   * Individual trees are built using impurity "Variance."
   *
   * @group setParam
   */
  @Since("1.4.0")
  override def setImpurity(value: String): this.type = {
    logWarning("GBTClassifier.setImpurity should NOT be used")
    this
  }

  // Parameters from TreeEnsembleParams:

  /** @group setParam */
  @Since("1.4.0")
  override def setSubsamplingRate(value: Double): this.type = set(subsamplingRate, value)

  /** @group setParam */
  @Since("1.4.0")
  override def setSeed(value: Long): this.type = set(seed, value)

  // Parameters from GBTParams:

  /** @group setParam */
  @Since("1.4.0")
  override def setMaxIter(value: Int): this.type = set(maxIter, value)

  /** @group setParam */
  @Since("1.4.0")
  override def setStepSize(value: Double): this.type = set(stepSize, value)

  /** @group setParam */
  @Since("2.3.0")
  override def setFeatureSubsetStrategy(value: String): this.type =
    set(featureSubsetStrategy, value)

  // Parameters from GBTClassifierParams:

  /** @group setParam */
  @Since("1.4.0")
  def setLossType(value: String): this.type = set(lossType, value)

  override protected def train(dataset: Dataset[_]): CusGBTClassificationModel = {
    val categoricalFeatures: Map[Int, Int] =
      MetadataUtils.getCategoricalFeatures(dataset.schema($(featuresCol)))
    // We copy and modify this from Classifier.extractLabeledPoints since GBT only supports
    // 2 classes now.  This lets us provide a more precise error message.
    val oldDataset: RDD[LabeledPoint] =
    dataset.select(col($(labelCol)), col($(featuresCol))).rdd.map {
      case Row(label: Double, features: Vector) =>
        require(label == 0 || label == 1, s"GBTClassifier was given" +
          s" dataset with invalid label $label.  Labels must be in {0,1}; note that" +
          s" GBTClassifier currently only supports binary classification.")
        LabeledPoint(label, features)
    }
    val numFeatures = oldDataset.first().features.size
    val boostingStrategy = super.getOldBoostingStrategy(categoricalFeatures, OldAlgo.Classification)

    val numClasses = 2

    val instr = Instrumentation.create(this, oldDataset)
    instr.logParams(labelCol, featuresCol, predictionCol, impurity, lossType,
      maxDepth, maxBins, maxIter, maxMemoryInMB, minInfoGain, minInstancesPerNode,
      seed, stepSize, subsamplingRate, cacheNodeIds, checkpointInterval, featureSubsetStrategy)
    instr.logNumFeatures(numFeatures)
    instr.logNumClasses(numClasses)

    val (baseLearners, learnerWeights) = GradientBoostedTrees.run(oldDataset, boostingStrategy,
      $(seed), $(featureSubsetStrategy))
    val oldGBTModel = new OldGBTModel(OldAlgo.Classification, baseLearners.map(_.toOld), learnerWeights)
    val m = new CusGBTClassificationModel(uid, oldGBTModel, numFeatures, GBDTUitl.getOutputVectorDim(oldGBTModel))
    instr.logSuccess(m)
    m
  }

  @Since("1.4.1")
  override def copy(extra: ParamMap): CusGBTClassifier = defaultCopy(extra)
}

@Since("1.4.0")
object CusGBTClassifier extends DefaultParamsReadable[CusGBTClassifier] {

  /** Accessor for supported loss settings: logistic */
  @Since("1.4.0")
  final val supportedLossTypes: Array[String] = GBTClassifierParams.supportedLossTypes

  @Since("2.0.0")
  override def load(path: String): CusGBTClassifier = super.load(path)
}

/**
 * Gradient-Boosted Trees (GBTs) (http://en.wikipedia.org/wiki/Gradient_boosting)
 * model for classification.
 * It supports binary labels, as well as both continuous and categorical features.
 *
 * @note Multiclass labels are not currently supported.
 */
@Since("1.6.0")
class CusGBTClassificationModel(@Since("1.6.0") override val uid: String,
                                val gbdtModel: OldGBTModel,
                                override val numFeatures: Int,
                                val outputNumFeatures: Int)
  extends PredictionModel[Vector, CusGBTClassificationModel]
    with GBTClassifierParams
    with CusGBTClassifierParams with MLWritable with Serializable {

  require(gbdtModel.trees.nonEmpty, "GBTClassificationModel requires at least 1 tree.")
  require(gbdtModel.trees.length == gbdtModel.treeWeights.length, "GBTClassificationModel given trees, treeWeights" +
    s" of non-matching lengths (${gbdtModel.trees.length}, ${gbdtModel.treeWeights.length}, respectively).")


  /**
   * Number of trees in ensemble
   */
  @Since("2.0.0")
  val getNumTrees: Int = trees.length

  @Since("1.4.0")
  def treeWeights: Array[Double] = gbdtModel.treeWeights

  override protected def transformImpl(dataset: Dataset[_]): DataFrame = {
    dataset.withColumn($(gbtGeneratedFeaturesCol), GBDTUitl.genGBDTFeatureUDF(gbdtModel)(col($(featuresCol))))
  }


  /** Number of trees in ensemble */
  val numTrees: Int = trees.length

  @Since("1.4.0")
  override def copy(extra: ParamMap): CusGBTClassificationModel = {
    copyValues(new CusGBTClassificationModel(uid, gbdtModel, numFeatures, outputNumFeatures),
      extra).setParent(parent)
  }

  @Since("1.4.0")
  override def toString: String = {
    s"GBTClassificationModel (uid=$uid) with $numTrees trees"
  }


  // hard coded loss, which is not meant to be changed in the model
  private val loss = getOldLossType

  @Since("2.0.0")
  override def write: MLWriter = new CusGBTClassificationModel.CusGBTClassificationModelWriter(this)

  override protected def predict(features: Vector): Double = 0.0

  def trees: Array[DecisionTreeModel] = gbdtModel.trees
}

object GBDTUitl {
  def getOutputVectorDim(gbdtModel: OldGBTModel): Int = {
    val GBDTMaxIter = gbdtModel.trees.length
    (0 until GBDTMaxIter).map(i => getLeafNodes(gbdtModel.trees(i).topNode).length).sum
  }

  def genGBDTFeatureUDF(gbdtModel: OldGBTModel): UserDefinedFunction = {
    udf { features: Vector => {
      val gbdtFeatures = getGBDTFeatures(gbdtModel, features)
      Vectors.dense(features.toArray ++ gbdtFeatures.toArray)
    }
    }
  }

  def getGBDTFeatures(gbdtModel: OldGBTModel, features: Vector): Vector = {
    val GBDTMaxIter = gbdtModel.trees.length
    val treeLeafArray = new Array[Array[Int]](GBDTMaxIter)
    //拿到每棵树的叶子节点
    for (i <- 0 until GBDTMaxIter) {
      treeLeafArray(i) = getLeafNodes(gbdtModel.trees(i).topNode)
    }
    var newFeature = new Array[Double](0)
    for (i <- 0 until GBDTMaxIter) {
      //预测当前实例落在该树的哪个叶子节点
      val treePredict = predictModify(gbdtModel.trees(i).topNode, features.toDense)
      val treeArray = new Array[Double]((gbdtModel.trees(i).numNodes + 1) / 2)
      treeArray(treeLeafArray(i).indexOf(treePredict)) = 1
      newFeature = newFeature ++ treeArray
    }
    Vectors.dense(newFeature)
  }

  def getLeafNodes(node: OldNode): Array[Int] = {
    var treeLeafNodes = new Array[Int](0)
    if (node.isLeaf) {
      treeLeafNodes = treeLeafNodes :+ node.id
    } else {
      treeLeafNodes = treeLeafNodes ++ getLeafNodes(node.leftNode.get)
      treeLeafNodes = treeLeafNodes ++ getLeafNodes(node.rightNode.get)
    }
    treeLeafNodes
  }

  /**
   *
   * @param node
   * @param features
   * @return
   */
  def predictModify(node: OldNode, features: DenseVector): Int = {
    val split = node.split
    if (node.isLeaf) {
      node.id
    } else {
      if (split.get.featureType == FeatureType.Continuous) {
        if (features(split.get.feature) <= split.get.threshold) {
          predictModify(node.leftNode.get, features)
        } else {
          predictModify(node.rightNode.get, features)
        }
      } else {
        if (split.get.categories.contains(features(split.get.feature))) {
          predictModify(node.leftNode.get, features)
        } else {
          predictModify(node.rightNode.get, features)
        }
      }
    }
  }
}

@Since("2.0.0")
object CusGBTClassificationModel extends MLReadable[CusGBTClassificationModel] {

  @Since("2.0.0")
  override def read: MLReader[CusGBTClassificationModel] = new CusGBTClassificationModelReader

  @Since("2.0.0")
  override def load(path: String): CusGBTClassificationModel = super.load(path)

  private[CusGBTClassificationModel]
  class CusGBTClassificationModelWriter(instance: CusGBTClassificationModel) extends MLWriter {

    private case class Data(inputNumFeatures: Int, outputNumFeatures: Int)

    override protected def saveImpl(path: String): Unit = {
      DefaultParamsWriter.saveMetadata(instance, path, sc)
      val gbtDataPath = new Path(path, "gbtData").toString
      instance.gbdtModel.save(sc, gbtDataPath)
      val data = Data(instance.numFeatures, instance.outputNumFeatures)
      val dataPath = new Path(path, "data").toString
      sparkSession.createDataFrame(Seq(data)).repartition(1).write.parquet(dataPath)
    }
  }

  private class CusGBTClassificationModelReader extends MLReader[CusGBTClassificationModel] {

    /** Checked against metadata when loading model */
    private val className = classOf[CusGBTClassificationModel].getName

    override def load(path: String): CusGBTClassificationModel = {
      val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
      val gbtDataPath = new Path(path, "gbtData").toString
      val gbtModel = OldGBTModel.load(sc, gbtDataPath)
      val dataPath = new Path(path, "data").toString
      val data = sparkSession.read.parquet(dataPath)
        .select("numFeatures", "outputNumFeatures")
        .head()
      val numFeatures = data.getInt(0)
      val outputNumFeatures = data.getInt(1)
      val model = new CusGBTClassificationModel(metadata.uid, gbtModel, numFeatures, outputNumFeatures)
      DefaultParamsReader.getAndSetParams(model, metadata)
      model
    }
  }

}
