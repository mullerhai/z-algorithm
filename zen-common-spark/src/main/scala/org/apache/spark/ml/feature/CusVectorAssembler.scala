package org.apache.spark.ml.feature

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkException
import org.apache.spark.annotation.Since
import org.apache.spark.ml.attribute.{Attribute, AttributeGroup, NumericAttribute, UnresolvedAttribute}
import org.apache.spark.ml.feature.CusVectorAssemblerModel.CusVectorAssemblerModelWriter
import org.apache.spark.ml.linalg.{Vector, VectorUDT, Vectors}
import org.apache.spark.ml.param._
import org.apache.spark.ml.param.shared._
import org.apache.spark.ml.util._
import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Row}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ArrayBuilder}

/**
 * @Author: morris
 * @Date: 2020/6/23 16:33
 * @description
 * @reviewer
 */
/**
 * A feature transformer that merges multiple columns into a vector column.
 */
trait CusVectorAssemblerBase extends Params with HasHandleInvalid
  with HasInputCols with HasOutputCol {
  //  @Since("1.4.0")
  //  def this() = this(Identifiable.randomUID("vecAssembler"))
  val  INVALID_KEY = "##"

  val inputDoubleCols: StringArrayParam = new StringArrayParam(this, "inputDoubleCols",
    "double cols")
  val inputIntCols: StringArrayParam = new StringArrayParam(this, "inputIntCols",
    "int cols")
  val inputVectorCols: StringArrayParam = new StringArrayParam(this, "inputVectorCols",
    "vector cols")

  def setInputDoubleCols(value: Array[String]): this.type = set(inputDoubleCols, value)

  def setInputIntCols(value: Array[String]): this.type = set(inputIntCols, value)

  def setInputVectorCols(value: Array[String]): this.type = set(inputVectorCols, value)


  setDefault(inputIntCols, Array("##"))
  setDefault(inputDoubleCols, Array("##"))
  setDefault(inputVectorCols, Array("##"))
  /*  val inputVectorDims: IntArrayParam = new IntArrayParam(this, "inputVectorDims",
      "input vector type dim array")
    val outputVectorDim: IntParam = new IntParam(this, "outputVectorDim",
      "output vector dim ")

    protected def setInputVectorDims(value: Array[Int]): this.type = set(inputVectorDims, value)

    protected def setOutputVectorDim(value: Int): this.type = set(outputVectorDim, value)

    def getInputVectorDims(): Array[Int] = $(inputVectorDims)

    def getOutputVectorDim(): Int = $(outputVectorDim)*/


  def getInputDoubleCols(): Array[String] = $(inputDoubleCols)

  def getInputIntCols(): Array[String] = $(inputIntCols)

  def getInputVectorCols(): Array[String] = $(inputVectorCols)

  //按double int vector 顺序排列
  def getInputAllCols: Array[String] = {
      val array = new ArrayBuffer[String]
      if (!getInputDoubleCols().head.equals(INVALID_KEY)) array.insertAll(array.length,$(inputDoubleCols))
      if (!getInputIntCols().head.equals(INVALID_KEY)) array.insertAll(array.length,$(inputIntCols))
      if (!getInputVectorCols().head.equals(INVALID_KEY)) array.insertAll(array.length,$(inputVectorCols))
    set(inputCols, array.toArray)
    $(inputCols)
  }

  /** @group setParam */
  /*   @Since("1.4.0")
     def setInputCols(value: Array[String]): this.type ={} set(inputCols, value)*/

  /** @group setParam */
  @Since("1.4.0")
  def setOutputCol(value: String): this.type = set(outputCol, value)

  protected def validateAndTransformSchema(schema: StructType): StructType = {
//    val intCols = getInputIntCols()
    val doubleCols = getInputDoubleCols()
    val vectorCols = getInputVectorCols()
    val outputColName = $(outputCol)
//    if (!intCols.head.equals(INVALID_KEY)) intCols.foreach(SchemaUtils.checkColumnType(schema, _, IntegerType))
    if (!doubleCols.head.equals(INVALID_KEY)) doubleCols.foreach(SchemaUtils.checkColumnType(schema, _, DoubleType))
    if (!vectorCols.head.equals(INVALID_KEY)) vectorCols.foreach(SchemaUtils.checkColumnType(schema, _, new VectorUDT))


    require(outputColName != null,
      s"The number of input columns  must be the same as the number of " +
        s"output columns ${outputColName.length}.")

    // 输入列的类型必须是StringType
    //    inputColNames.foreach(SchemaUtils.checkColumnType(schema, _, StringType))
    val outputFields = schema.fields ++ Array(StructField(outputColName, new VectorUDT, nullable = true))
    StructType(outputFields)
  }
}

class CusVectorAssemblerEstimator(override val uid: String)
  extends Estimator[CusVectorAssemblerModel] with CusVectorAssemblerBase with DefaultParamsWritable {
  def this() = this(Identifiable.randomUID("vectorAssembler"))


  override def transformSchema(schema: StructType): StructType = {
    validateAndTransformSchema(schema)
  }

  override def fit(dataset: Dataset[_]): CusVectorAssemblerModel = {
    transformSchema(dataset.schema, logging = true)
    //计算输出总维度和向量类型的每列维度


    var outputDim = 0
    if (!getInputDoubleCols().head.equals(INVALID_KEY)) outputDim += getInputDoubleCols().length
    if (!getInputIntCols().head.equals(INVALID_KEY)) outputDim += getInputIntCols().length
    val inputVectorDims = if (!getInputVectorCols().head.equals(INVALID_KEY)) {
      val vectorCols = getInputVectorCols()
      val inputVectorDims = vectorCols.indices.map(idx => {
        dataset.select(col(vectorCols(idx))).head().get(0).asInstanceOf[Vector].size
      }).toArray
      outputDim += inputVectorDims.sum
      inputVectorDims
    } else {
      Array(0)
    }
    val model = new CusVectorAssemblerModel(uid, inputVectorDims, outputDim).setParent(this)
    copyValues(model)
  }

  override def copy(extra: ParamMap): Estimator[CusVectorAssemblerModel] = defaultCopy(extra)

}


object CusVectorAssemblerEstimator extends DefaultParamsReadable[CusVectorAssemblerEstimator] {
  private[feature] val KEEP_INVALID: String = "keep"

  private[feature] val ERROR_INVALID: String = "error"

  private[feature] val supportedHandleInvalids: Array[String] = Array(KEEP_INVALID, ERROR_INVALID)

  override def load(path: String): CusVectorAssemblerEstimator = super.load(path)
}

@Since("1.4.0")
class CusVectorAssemblerModel @Since("1.4.0")(
                                               @Since("1.4.0") override val uid: String
                                               , val inputVectorDims: Array[Int]
                                               , val outputVectorDim: Int
                                             )
  extends Model[CusVectorAssemblerModel] with CusVectorAssemblerBase with MLWritable {


  @Since("2.0.0")
  override def transform(dataset: Dataset[_]): DataFrame = {
    transformSchema(dataset.schema, logging = true)

    // Schema transformation.
    val schema = dataset.schema
    lazy val first = dataset.toDF.first()
//    val cols = getInputAllCols
    val attrs = getInputAllCols.flatMap { c =>
      val field = schema(c)
      val index = schema.fieldIndex(c)
      field.dataType match {
        case DoubleType =>
          val attr = Attribute.fromStructField(field)
          // If the input column doesn't have ML attribute, assume numeric.
          if (attr == UnresolvedAttribute) {
            Some(NumericAttribute.defaultAttr.withName(c))
          } else {
            Some(attr.withName(c))
          }
        case _: NumericType | BooleanType =>
          // If the input column type is a compatible scalar type, assume numeric.
          Some(NumericAttribute.defaultAttr.withName(c))
        case _: VectorUDT =>
          val group = AttributeGroup.fromStructField(field)
          if (group.attributes.isDefined) {
            // If attributes are defined, copy them with updated names.
            group.attributes.get.zipWithIndex.map { case (attr, i) =>
              if (attr.name.isDefined) {
                // TODO: Define a rigorous naming scheme.
                attr.withName(c + "_" + attr.name.get)
              } else {
                attr.withName(c + "_" + i)
              }
            }
          } else {
            // Otherwise, treat all attributes as numeric. If we cannot get the number of attributes
            // from metadata, check the first row.
            val numAttrs = group.numAttributes.getOrElse(first.getAs[Vector](index).size)
            Array.tabulate(numAttrs)(i => NumericAttribute.defaultAttr.withName(c + "_" + i))
          }
        case otherType =>
          throw new SparkException(s"CusVectorAssembler does not support the $otherType type")
      }
    }
    val metadata = new AttributeGroup($(outputCol), attrs).toMetadata()

    // Data transformation.
    val assembleFunc = udf { r: Row =>
      CusVectorAssemblerModel.assemble(r.toSeq: _*)
    }.asNondeterministic()
    val args = getInputAllCols.map { c =>
      schema(c).dataType match {
        case DoubleType => dataset(c)
        case _: VectorUDT => dataset(c)
        case _: NumericType | BooleanType => dataset(c).cast(DoubleType).as(s"${c}_double_$uid")
      }
    }

    dataset.select(col("*"), assembleFunc(struct(args: _*)).as($(outputCol), metadata))
  }

  @Since("1.4.0")
  override def transformSchema(schema: StructType): StructType = {
    val inputColNames = getInputAllCols
    val outputColName = $(outputCol)
    val incorrectColumns = inputColNames.flatMap { name =>
      schema(name).dataType match {
        case _: NumericType | BooleanType => None
        case t if t.isInstanceOf[VectorUDT] => None
        case other => Some(s"Data type $other of column $name is not supported.")
      }
    }
    if (incorrectColumns.nonEmpty) {
      throw new IllegalArgumentException(incorrectColumns.mkString("\n"))
    }
    if (schema.fieldNames.contains(outputColName)) {
      throw new IllegalArgumentException(s"Output column $outputColName already exists.")
    }
    StructType(schema.fields :+  StructField(outputColName, new VectorUDT, nullable = true))
  }

  @Since("1.4.1")
  override def copy(extra: ParamMap): CusVectorAssemblerModel = {
    val copied = new CusVectorAssemblerModel(uid, inputVectorDims, outputVectorDim)
    copyValues(copied, extra).setParent(parent)
  }

  override def write: _root_.org.apache.spark.ml.util.MLWriter = new CusVectorAssemblerModelWriter(this)
}

object CusVectorAssemblerModel extends MLReadable[CusVectorAssemblerModel] {

  private[CusVectorAssemblerModel]
  def assemble(vv: Any*): Vector = {
    val indices = mutable.ArrayBuilder.make[Int]
    val values = mutable.ArrayBuilder.make[Double]
    var cur = 0
    vv.foreach {
      case v: Double =>
        if (v != 0.0) {
          indices += cur
          values += v
        }
        cur += 1
      case vec: Vector =>
        vec.foreachActive { case (i, v) =>
          if (v != 0.0) {
            indices += cur + i
            values += v
          }
        }
        cur += vec.size
      case null =>
        // TODO: output Double.NaN?
        throw new SparkException("Values to assemble cannot be null.")
      case o =>
        throw new SparkException(s"$o of type ${o.getClass.getName} is not supported.")
    }
    Vectors.sparse(cur, indices.result(), values.result()).compressed
  }


  class CusVectorAssemblerModelWriter(instance: CusVectorAssemblerModel) extends MLWriter {

    private case class Data(inputVectorDims: Array[Int], outputVectorDim: Int)

    override protected def saveImpl(path: String): Unit = {
      DefaultParamsWriter.saveMetadata(instance, path, sc)
      val data = Data(instance.inputVectorDims, instance.outputVectorDim)
      val dataPath = new Path(path, "data").toString
      sparkSession.createDataFrame(Seq(data)).repartition(1).write.parquet(dataPath)
    }
  }

  private class CusVectorAssemblerModelReader extends MLReader[CusVectorAssemblerModel] {

    private val className = classOf[CusVectorAssemblerModel].getName

    override def load(path: String): CusVectorAssemblerModel = {
      val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
      val dataPath = new Path(path, "data").toString
      val data = sparkSession.read.parquet(dataPath)
        .select("inputVectorDims")
        .head()

      val data2 = sparkSession.read.parquet(dataPath)
        .select("outputVectorDim")
        .head()
      val inputVectorDims = if(data.getAs[Seq[Int]](0) == null) {
        Array(0) //默认值
      }else{
        data.getAs[Seq[Int]](0).toArray
      }
      val outputVectorDim = data2.getAs[Int](0)
      val model = new CusVectorAssemblerModel(metadata.uid, inputVectorDims, outputVectorDim)
      DefaultParamsReader.getAndSetParams(model, metadata)
      model
    }
  }

  override def read: MLReader[CusVectorAssemblerModel] = new CusVectorAssemblerModelReader

  override def load(path: String): CusVectorAssemblerModel = super.load(path)
}



