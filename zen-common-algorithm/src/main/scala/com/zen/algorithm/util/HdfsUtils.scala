package com.zen.algorithm.util

import java.io.{BufferedOutputStream, File, FileOutputStream, OutputStream}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, FileSystem, Path}

/**
 * @Author: xiongjun
 * @Date: 2020/6/8 11:24
 * @description
 * @reviewer
 */
object HdfsUtils {
  def uploadModel(destPath: String, srcPath: String) = {
    fileSystem.copyFromLocalFile(new Path(srcPath), new Path(destPath))
  }

  final val config: Configuration = new Configuration()
  final val fileSystem: FileSystem = FileSystem.get(config)

  def get(sourcePath: String, descPath: String) = {

    val path = new Path(sourcePath)
    if (!fileSystem.exists(path)) {
      throw new Exception("File " + sourcePath + " does not exists")
    }

    val in: FSDataInputStream = fileSystem.open(path)

    val out: OutputStream = new BufferedOutputStream(new FileOutputStream(new File(descPath)))

    val b: Array[Byte] = new Array[Byte](1024)
    var numBytes: Int = in.read(b)

    while (numBytes > 0) {
      out.write(b, 0, numBytes)
      numBytes = in.read(b)
    }

    in.close()
    out.close()
    fileSystem.close()
  }

  def fileExists(inputPath: String): Boolean = {

    val path = new Path(inputPath)
    fileSystem.exists(path)
  }
}
