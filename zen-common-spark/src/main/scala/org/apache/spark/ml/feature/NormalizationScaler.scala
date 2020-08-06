package org.apache.spark.ml.feature

import org.apache.hadoop.fs.Path
import org.apache.spark.ml.param.shared.{HasInputCols, HasOutputCols}
import org.apache.spark.ml.param.{DoubleArrayParam, DoubleParam, IntParam, ParamMap, Params}
import org.apache.spark.ml.util.{DefaultParamsReader, DefaultParamsWritable, DefaultParamsWriter, Identifiable, MLReadable, MLReader, MLWritable, MLWriter, SchemaUtils}
import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{avg, col, lit, max, udf}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{Column, DataFrame, Dataset}
import org.apache.spark.ml.feature.NormalizationScalerModel.NormalizationScalerModelWriter

import scala.collection.immutable

/**
 * @Author: xiongjun
 * @Date: 2020/6/10 15:28
 * @description 归一化组件
 * @reviewer
 */
trait NormalizationScalerParams extends Params with HasInputCols with HasOutputCols {

  def setInputCols(values: Array[String]): this.type = set(inputCols, values)

  def setOutputCols(values: Array[String]): this.type = set(outputCols, values)

  /**
   * lower bound after transformation, shared by all features
   *
   * @group param
   */
  val min: DoubleArrayParam = new DoubleArrayParam(this, "min",
    "lower bound of the output feature range")

  /** @group getParam */
  def getMin: Array[Double] = $(min)

  def setMin(value: Array[Double]): this.type = set(min, value)

  /**
   * upper bound after transformation, shared by all features
   *
   * @group param
   */
  val max: DoubleArrayParam = new DoubleArrayParam(this, "max",
    "upper bound of the output feature range")


  /** @group getParam */
  def getMax: Array[Double] = $(max)

  def setMax(value: Array[Double]): this.type = set(max, value)

  /** 归一化=1  标准化=2  */
  var algorithmType: IntParam = new IntParam(this, "algorithmType"
    , "selected algorithm type: 1=Normalization , 2=Standardization")

  def getAlgorithmType: Int = $(algorithmType)
  /** 归一化=1  标准化=2  */
  def setAlgorithmType(value: Int): this.type = set(algorithmType, value)


  /** 归一化：最大最小值 */
  val Normalization = 1
  /** 标准化 */
  val Standardization = 2

  /** Validates and transforms the input schema. */
  protected def validateAndTransformSchema(schema: StructType): StructType = {
    val length = $(min).length
    require($(min).length == $(max).length, s"The specified min(${$(min)}).length is not equal to max(${$(max)}).length")

    for (i <- 0 until length) {
      require($(min)(i) < $(max)(i), s"The specified min(${$(min)})'length is larger or equal to max(${$(max)})")
    }
    require($(min).length != 0 || $(max).length != 0
      , s"The maxArr or minArr is null")

    $(inputCols).foreach(SchemaUtils.checkNumericType(schema, _))
    val contains = $(outputCols).filter(schema.fieldNames.contains(_))
    require(contains.length == 0,
      s"Output columns ${contains.mkString(",", "[", "]")} already exists.")
    val outputFields = schema.fields ++ $(outputCols).map(colName => StructField(colName, DoubleType, nullable = true))
    StructType(outputFields)
  }
}

class NormalizationScaler(override val uid: String)
  extends Estimator[NormalizationScalerModel]
    with NormalizationScalerParams
    with DefaultParamsWritable {
  def this() = this(Identifiable.randomUID("NormalizationScaler"))
  override def fit(dataset: Dataset[_]): NormalizationScalerModel = {
    transformSchema(dataset.schema) //校验schema
    val inputColNames = $(inputCols)
    val dict =
    //判断算法类型 1.归一化 2.标准化
      if (getAlgorithmType == Normalization) {
        inputColNames.indices.map(index => {
          val colName = col(inputColNames(index)).cast(DoubleType)
          val max = dataset.select(colName).rdd.map(_.getDouble(0)).max()
          val min = dataset.select(colName).rdd.map(_.getDouble(0)).min()
          index -> Seq(max, min)
        }).toMap
      } else {
        inputColNames.indices.map(index => {
          val colName = col(inputColNames(index)).cast(DoubleType)

          val frame = dataset.agg(avg(colName).as("avg"))

          val avgNum = frame.first()(0).asInstanceOf[Double]
          val variance = dataset.select(colName).rdd.map(v => {
            (avgNum - v.getDouble(0)) * (avgNum - v.getDouble(0))
          }).reduce(_ + _)
          val length = dataset.select(colName).count()
          val std = sqr(variance / length)
          index -> Seq(avgNum, std)
        }).toMap
      }
    println("DICT" + dict)

    val model = new NormalizationScalerModel(uid, dict)
    copyValues(model)
  }

  /**
   * 求平方差
   *
   * @param n
   * @return
   */
  def sqr(n: Double):Double = {
    var k = 1.0; //可任取  while(Math.abs(k*k-n)>1e-9) //double不能用==比较
    {
      k = (k + n / k) / 2
    }
    k
  }


  override def copy(extra: ParamMap): Estimator[NormalizationScalerModel] = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }
}

object NormalizationScalerModel extends MLReadable[NormalizationScalerModel] {

  private[NormalizationScalerModel]
  class NormalizationScalerModelWriter(instance: NormalizationScalerModel) extends MLWriter {

    private case class Data(dict: Map[Int, Seq[Double]])

    override protected def saveImpl(path: String): Unit = {
      DefaultParamsWriter.saveMetadata(instance, path, sc)
      val data = Data(instance.dict)
      val dataPath = new Path(path, "data").toString
      sparkSession.createDataFrame(Seq(data)).repartition(1).write.parquet(dataPath)
    }
  }

  private class NormalizationScalerModelReader extends MLReader[NormalizationScalerModel] {

    private val className = classOf[NormalizationScalerModel].getName

    override def load(path: String): NormalizationScalerModel = {
      val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
      val dataPath = new Path(path, "data").toString
      val data = sparkSession.read.parquet(dataPath)
        .select("dict")
        .head()

      val dict = data.getAs[Map[Int, Seq[Double]]](0)
      val model = new NormalizationScalerModel(metadata.uid, dict)
      DefaultParamsReader.getAndSetParams(model, metadata)
      model
    }
  }
  override def read: MLReader[NormalizationScalerModel] = new NormalizationScalerModelReader

  override def load(path: String): NormalizationScalerModel = super.load(path)
}



class NormalizationScalerModel(override val uid: String, val dict: Map[Int, Seq[Double]])
  extends Model[NormalizationScalerModel] with NormalizationScalerParams with MLWritable {

  override def copy(extra: ParamMap): NormalizationScalerModel = defaultCopy(extra)

  override def write: MLWriter = new NormalizationScalerModelWriter(this)

  override def transform(dataset: Dataset[_]): DataFrame = {

    val inputColNames = $(inputCols)
    val outputColNames = $(outputCols)
    val outPutColEncodes =
    //获取执行器
      if (getAlgorithmType == Normalization) {
        normalizationActuator(inputColNames, outputColNames)
      } else {
        standardizationActuator(inputColNames, outputColNames)
      }
    dataset.withColumns(outputColNames, outPutColEncodes)

  }

  /**
   * 归一化 执行器
   *
   * @param inputColNames
   * @param outputColNames
   * @return
   */
  private def normalizationActuator(inputColNames: Array[String], outputColNames: Array[String]) = {
    val outPutColEncodes = inputColNames.indices.map(index => {
      val inputColName = inputColNames(index)
      val outputColName = outputColNames(index)
      val column = normalizationActuatorUdf(col(inputColName).cast(DoubleType), lit(index)).as(outputColName)
      column
    })
    outPutColEncodes
  }

  /**
   * 归一化执行器UDF
   * @return
   */
  private def normalizationActuatorUdf: UserDefinedFunction = {
    udf { (value: Double, index: Int) => {
      val scoreParameter = dict.get(index)
      val max = scoreParameter.get.head
      val min = scoreParameter.get(1)
      //边界处理
      borderProcessor(index, (value - min) / (max - min))
    }
    }

  }

  /**
   * 标准化 执行器
   *
   * @param inputColNames
   * @param outputColNames
   * @return
   */
  private def standardizationActuator(inputColNames: Array[String], outputColNames: Array[String]) = {
    val outPutColEncodes = inputColNames.indices.map(index => {
      val inputColName = inputColNames(index)
      val outputColName = outputColNames(index)
      val column = standardizationActuatorUdf(col(inputColName).cast(DoubleType), lit(index)).as(outputColName)
      column
    })
    outPutColEncodes
  }



  /**
   * 边界处理器
   *
   * @param index inputcols 索引
   * @param x     算法输出的值
   * @return
   */
  private def borderProcessor(index: Int, x: Double) = {
    //边界处理
    val minBorder = getMin(index)
    val maxBorder = getMax(index)
    if (x > maxBorder) {
      maxBorder
    } else if (x < minBorder) {
      minBorder
    } else {
      x
    }
  }

  private def standardizationActuatorUdf: UserDefinedFunction = {
    udf { (value: Double, index: Int) => {
      val scoreParameter = dict.get(index)
      val avg = scoreParameter.get.head
      var std = scoreParameter.get.last
      //边界处理
      if (std == 0){
        std = 1e-8
      }
      borderProcessor(index, (value - avg) / std)

    }
    }
  }


  override def transformSchema(schema: StructType): StructType = validateAndTransformSchema(schema)


}
