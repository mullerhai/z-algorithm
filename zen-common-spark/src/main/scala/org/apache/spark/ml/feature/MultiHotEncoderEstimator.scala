package org.apache.spark.ml.feature

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkException
import org.apache.spark.ml.feature.MultiHotEncoderModel.MultiHotEncoderModelWriter
import org.apache.spark.ml.linalg.{VectorUDT, Vectors}
import org.apache.spark.ml.param.shared.{HasHandleInvalid, HasInputCols, HasOutputCols}
import org.apache.spark.ml.param.{Param, ParamMap, ParamValidators, Params}
import org.apache.spark.ml.util._
import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{col, lit, udf}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 11:42
 * @description
 * @reviewer
 */
/** Private trait for params and common methods for MultiHotEncoderEstimator and MultiHotEncoderModel */
trait MultiHotEncoderBase extends Params with HasHandleInvalid
  with HasInputCols with HasOutputCols {

  def setInputCols(values: Array[String]): this.type = set(inputCols, values)

  def setOutputCols(values: Array[String]): this.type = set(outputCols, values)

  override val handleInvalid: Param[String] = new Param[String](this, "handleInvalid",
    "How to handle invalid data during transform(). " +
      "Options are 'keep' (invalid data presented as an extra categorical feature) " +
      "or error (throw an error). Note that this Param is only used during transform; " +
      "during fitting, invalid data will result in an error.",
    ParamValidators.inArray(MultiHotEncoderEstimator.supportedHandleInvalids))

  setDefault(handleInvalid, MultiHotEncoderEstimator.ERROR_INVALID)

  val delimiter = new Param[String](this, "delimiter", "delimiter for feature")

  def getDelimiter: String = $(delimiter)

  setDefault(delimiter, ",")

  protected def validateAndTransformSchema(schema: StructType): StructType = {
    val inputColNames = $(inputCols)
    val outputColNames = $(outputCols)

    require(inputColNames.length == outputColNames.length,
      s"The number of input columns ${inputColNames.length} must be the same as the number of " +
        s"output columns ${outputColNames.length}.")

    // 输入列的类型必须是StringType
    inputColNames.foreach(SchemaUtils.checkColumnType(schema, _, StringType))

    val outputFields = outputColNames.map { case outputColName =>
      StructField(outputColName, new VectorUDT, nullable = true)
    }
    outputFields.foldLeft(schema) { case (newSchema, outputField) =>
      SchemaUtils.appendColumn(newSchema, outputField)
    }
  }

}

class MultiHotEncoderEstimator(override val uid: String)
  extends Estimator[MultiHotEncoderModel] with MultiHotEncoderBase with DefaultParamsWritable {
  def this() = this(Identifiable.randomUID("multiHotEncoder"))


  def setHandleInvalid(value: String): this.type = set(handleInvalid, value)

  def setDelimiter(value: String): this.type = set(delimiter, value)

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }

  override def fit(dataset: Dataset[_]): MultiHotEncoderModel = {
    transformSchema(dataset.schema)
    val inputColNames = $(inputCols)
    val dict = inputColNames.indices.map(index => {
      val colMap = dataset.select(col(inputColNames(index)).cast(StringType)).rdd.flatMap(row => {
        row.getString(0).split($(delimiter))
      }).distinct.zipWithIndex.map(valueAndIdx => {
        valueAndIdx._1 -> valueAndIdx._2.toInt
      }).collect().toMap

      index -> colMap
    }).toMap

    val model = new MultiHotEncoderModel(uid, dict).setParent(this)
    copyValues(model)
  }

  override def copy(extra: ParamMap): Estimator[MultiHotEncoderModel] = defaultCopy(extra)

}

object MultiHotEncoderEstimator extends DefaultParamsReadable[MultiHotEncoderEstimator] {
  private[feature] val KEEP_INVALID: String = "keep"

  private[feature] val ERROR_INVALID: String = "error"

  private[feature] val supportedHandleInvalids: Array[String] = Array(KEEP_INVALID, ERROR_INVALID)

  override def load(path: String): MultiHotEncoderEstimator = super.load(path)
}


class MultiHotEncoderModel(override val uid: String, val dict: Map[Int, Map[String, Int]])
  extends Model[MultiHotEncoderModel] with MultiHotEncoderBase with MLWritable {

  def setHandleInvalid(value: String): this.type = set(handleInvalid, value)

  def setDelimiter(value: String): this.type = set(delimiter, value)


  override def copy(extra: ParamMap): MultiHotEncoderModel = {
    val copied = new MultiHotEncoderModel(uid, dict)
    copyValues(copied, extra).setParent(parent)
  }

  override def write: MLWriter = new MultiHotEncoderModelWriter(this)

  override def transform(dataset: Dataset[_]): DataFrame = {
    transformSchema(dataset.schema, logging = true)
    val inputColNames = $(inputCols)
    val outputColNames = $(outputCols)
    val encodedColumns = inputColNames.indices.map { idx =>
      val inputColName = inputColNames(idx)
      val outputColName = outputColNames(idx)

      encoder(col(inputColName).cast(StringType), lit(idx))
        .as(outputColName)
    }

    dataset.withColumns(outputColNames, encodedColumns)
  }

  private def encoder: UserDefinedFunction = {
    val keepInvalid = getHandleInvalid == OneHotEncoderEstimator.KEEP_INVALID

    // The udf performed on input data. The first parameter is the input value. The second
    // parameter is the index in inputCols of the column being encoded.
    udf { (label: String, colIdx: Int) =>
      val valueAndIdx = dict(colIdx)
      val values = label.split($(delimiter)).distinct
      val indices = values.map(value => {
        if (valueAndIdx.contains(value)) {
          valueAndIdx(value)
        } else {
          if (keepInvalid) {
            valueAndIdx.size
          } else {
            throw new SparkException(s"Unseen value: $value. To handle unseen values, " +
              s"set Param handleInvalid to ${OneHotEncoderEstimator.KEEP_INVALID}.")
          }
        }
      })
      val vectorValues = indices.map(_ => 1.0)
      if (keepInvalid) {
        Vectors.sparse(valueAndIdx.size + 1, indices.sorted, vectorValues)
      } else {
        Vectors.sparse(valueAndIdx.size, indices.sorted, vectorValues)
      }
    }
  }

  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }
}

object MultiHotEncoderModel extends MLReadable[MultiHotEncoderModel] {

  private[MultiHotEncoderModel]
  class MultiHotEncoderModelWriter(instance: MultiHotEncoderModel) extends MLWriter {

    private case class Data(dict: Map[Int, Map[String, Int]])

    override protected def saveImpl(path: String): Unit = {
      DefaultParamsWriter.saveMetadata(instance, path, sc)
      val data = Data(instance.dict)
      val dataPath = new Path(path, "data").toString
      sparkSession.createDataFrame(Seq(data)).repartition(1).write.parquet(dataPath)
    }
  }

  private class MultiHotEncoderModelReader extends MLReader[MultiHotEncoderModel] {

    private val className = classOf[MultiHotEncoderModel].getName

    override def load(path: String): MultiHotEncoderModel = {
      val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
      val dataPath = new Path(path, "data").toString
      val data = sparkSession.read.parquet(dataPath)
        .select("dict")
        .head()

      val dict = data.getAs[Map[Int, Map[String, Int]]](0)
      val model = new MultiHotEncoderModel(metadata.uid, dict)
      DefaultParamsReader.getAndSetParams(model, metadata)
      model
    }
  }

  override def read: MLReader[MultiHotEncoderModel] = new MultiHotEncoderModelReader

  override def load(path: String): MultiHotEncoderModel = super.load(path)
}
