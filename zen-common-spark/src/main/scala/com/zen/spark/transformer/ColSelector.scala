package com.zen.spark.transformer

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param._
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset}

/**
 * @Author: xiongjun
 * @Date: 2020/6/12 11:10
 * @description 选择保留指定的列，节约资源
 * @reviewer
 */
class ColSelector(override val uid: String) extends Transformer with DefaultParamsWritable {

  final val cols = new StringArrayParam(this, "cols", "column names that preserved")

  def setCols(value: Array[String]): this.type = set(cols, value)

  def getCols: Array[String] = $(cols)

  // 默认保留全部列
  setDefault(cols, Array[String]("*"))

  def this() = this(Identifiable.randomUID("colselector"))

  override def transform(dataset: Dataset[_]): DataFrame = {
    logInfo(s"cols: ${$(cols).toSeq.mkString(",")}")
    // 筛选出保留列保留下来
    if ($(cols).length >= 1 && $(cols).head != "*") {
      dataset.select($(cols).map(dataset.col): _*)
    } else {
      dataset.toDF()
    }
  }

  override def copy(extra: ParamMap): Transformer = {
    defaultCopy(extra)
  }

  override def transformSchema(schema: StructType): StructType = {
    val names = schema.fieldNames
    // 参数不能为空
    require($(cols).length > 0, "cols' length must be greater than 0")
    if ($(cols).head == "*") {
      // 默认保留所有列
      setCols(names)
    } else {
      //cols 必须存在
      $(cols).foreach(col => {
        logInfo("select cols: " + col)
        require(names.contains(col), s"item of cols must be existed (false item: $col)")
      })
    }
    StructType(schema.fields.filter(field => $(cols).contains(field.name)))
  }

}

object ColSelector extends DefaultParamsReadable[ColSelector] {}
