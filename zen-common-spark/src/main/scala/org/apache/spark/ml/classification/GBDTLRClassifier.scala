package org.apache.spark.ml.classification

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkException
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.param._
import org.apache.spark.ml.util._
import org.apache.spark.ml.{PredictionModel, Predictor, classification}
import org.apache.spark.mllib.linalg.{DenseVector => OldDenseVector}
import org.apache.spark.mllib.regression.{LabeledPoint => OldLabeledPoint}
import org.apache.spark.mllib.tree.GradientBoostedTrees
import org.apache.spark.mllib.tree.configuration.{FeatureType, Algo => OldAlgo, BoostingStrategy => OldBoostingStrategy, Strategy => OldStrategy}
import org.apache.spark.mllib.tree.impurity.{Variance => OldVariance}
import org.apache.spark.mllib.tree.loss.{LogLoss => OldLogLoss, Loss => OldLoss}
import org.apache.spark.mllib.tree.model.{DecisionTreeModel, GradientBoostedTreesModel, Node => OldNode}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row}

/**
 * @Author: xiongjun
 * @Date: 2020/6/1 14:20
 * @description
 * @reviewer
 */
trait GBDTLRClassifierParams extends Params {
  val checkpointInterval: IntParam = new IntParam(this, "checkpointInterval", "value>=1,如果为-1则关闭checkpoint",
    (interval: Int) => interval == -1 || interval >= 1)

  def getCheckpointInterval: Int = $(checkpointInterval)

  /**
   * 损失函数类型
   */
  val lossType: Param[String] = new Param[String](this, "lossType",
    "GBDT的损失函数类型,目前支持logistic, squared, absolute", (value: String) => value == "logistic")

  def getLossType: String = $(lossType).toLowerCase

  //连续特征离散化最大的bin数,默认32
  val maxBins: IntParam = new IntParam(this, "maxBins", "最大分箱数", ParamValidators.gtEq(2))

  def getMaxBins: Int = $(maxBins)


  //决策树最大深度,>0,gbdt的树的深度不宜太深，默认3
  val maxDepth: IntParam = new IntParam(this, "maxDepth", "决策树最大深度", ParamValidators.gt(0))

  def getMaxDepth: Int = $(maxDepth)

  /**
   * 是否缓存节点Id
   * 如果为true，对于每个实例，将会缓存它对应的node id
   * 如果为false，算法每次都要重新计算
   * 设置为true可以加速训练
   * 默认true
   */
  val cacheNodeIds: BooleanParam = new BooleanParam(this, "cacheNodeIds", "是否缓存每个实例对应的节点Id")

  def getCacheNodeIds: Boolean = $(cacheNodeIds)


  /**
   * 分配给直方图聚合的最大内存，
   * 如果设置太小，那么可能每次迭代只能划分一个节点
   * 默认256MB
   */
  val maxMemoryInMB: IntParam = new IntParam(this, "maxMemoryInMB", "分配给直方图聚合的最大内存", ParamValidators.gt(0))

  def getMaxMemoryInMB: Int = $(maxMemoryInMB)

  /**
   * 在分裂后，每个节点最少需要拥有的实例个数
   * 如果当前分裂使得左节点或者右节点拥有的实例数小于设置的值，则放弃此次分裂
   * 默认为1，数据量较多时可以适当增大，更加鲁棒
   */
  val minInstancesPerNode: IntParam = new IntParam(this, "minInstancesPerNode",
    "每个节点最少拥有的实例数", ParamValidators.gtEq(1))

  def getMinInstancesPerNode: Int = $(minInstancesPerNode)


  /**
   * 最小信息增益
   * 分裂之后的信息增加至少要大于当前设置的值，否则此次分裂无效
   */
  val minInfoGain: DoubleParam = new DoubleParam(this, "minInfoGain", "最小信息增益", ParamValidators.gtEq(0))

  def getMinInfoGain: Double = $(minInfoGain)


  /**
   * 最大迭代次数
   */
  val GBDTMaxIter: IntParam = new IntParam(this, "GBDTMaxIter", "gbdt最大迭代次数", ParamValidators.gtEq(1))

  def getGBDTMaxIter: Int = $(GBDTMaxIter)


  /**
   * 步长，学习率，论文中称为shrinking，值域(0,1]
   */
  val stepSize: DoubleParam = new DoubleParam(this, "stepSize", "learning rate", ParamValidators.gt(0))

  def getStepSize: Double = $(stepSize)


  /**
   * 子采样率
   * 训练每棵决策树从训练集数据采样的比例，值域(0,1]
   * 默认1，适当采样更加鲁棒
   */
  val subsamplingRate: DoubleParam = new DoubleParam(this, "subsamplingRate", "子采样率", ParamValidators.gt(0))

  def getSubsamplingRate: Double = $(subsamplingRate)


  val seed: LongParam = new LongParam(this, "seed", "random seed")

  def getSeed: Long = $(seed)


  /**
   * 弹性网络参数，值域为[0,1]
   * 如果设置为0，就是L2正则化
   * 如果设置为1，就是L1正则化
   * 默认0
   */
  val elasticNetParam: DoubleParam = new DoubleParam(this, "elasticNetParam",
    "弹性网络参数", ParamValidators.inRange(0, 1))

  def getElasticNetParam: Double = $(elasticNetParam)


  /**
   * 描述标签类型
   * 支持auto，binomial,multinomial
   */
  val family: Param[String] = new Param[String](this, "family", "描述标签类型",
    ParamValidators.inArray[String](
      Array("auto", "binomial", "multinomial").map(_.toLowerCase)))

  def getFamily: String = $(family)


  /**
   * 是否拟合截距项
   */
  val fitIntercept: BooleanParam = new BooleanParam(this, "fitIntercept",
    "whether to fit an intercept term")

  /** @group getParam */
  def getFitIntercept: Boolean = $(fitIntercept)


  /**
   * LR最大迭代数
   */
  val LRMaxIter: IntParam = new IntParam(this, "LRMaxIter",
    "maximum number of iterations (>= 0) of LR",
    ParamValidators.gtEq(0))

  /** @group getParam */
  def getLRMaxIter: Int = $(LRMaxIter)


  /**
   * Param for Column name for predicted class conditional probabilities.
   *
   * '''Note''': Not all models output well-calibrated probability estimates!
   *
   * These probabilities should be treated as confidences, not precise probabilities.
   *
   * @group param
   */
  val probabilityCol: Param[String] = new Param[String](this, "probabilityCol",
    "Column name for predicted class conditional probabilities. Note: Not all models output" +
      " well-calibrated probability estimates! These probabilities should be treated as" +
      " confidences, not precise probabilities")

  /** @group getParam */
  def getProbabilityCol: String = $(probabilityCol)


  /**
   * Param for raw prediction (a.k.a. confidence) column name.
   *
   * @group param
   */
  val rawPredictionCol: Param[String] = new Param[String](this, "rawPredictionCol",
    "raw prediction (a.k.a. confidence) column name")

  /** @group getParam */
  def getRawPredictionCol: String = $(rawPredictionCol)


  /**
   * gbdt生成的特征列的名称
   *
   * @group param
   */
  val gbtGeneratedFeaturesCol: Param[String] = new Param[String](this, "gbtGeneratedCol",
    "gbt generated features column name")

  /** @group getParam */
  def getGbtGeneratedFeaturesCol: String = $(gbtGeneratedFeaturesCol)

  /**
   * 输入给gbdt的特征列，gbdt不适合处理高维稀疏向量，
   * 将高维稀疏向量直接喂给LR效果更佳
   */
  val gbdtInputFeaturesCol: Param[String] = new Param[String](this, "gbdtInputFeaturesCol",
    "gbdt input features column name")

  def getGbdtInputFeaturesCol: String = $(gbdtInputFeaturesCol)

  /**
   * 正则化参数
   *
   * @group param
   */
  val regParam: DoubleParam = new DoubleParam(this, "regParam",
    "regularization parameter (>= 0)", ParamValidators.gtEq(0))

  /** @group getParam */
  def getRegParam: Double = $(regParam)


  /**
   * 拟合模型前是否对训练特征进行标准化
   *
   * @group param
   */
  val standardization: BooleanParam = new BooleanParam(this, "standardization",
    "whether to standardize the training features before fitting the model")

  /** @group getParam */
  def getStandardization: Boolean = $(standardization)


  /**
   * 二分类任务设置阈值，对于排序场景没有作用
   * Param for threshold in binary classification prediction, in range [0, 1].
   *
   * @group param
   */
  val threshold: DoubleParam = new DoubleParam(this, "threshold",
    "threshold in binary classification prediction, in range [0, 1]",
    ParamValidators.inRange(0, 1))

  /**
   * If `thresholds` is set with length 2 (i.e., binary classification),
   * this returns the equivalent threshold: {{{ 1 / (1 + thresholds(0) / thresholds(1)) }}}
   * Otherwise, returns `threshold` if set, or its default value if unset.
   *
   * @group getParam
   * @throws IllegalArgumentException if `thresholds` is set to an array of length other than 2.
   */
  def getThreshold: Double = {
    checkThresholdConsistency()
    if (isSet(thresholds)) {
      val ts = $(thresholds)
      require(ts.length == 2, "Logistic Regression getThreshold only applies to" +
        " binary classification, but thresholds has length != 2.  thresholds: " +
        ts.mkString(","))
      1.0 / (1.0 + ts(0) / ts(1))
    } else {
      $(threshold)
    }
  }

  /**
   * Param for Thresholds in multi-class classification to adjust the probability
   * of predicting each class. Array must have length equal to the number of classes,
   * with values > 0 excepting that at most one value may be 0. The class with largest
   * value p/t is predicted, where p is the original probability of that class and t is
   * the class's threshold.
   *
   * @group param
   */
  val thresholds: DoubleArrayParam = new DoubleArrayParam(this, "thresholds",
    "Thresholds in multi-class classification to adjust the probability of predicting" +
      " each class. Array must have length equal to the number of classes, with values > 0" +
      " excepting that at most one value may be 0. The class with largest value p/t is" +
      " predicted, where p is the original probability of that class and t is the class's" +
      " threshold", (t: Array[Double]) => t.forall(_ >= 0) && t.count(_ == 0) <= 1)

  /**
   * Get thresholds for binary or multiclass classification.
   *
   * If `thresholds` is set, return its value.
   * Otherwise, if `threshold` is set, return the equivalent thresholds for binary
   * classification: (1-threshold, threshold).
   * If neither are set, throw an exception.
   *
   * @group getParam
   */
  def getThresholds: Array[Double] = {
    checkThresholdConsistency()
    if (!isSet(thresholds) && isSet(threshold)) {
      val t = $(threshold)
      Array(1 - t, t)
    } else {
      $(thresholds)
    }
  }

  /**
   * Param for the convergence tolerance for iterative algorithms (&gt;= 0).
   *
   * @group param
   */
  val tol: DoubleParam = new DoubleParam(this, "tol",
    "the convergence tolerance for iterative algorithms (>= 0)", ParamValidators.gtEq(0))

  /** @group getParam */
  def getTol: Double = $(tol)

  /**
   * Param for weight column name. If this is not set or empty, we treat all instance
   * weights as 1.0.
   *
   * @group param
   */
  val weightCol: Param[String] = new Param[String](this, "weightCol",
    "weight column name. If this is not set or empty, we treat all instance weights as 1.0")

  /** @group getParam */
  def getWeightCol: String = $(weightCol)

  /**
   * Param for suggested depth for treeAggregate (&gt;= 2).
   *
   * @group expertParam
   */
  val aggregationDepth: IntParam = new IntParam(this, "aggregationDepth",
    "suggested depth for treeAggregate (>= 2)", ParamValidators.gtEq(2))

  /** @group expertGetParam */
  def getAggregationDepth: Int = $(aggregationDepth)

  /**
   * If `threshold` and `thresholds` are both set, ensures they are consistent.
   *
   * @throws IllegalArgumentException if `threshold` and `thresholds` are not equivalent
   */
  private def checkThresholdConsistency(): Unit = {
    if (isSet(threshold) && isSet(thresholds)) {
      val ts = $(thresholds)
      require(ts.length == 2, "Logistic Regression found inconsistent values for threshold and" +
        s" thresholds.  Param threshold is set (${$(threshold)}), indicating binary" +
        s" classification, but Param thresholds is set with length ${ts.length}." +
        " Clear one Param value to fix this problem.")
      val t = 1.0 / (1.0 + ts(0) / ts(1))
      require(math.abs($(threshold) - t) < 1E-5, "Logistic Regression getThreshold found" +
        s" inconsistent values for threshold (${$(threshold)}) and thresholds (equivalent to $t)")
    }
  }

  setDefault(seed -> this.getClass.getName.hashCode.toLong,
    subsamplingRate -> 1.0, GBDTMaxIter -> 20, stepSize -> 0.1, maxDepth -> 5, maxBins -> 32,
    minInstancesPerNode -> 1, minInfoGain -> 0.0, checkpointInterval -> 10, fitIntercept -> true,
    probabilityCol -> "probability", rawPredictionCol -> "rawPrediction", standardization -> true,
    threshold -> 0.5, lossType -> "logistic", cacheNodeIds -> false, maxMemoryInMB -> 256,
    regParam -> 0.0, elasticNetParam -> 0.0, family -> "auto", LRMaxIter -> 100, tol -> 1E-6,
    aggregationDepth -> 2, gbtGeneratedFeaturesCol -> "gbt_generated_features",
    gbdtInputFeaturesCol -> "gbdtInputFeaturesCol")
}

class GBDTLRClassifier(override val uid: String)
  extends Predictor[Vector, GBDTLRClassifier, GBDTLRClassificationModel]
    with GBDTLRClassifierParams with DefaultParamsWritable {

  def this() = this(Identifiable.randomUID("gbdtlr"))

  /** @group setParam */
  def setMaxDepth(value: Int): this.type = set(maxDepth, value)

  /** @group setParam */
  def setMaxBins(value: Int): this.type = set(maxBins, value)

  /** @group setParam */
  def setMinInstancesPerNode(value: Int): this.type = set(minInstancesPerNode, value)

  /** @group setParam */
  def setMinInfoGain(value: Double): this.type = set(minInfoGain, value)

  /** @group setParam */
  def setMaxMemoryInMB(value: Int): this.type = set(maxMemoryInMB, value)

  /** @group setParam */
  def setCacheNodeIds(value: Boolean): this.type = set(cacheNodeIds, value)

  /** @group setParam */
  def setCheckpointInterval(value: Int): this.type = set(checkpointInterval, value)

  /**
   * The impurity setting is ignored for GBT models.
   * Individual trees are build using impurity "Variance."
   *
   * @group setParam
   */
  def setImpurity(value: String): this.type = {
    logWarning("GBDTLRClassifier in the GBDTLRClassifier should NOT be used")
    this
  }

  /** @group setParam */
  def setSubsamplingRate(value: Double): this.type = set(subsamplingRate, value)

  /** @group setParam */
  def setSeed(value: Long): this.type = set(seed, value)

  /** @group setParam */
  def setGBTMaxIter(value: Int): this.type = set(GBDTMaxIter, value)

  /** @group setParam */
  def setStepSize(value: Double): this.type = set(stepSize, value)

  /** @group setParam */
  def setLossType(value: String): this.type = set(lossType, value)

  /** @group setParam */
  def setElasticNetParam(value: Double): this.type = set(elasticNetParam, value)

  /** @group setParam */
  def setFamily(value: String): this.type = set(family, value)

  /** @group setParam */
  def setFitIntercept(value: Boolean): this.type = set(fitIntercept, value)

  /** @group setParam */
  def setLRMaxIter(value: Int): this.type = set(LRMaxIter, value)

  /** @group setParam */
  def setProbabilityCol(value: String): this.type = set(probabilityCol, value)

  /** @group setParam */
  def setRawPredictionCol(value: String): this.type = set(rawPredictionCol, value)

  /** @group setParam */
  def setRegParam(value: Double): this.type = set(regParam, value)

  /** @group setParam */
  def setStandardization(value: Boolean): this.type = set(standardization, value)

  /** @group setParam */
  def setTol(value: Double): this.type = set(tol, value)

  /** @group setParam */
  def setWeightCol(value: String): this.type = set(weightCol, value)

  /** @group setParam */
  def setAggregationDepth(value: Int): this.type = set(aggregationDepth, value)

  /** @group setParam */
  def setGbtGeneratedFeaturesCol(value: String): this.type = set(gbtGeneratedFeaturesCol, value)

  def setGbdtInputFeaturesCol(value: String): this.type = set(gbdtInputFeaturesCol, value)

  /**
   * Set threshold in binary classification, in range [0, 1].
   *
   * If the estimated probability of class label 1 is greater than threshold, then predict 1,
   * else 0. A high threshold encourages the model to predict 0 more often;
   * a low threshold encourages the model to predict 1 more often.
   *
   * '''Note''': Calling this with threshold p is equivalent to calling
   * `setThresholds(Array(1-p, p))`.
   * When `setThreshold()` is called, any user-set value for `thresholds` will be cleared.
   * If both `threshold` and `thresholds` are set in a ParamMap, then they must be
   * equivalent.
   *
   * Default is 0.5.
   *
   * @group setParam
   */
  // TODO: Implement SPARK-11543?
  def setThreshold(value: Double): this.type = {
    if (isSet(thresholds)) clear(thresholds)
    set(threshold, value)
  }

  /**
   * Set thresholds in multiclass (or binary) classification to adjust the probability of
   * predicting each class. Array must have length equal to the number of classes,
   * with values greater than 0, excepting that at most one value may be 0.
   * The class with largest value p/t is predicted, where p is the original probability of that
   * class and t is the class's threshold.
   *
   * '''Note''': When `setThresholds()` is called, any user-set value for `threshold`
   * will be cleared.
   * If both `threshold` and `thresholds` are set in a ParamMap, then they must be
   * equivalent.
   *
   * @group setParam
   */
  def setThresholds(value: Array[Double]): this.type = {
    if (isSet(threshold)) clear(threshold)
    set(thresholds, value)
  }

  private def getOldStrategy(categoricalFeatures: Map[Int, Int]): OldStrategy = {
    val strategy = OldStrategy.defaultStrategy(OldAlgo.Classification)
    strategy.impurity = OldVariance
    strategy.checkpointInterval = getCheckpointInterval
    strategy.maxBins = getMaxBins
    strategy.maxDepth = getMaxDepth
    strategy.maxMemoryInMB = getMaxMemoryInMB
    strategy.minInfoGain = getMinInfoGain
    strategy.minInstancesPerNode = getMinInstancesPerNode
    strategy.useNodeIdCache = getCacheNodeIds
    strategy.numClasses = 2
    strategy.categoricalFeaturesInfo = categoricalFeatures
    strategy.subsamplingRate = getSubsamplingRate
    strategy
  }

  private def getOldLossType: OldLoss = {
    getLossType match {
      case "logistic" => OldLogLoss
      case _ =>
        // Should never happen because of check in setter method.
        throw new RuntimeException(s"GBTClassifier was given bad loss type:" +
          s" $getLossType")
    }
  }


  override def copy(extra: ParamMap): GBDTLRClassifier = defaultCopy(extra)

  override protected def train(dataset: Dataset[_]): GBDTLRClassificationModel = {
    val gbdtInputCategoricalFeatures: Map[Int, Int] = MetadataUtils.getCategoricalFeatures(dataset.schema($(gbdtInputFeaturesCol)))
    val oldDataset: RDD[OldLabeledPoint] = dataset.select(col($(labelCol)), col($(gbdtInputFeaturesCol))).rdd.map {
      case Row(label: Double, features: Vector) =>
        require(label == 0 || label == 1, s"GBTClassifier was given" +
          s" dataset with invalid label $label.  Labels must be in {0,1}; note that" +
          s" GBTClassifier currently only supports binary classification.")
        OldLabeledPoint(label, new OldDenseVector(features.toArray))
    }
    val gbdtInputNumFeatures = oldDataset.first().features.size
    val strategy = getOldStrategy(gbdtInputCategoricalFeatures)
    val boostingStrategy = new OldBoostingStrategy(strategy, getOldLossType, getGBDTMaxIter, getStepSize)
    val instr = Instrumentation.create(this, oldDataset)
    instr.logParams(labelCol, featuresCol, predictionCol, lossType,
      maxDepth, maxBins, GBDTMaxIter, LRMaxIter, maxMemoryInMB, minInfoGain, minInstancesPerNode,
      seed, stepSize, subsamplingRate, cacheNodeIds, checkpointInterval)
    instr.logNumFeatures(gbdtInputNumFeatures)
    instr.logNumClasses(2)
    val gbtModel = GradientBoostedTrees.train(oldDataset, boostingStrategy)

    val addFeatureUDF = GBDTLRUtil.addFeatureUDF(gbtModel)
    val datasetWithCombinedFeatures = dataset.withColumn($(gbtGeneratedFeaturesCol), addFeatureUDF(col($(featuresCol))))

    val lr = new LogisticRegression()
      .setRegParam($(regParam))
      .setElasticNetParam($(elasticNetParam))
      .setMaxIter($(LRMaxIter))
      .setTol($(tol))
      .setLabelCol($(labelCol))
      .setFeaturesCol($(gbtGeneratedFeaturesCol))
      .setFitIntercept($(fitIntercept))
      .setFamily($(family))
      .setStandardization($(standardization))
      .setPredictionCol($(predictionCol))
      .setProbabilityCol($(probabilityCol))
      .setRawPredictionCol($(rawPredictionCol))
      .setAggregationDepth($(aggregationDepth))

    if (isSet(weightCol)) lr.setWeightCol($(weightCol))
    if (isSet(threshold)) lr.setThreshold($(threshold))
    if (isSet(thresholds)) lr.setThresholds($(thresholds))
    val lrModel = lr.fit(datasetWithCombinedFeatures)

    val model = copyValues(new GBDTLRClassificationModel(uid, gbtModel, lrModel).setParent(this))
    val summary = new GBDTLRClassifierTrainingSummary(datasetWithCombinedFeatures, lrModel.summary, gbtModel.trees, gbtModel.treeWeights)
    model.setSummary(Some(summary))
    model
  }
}

object GBDTLRClassifier extends DefaultParamsReadable[GBDTLRClassifier] {

  override def load(path: String): GBDTLRClassifier = super.load(path)
}

class GBDTLRClassificationModel(override val uid: String,
                                val gbtModel: GradientBoostedTreesModel,
                                val lrModel: LogisticRegressionModel)
  extends PredictionModel[Vector, GBDTLRClassificationModel]
    with GBDTLRClassifierParams with MLWritable {

  private var trainingSummary: Option[GBDTLRClassifierTrainingSummary] = None

  private[classification] def setSummary(summary: Option[GBDTLRClassifierTrainingSummary]): this.type = {
    this.trainingSummary = summary
    this
  }

  def hasSummary: Boolean = {
    trainingSummary.nonEmpty
  }

  def summary: GBDTLRClassifierTrainingSummary = trainingSummary.getOrElse {
    throw new SparkException(
      s"No training summary available for the ${this.getClass.getSimpleName}"
    )
  }


  override protected def predict(features: Vector): Double = 0.0

  override def transform(dataset: Dataset[_]): DataFrame = {
    val datasetWithCombinedFeatures = dataset.withColumn($(gbtGeneratedFeaturesCol),
      GBDTLRUtil.addFeatureUDF(gbtModel)(col($(featuresCol))))
    val predictions = lrModel.transform(datasetWithCombinedFeatures)
    predictions
  }

  def evaluate(dataset: Dataset[_]): GBDTLRclassifierSummary = {
    val datasetWithCombinedFeatures = dataset.withColumn($(gbtGeneratedFeaturesCol),
      GBDTLRUtil.addFeatureUDF(gbtModel)(col($(featuresCol)))
    )
    val lrSummary = lrModel.evaluate(datasetWithCombinedFeatures)
    new GBDTLRclassifierSummary(lrSummary)
  }


  override def write: MLWriter = new classification.GBDTLRClassificationModel.GBDTLRClassificationModelWriter(this)

  override def copy(extra: ParamMap): GBDTLRClassificationModel = {
    val copied = copyValues(new GBDTLRClassificationModel(uid, gbtModel, lrModel), extra)
    copied.setSummary(trainingSummary).setParent(this.parent)
  }
}

object GBDTLRClassificationModel extends MLReadable[GBDTLRClassificationModel] {
  override def read: MLReader[GBDTLRClassificationModel] = new GBDTLRClassificationModelReader

  override def load(path: String): GBDTLRClassificationModel = super.load(path)

  private[GBDTLRClassificationModel] class GBDTLRClassificationModelWriter(
                                                                            instance: GBDTLRClassificationModel) extends MLWriter {
    override protected def saveImpl(path: String): Unit = {
      DefaultParamsWriter.saveMetadata(instance, path, sc)
      val gbtDataPath = new Path(path, "gbtData").toString
      instance.gbtModel.save(sc, gbtDataPath)
      val lrDataPath = new Path(path, "lrData").toString
      instance.lrModel.save(lrDataPath)
    }
  }

  private class GBDTLRClassificationModelReader extends MLReader[GBDTLRClassificationModel] {
    private val className: String = classOf[GBDTLRClassificationModel].getName

    override def load(path: String): GBDTLRClassificationModel = {
      val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
      val gbtDataPath = new Path(path, "gbtData").toString
      val lrDataPath = new Path(path, "lrData").toString
      val gbtModel = GradientBoostedTreesModel.load(sc, gbtDataPath)
      val lrModel = LogisticRegressionModel.load(lrDataPath)
      val model = new GBDTLRClassificationModel(metadata.uid, gbtModel, lrModel)
      DefaultParamsReader.getAndSetParams(model, metadata)
      model
    }
  }

}

class GBDTLRClassifierTrainingSummary(@transient val newDataset: DataFrame,
                                      val logRegSummary: LogisticRegressionTrainingSummary,
                                      val gbtTrees: Array[DecisionTreeModel],
                                      val treeWeights: Array[Double]) extends Serializable {}

class GBDTLRclassifierSummary(val binaryLogisticRegressionSummary: LogisticRegressionSummary) extends Serializable {}

object GBDTLRUtil {

  def addFeatureUDF(gbdtModel: GradientBoostedTreesModel): UserDefinedFunction = {
    udf { (features: Vector) =>
      val gbdtFeatures = getGBDTFeatures(gbdtModel, features)
      Vectors.dense(features.toArray ++ gbdtFeatures.toArray)
    }
  }

  def getGBDTFeatures(gbdtModel: GradientBoostedTreesModel, features: Vector): Vector = {
    val GBDTMaxIter = gbdtModel.trees.length
    val oldFeatures = new OldDenseVector(features.toArray)
    val treeLeafArray = new Array[Array[Int]](GBDTMaxIter)
    for (i <- 0 until GBDTMaxIter) {
      treeLeafArray(i) = getLeafNodes(gbdtModel.trees(i).topNode)
    }
    var newFeature = new Array[Double](0)
    for (i <- 0 until GBDTMaxIter) {
      val treePredict = predictModify(gbdtModel.trees(i).topNode, oldFeatures.toDense)
      val treeArray = new Array[Double]((gbdtModel.trees(i).numNodes + 1) / 2)
      treeArray(treeLeafArray(i).indexOf(treePredict)) = 1
      newFeature = newFeature ++ treeArray
    }
    Vectors.dense(newFeature)
  }

  /**
   *
   * @param node
   * @param features
   * @return
   */
  def predictModify(node: OldNode, features: OldDenseVector): Int = {
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
}
