package com.zen.algorithm.util

import java.io.{PrintWriter, UnsupportedEncodingException}
import java.security.MessageDigest

import com.zen.spark.transformer.MetaStorage
import org.apache.commons.codec.binary.{Base64, StringUtils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.ml.PipelineModel

import scala.util.matching.Regex

/**
 * @Author: xiongjun
 * @Date: 2020/6/8 11:13
 * @description
 * @reviewer
 */
object ForecastUtil {
  //base64加密
  def encrypt(str: String): String = {
    var ciphertext = ""
    try {
      ciphertext = Base64.encodeBase64String(StringUtils.getBytesUtf8(str))
    } catch {
      case e: UnsupportedEncodingException =>
        e.printStackTrace()
    }
    ciphertext
  }

  // base64解密
  def decryption(str: String): String = {
    var plaintext = ""
    if (str != null) {
      try {
        val b = Base64.decodeBase64(str)
        plaintext = new String(b, "UTF-8")
      } catch {
        case e: Exception =>
          e.printStackTrace()
      }
    }
    plaintext
  }

  // MD5 加密
  def MD5(input: String): String = {
    var md5: MessageDigest = null
    try {
      md5 = MessageDigest.getInstance("MD5")
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    val byteArray: Array[Byte] = input.getBytes
    val md5Bytes: Array[Byte] = md5.digest(byteArray)
    var hexValue: String = ""
    var i: Integer = 0
    for (i <- md5Bytes.indices) {
      val str: Int = md5Bytes(i).toInt & 0xff
      if (str < 16) {
        hexValue = hexValue + "0"
      }
      hexValue = hexValue + Integer.toHexString(str)
    }
    hexValue.toString
  }

  def isNotNull(str: Object): Boolean = {
    str != null && !"".equals(str.toString)
  }


  /**
   * 向 HDFS 写文件
   *
   * @param hdfsPath 文件保存的 HDFS 路径
   * @param content  文件内容
   * @return
   */
  def writeToHDFS(hdfsPath: String, content: String): Boolean = {
    val path = new Path(hdfsPath)
    val fs = FileSystem.get(new Configuration())
    val output = fs.create(path)
    val writer = new PrintWriter(output)
    writer.write(s"$content")
    writer.close()
    fs.exists(path)
  }

  /**
   * 新增列名可能与原始数据列名出线重复时，对新增列名进行重命名。规范：name#{number start from 2}(e.g. features#2)
   *
   * @param name
   * @param existingNames
   * @return
   */
  def renameDuplicatedColName(name: String, existingNames: Array[String]): String = {

    // 按照已有的有序序号，递归找到最小的合法序号
    def findSeq(curSeq: Int, seqs: Array[String]): Int = {
      if (seqs.length == 0 || !curSeq.toString.equals(seqs(0))) {
        curSeq
      } else {
        findSeq(curSeq + 1, seqs.drop(1))
      }
    }

    if (!existingNames.contains(name)) {
      // 无冲突时，返回原名
      name
    } else {
      // 冲突时，按照规范进行重命名
      // 提取出列名后面的序号，并从小到大排序（格式：列名#序号，#后面为非数字类型也不影响，e.g. (a,a#3,a#2,a#4,a#ab) => (2,3,4,ab)）
      val orderedSeq = existingNames
        .filter(n => n.startsWith(s"$name#") && n.split("#").length > 1)
        .map(_.split("#")(1))
        .sortWith(_ < _)

      s"$name#${findSeq(2, orderedSeq)}"
    }
  }

  /**
   * 返回模型的元信息
   *
   * @param model
   * @return
   */
  def getMetaFromModel(model: PipelineModel): MetaStorage = {
    model.stages(0).asInstanceOf[MetaStorage]
  }

  /**
   * 根据正则表达式提取值，匹配不上时返回空字符串
   *
   * @param reg
   * @param str
   * @param groupIdx
   * @return
   */
  def extractDataByReg(reg: Regex, str: String, groupIdx: Int): String = {
    val isMatch = reg.findAllIn(str).matchData.hasNext
    if (isMatch) {
      reg.findAllIn(str).matchData.next.group(groupIdx)
    } else {
      ""
    }
  }

  /**
   * 对 Map 值从高到低进行排序，并返回取 topn 结果
   *
   * @param map
   * @param topn
   * @return
   */
  def sortMapValAndTakeTopn(map: Map[String, Double], topn: Int): Map[String, Double] = {
    var res = Map[String, Double]()
    val resSeq = if (topn > 0) {
      map.toSeq.sortWith(_._2 > _._2).take(topn)
    } else {
      map.toSeq.sortWith(_._2 > _._2)
    }
    resSeq.foreach(r => res += (r._1 -> r._2))
    res
  }

  /**
   * 根据文件完整路径（目录/文件名）拿到文件名
   *
   * @param path
   * @return
   */
  def getFileName(path: String): String = {
    path.substring(path.lastIndexOf('/') + 1, path.length)
  }
}
