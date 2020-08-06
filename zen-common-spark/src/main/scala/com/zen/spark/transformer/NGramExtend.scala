package com.zen.spark.transformer

import org.apache.spark.ml.feature.NGram
import org.apache.spark.ml.param.BooleanParam
import org.apache.spark.ml.util.{DefaultParamsReadable, Identifiable}

/**
 * @Author: xiongjun
 * @Date: 2020/6/1 15:22
 * @description
 * @reviewer
 */
class NGramExtend(override val uid: String) extends NGram {
  val blank: BooleanParam = new BooleanParam(this, "isBlank", "whether there are blank bewteen characters")

  def setBlank(value: Boolean): this.type = set(blank, value)

  def getBlank: Boolean = $(blank)

  setDefault(blank, false)

  def this() = this(Identifiable.randomUID("ngram_extend"))

  override def setN(value: Int): NGramExtend.this.type = set(n, value)

  override def setInputCol(value: String): NGramExtend = set(inputCol, value)

  override def setOutputCol(value: String): NGramExtend = set(outputCol, value)

  override def createTransformFunc: Seq[String] => Seq[String] = (input: Seq[String]) => {

    if ($(blank)) {
      input.iterator.sliding($(n)).withPartial(false).map(_.mkString(" ")).toSeq
    } else {
      input.iterator.sliding($(n)).withPartial(false).map(_.mkString).toSeq
    }
  }
}

object NGramExtend extends DefaultParamsReadable[NGramExtend] {
  override def load(path: String): NGramExtend = super.load(path)
}
