package org.apache.spark.ml.feature

import org.apache.hadoop.fs.Path
import org.apache.spark.ml.param.shared.{HasHandleInvalid, HasInputCols, HasOutputCols}
import org.apache.spark.ml.param._
import org.apache.spark.ml.util._
import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/5 15:08
 * @description
 * @reviewer
 */
trait BucketizerExtendBase extends Params with HasHandleInvalid
  with HasInputCols with HasOutputCols {
  def setInputCols(values: Array[String]): this.type = set(inputCols, values)

  def setOutputCols(values: Array[String]): this.type = set(outputCols, values)

  override val handleInvalid: Param[String] = new Param[String](this, "handleInvalid",
    "how to handle invalid entries. Options are skip (filter out rows with invalid values), " +
      "error (throw an error), or keep (keep invalid values in a special additional bucket).",
    ParamValidators.inArray(BucketizerExtendEstimator.supportedHandleInvalids))

  def setHandleInvalid(value: String): this.type = set(handleInvalid, value)

  setDefault(handleInvalid, Bucketizer.ERROR_INVALID)

  /**
   * 分桶规则，目前支持等频，等宽
   */
  val rule: Param[String] = new Param[String](this, "rule", "bucketizer rule",
    ParamValidators.inArray(Array(BucketizerRule.equivalentwidth, BucketizerRule.equifrequent))
  )

  def getRule: String = $(rule)

  def setRule(value: String): this.type = set(rule, value)

  setDefault(rule, BucketizerRule.equivalentwidth)

  val numOfBucket: IntParam = new IntParam(this, "numOfBucket", "number of bucket",
    ParamValidators.gt(1))

  def getNumOfBucket: Int = $(numOfBucket)

  def setNumOfBucket(value: Int): this.type = set(numOfBucket, value)

  protected def validateAndTransformSchema(schema: StructType): StructType = {
    val inputColNames = $(inputCols)
    val outputColNames = $(outputCols)
    require(inputColNames.length == outputColNames.length, s"BucketizerExtendEstimator $this has mismatched Params " +
      s"for multi-column transform.  Params (inputCols, outputCols) should have " +
      s"equal lengths, but they have different lengths: " +
      s"(${inputColNames.length}, ${outputColNames.length}).")
    inputColNames.foreach(SchemaUtils.checkNumericType(schema, _))

    val outputFields = outputColNames.map { case outputColName =>
      StructField(outputColName, DoubleType, true)
    }
    outputFields.foldLeft(schema) { case (newSchema, outputField) =>
      SchemaUtils.appendColumn(newSchema, outputField)
    }
  }

}

class BucketizerExtendEstimator(override val uid: String)
  extends Estimator[BucketizerExtendModel] with BucketizerExtendBase with DefaultParamsWritable {

  def this() = this(Identifiable.randomUID("bucketizer_extend"))

  override def fit(dataset: Dataset[_]): BucketizerExtendModel = {
    transformSchema(dataset.schema)
    val inputColNames = $(inputCols)
    val splitsArray = inputColNames.map(colName => {
      val doubleRdd = dataset.select(col(colName).cast(DoubleType)).rdd.map(row => {
        row.getDouble(0)
      })
      if ($(rule).equals(BucketizerRule.equivalentwidth)) {
        val maxVal = doubleRdd.max()
        val minVal = doubleRdd.min()
        val length = maxVal - minVal
        val width = length / $(numOfBucket)
        Range(1, $(numOfBucket)).map(i => {
          i * width + minVal
        }).toArray
      } else {
        val count = doubleRdd.count()
        val freq = count / $(numOfBucket)
        var i = 1
        doubleRdd.collect().sorted.zipWithIndex.map(valAndIdx => {
          if (i < $(numOfBucket) && valAndIdx._2 == i * freq) {
            i += 1
            valAndIdx._1
          } else {
            -1
          }
        }).filter(_ > 0)
      }
    })
    val model = new BucketizerExtendModel(uid, splitsArray).setParent(this)
    copyValues(model)
  }

  override def copy(extra: ParamMap): Estimator[BucketizerExtendModel] = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }
}

object BucketizerExtendEstimator extends DefaultParamsReadable[BucketizerExtendEstimator] {
  private[feature] val SKIP_INVALID: String = "skip"
  private[feature] val ERROR_INVALID: String = "error"
  private[feature] val KEEP_INVALID: String = "keep"
  private[feature] val supportedHandleInvalids: Array[String] =
    Array(SKIP_INVALID, ERROR_INVALID, KEEP_INVALID)

  override def load(path: String): BucketizerExtendEstimator = super.load(path)
}

object BucketizerRule {
  final val equifrequent = "eqf" //等频
  final val equivalentwidth = "eqw" //等宽
}

class BucketizerExtendModel(override val uid: String, val splitsArray: Array[Array[Double]])
  extends Model[BucketizerExtendModel] with BucketizerExtendBase with MLWritable {
  override def copy(extra: ParamMap): BucketizerExtendModel = {
    val copied = new BucketizerExtendModel(uid, splitsArray)
    copyValues(copied, extra).setParent(parent)
  }

  override def write: MLWriter = ???

  override def transform(dataset: Dataset[_]): DataFrame = {
    transformSchema(dataset.schema)
    val (inputColumns, outputColumns) = ($(inputCols), $(outputCols))
    val filteredDataset = dataset.na.drop(inputColumns).toDF

    val bucketizers: Seq[UserDefinedFunction] = splitsArray.zipWithIndex.map { case (splits, idx) =>
      udf { (feature: Double) =>
        binarySearchForBuckets(splits, feature)
      }.withName(s"bucketizer_$idx")
    }
    val newCols = inputColumns.zipWithIndex.map { case (inputCol, idx) =>
      bucketizers(idx)(filteredDataset(inputCol).cast(DoubleType))
    }
    filteredDataset.withColumns(outputColumns, newCols)
  }

  private def binarySearchForBuckets(splits: Array[Double], feature: Double): Double = {
    if (feature < splits.head) {
      0
    } else if (feature >= splits.last) {
      $(numOfBucket) - 1
    } else {
      BucketizerExtendUtil.binarySearch(splits, feature)
    }
  }

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }
}

object BucketizerExtendModel extends MLReadable[BucketizerExtendModel] {

  private[BucketizerExtendModel]
  class BucketizerExtendModelWriter(instance: BucketizerExtendModel) extends MLWriter {

    private case class Data(splitsArray: Array[Array[Double]])

    override protected def saveImpl(path: String): Unit = {
      DefaultParamsWriter.saveMetadata(instance, path, sc)
      val data = Data(instance.splitsArray)
      val dataPath = new Path(path, "data").toString
      sparkSession.createDataFrame(Seq(data)).repartition(1).write.parquet(dataPath)
    }
  }

  private class BucketizerExtendModelReader extends MLReader[BucketizerExtendModel] {

    private val className = classOf[BucketizerExtendModel].getName

    override def load(path: String): BucketizerExtendModel = {
      val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
      val dataPath = new Path(path, "data").toString
      val data = sparkSession.read.parquet(dataPath)
        .select("splitsArray")
        .head()

      val splitsArray = data.getAs[Array[Array[Double]]](0)
      val model = new BucketizerExtendModel(metadata.uid, splitsArray)
      DefaultParamsReader.getAndSetParams(model, metadata)
      model
    }
  }


  override def load(path: String): BucketizerExtendModel = super.load(path)

  override def read: MLReader[BucketizerExtendModel] = new BucketizerExtendModelReader
}

object BucketizerExtendUtil {
  def binarySearch(splits: Array[Double], feature: Double): Int = {
    var low = 0
    var high = splits.length - 1
    while (low <= high) {
      val middle = (low + high) >> 1
      if (feature == splits(middle)) {
        return middle + 1
      } else if (feature < splits(middle)) {
        if (feature > splits(middle - 1)) {
          return middle
        }
        high = middle - 1
      } else {
        if (feature < splits(middle + 1)) {
          return middle + 1
        }
        low = middle + 1
      }
    }
    -1
  }
}