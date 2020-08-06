package org.apache.spark.ml.feature

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkException
import org.apache.spark.ml.feature.CountEncoderModel.CountEncoderModelWriter
import org.apache.spark.ml.param.shared.{HasHandleInvalid, HasInputCols, HasOutputCols}
import org.apache.spark.ml.param.{Param, ParamMap, ParamValidators, Params}
import org.apache.spark.ml.util._
import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{col, lit, udf}
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/10 18:13
 * @description count编码，只针对二分类问题，其他问题不可使用
 * @reviewer
 */
trait CountEncoderBase extends Params with HasInputCols with HasOutputCols with HasHandleInvalid {
  def setInputCols(values: Array[String]): this.type = set(inputCols, values)

  def setOutputCols(values: Array[String]): this.type = set(outputCols, values)

  val labelCol: Param[String] = new Param[String](this, "labelCol", "click label column name")

  def getLabelCol: String = $(labelCol)

  def setLabelCol(value: String): this.type = set(labelCol, value)

  override val handleInvalid: Param[String] = new Param[String](this, "handleInvalid",
    "How to handle invalid data during transform(). " +
      "Options are 'keep' (invalid data return 0) " +
      "or error (throw an error). Note that this Param is only used during transform; " +
      "during fitting, invalid data will result in an error.",
    ParamValidators.inArray(MultiHotEncoderEstimator.supportedHandleInvalids))

  def setHandleInvalid(value: String): this.type = set(handleInvalid, value)
  setDefault(handleInvalid, MultiHotEncoderEstimator.ERROR_INVALID)

  protected def validateAndTransformSchema(schema: StructType): StructType = {
    val inputColNames = $(inputCols)
    val outputColNames = $(outputCols)
    require(isDefined(labelCol), "must specified labelCol")

    inputColNames.foreach(SchemaUtils.checkNumericType(schema, _))
    require(inputColNames.length == outputColNames.length, s"The number of input columns ${inputColNames.length} must be the same as the number of " +
      s"output columns ${outputColNames.length}.")
    val containFields = outputColNames.filter(schema.fieldNames.contains(_))
    require(containFields.length == 0, s"Output columns ${containFields.mkString(",", "[", "]")} already exists.")
    val outputFields = schema.fields ++ outputColNames.map(colName => StructField(colName, IntegerType, nullable = true))
    StructType(outputFields)
  }

}

class CountEncoderEstimator(override val uid: String)
  extends Estimator[CountEncoderModel] with CountEncoderBase with DefaultParamsWritable {
  def this() = this(Identifiable.randomUID("count_encoder"))

  override def fit(dataset: Dataset[_]): CountEncoderModel = {
    transformSchema(dataset.schema)
    val inputColNames = $(inputCols)
    val labelColName = $(labelCol)
    val dicts = inputColNames.map(colName => {
      dataset.select(Array(col(colName).cast(IntegerType), col(labelColName).cast(IntegerType)): _*).rdd.map(row => {
        val value = row.getInt(0)
        val label = row.getInt(1)
        (value, label)
      }).reduceByKey(_ + _).collect().toMap
    })
    val model = new CountEncoderModel(uid, dicts).setParent(this)
    copyValues(model)
  }

  override def copy(extra: ParamMap): Estimator[CountEncoderModel] = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }
}

object CountEncoderEstimator extends DefaultParamsReadable[CountEncoderEstimator] {
  override def load(path: String): CountEncoderEstimator = super.load(path)
}

class CountEncoderModel(override val uid: String, val dicts: Array[Map[Int, Int]]) extends Model[CountEncoderModel] with CountEncoderBase with MLWritable {
  override def copy(extra: ParamMap): CountEncoderModel = {
    val copied = new CountEncoderModel(uid, dicts)
    copyValues(copied, extra).setParent(parent)
  }

  override def write: MLWriter = new CountEncoderModelWriter(this)

  override def transform(dataset: Dataset[_]): DataFrame = {
    val inputColNames = $(inputCols)
    val outputColNames = $(outputCols)
    val encodedColumns = inputColNames.indices.map { idx =>
      val inputColName = inputColNames(idx)
      val outputColName = outputColNames(idx)

      encoder(col(inputColName).cast(IntegerType), lit(idx))
        .as(outputColName)
    }
    dataset.withColumns(outputColNames, encodedColumns)
  }

  private def encoder: UserDefinedFunction = {
    val keepInvalid = getHandleInvalid == OneHotEncoderEstimator.KEEP_INVALID
    // The udf performed on input data. The first parameter is the input value. The second
    // parameter is the index in inputCols of the column being encoded.
    udf { (value: Int, colIdx: Int) =>
      val dict = dicts(colIdx)
      if (dict.contains(value)) {
        dict(value)
      } else {
        if (keepInvalid) {
          0
        } else {
          throw new SparkException(s"Unseen value: $value. To handle unseen values, " +
            s"set Param handleInvalid to ${OneHotEncoderEstimator.KEEP_INVALID}.")
        }
      }
    }
  }

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }
}

object CountEncoderModel extends MLReadable[CountEncoderModel] {

  private[CountEncoderModel]
  class CountEncoderModelWriter(instance: CountEncoderModel) extends MLWriter {

    private case class Data(dicts: Array[Map[Int, Int]])

    override protected def saveImpl(path: String): Unit = {
      DefaultParamsWriter.saveMetadata(instance, path, sc)
      val data = Data(instance.dicts)
      val dataPath = new Path(path, "data").toString
      sparkSession.createDataFrame(Seq(data)).repartition(1).write.parquet(dataPath)
    }
  }

  private class CountEncoderModelReader extends MLReader[CountEncoderModel] {

    private val className = classOf[CountEncoderModel].getName

    override def load(path: String): CountEncoderModel = {
      val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
      val dataPath = new Path(path, "data").toString
      val data = sparkSession.read.parquet(dataPath)
        .select("dicts")
        .head()

      val dicts = data.getAs[Array[Map[Int, Int]]](0)
      val model = new CountEncoderModel(metadata.uid, dicts)
      DefaultParamsReader.getAndSetParams(model, metadata)
      model
    }
  }

  override def read: MLReader[CountEncoderModel] = new CountEncoderModelReader

  override def load(path: String): CountEncoderModel = super.load(path)
}

