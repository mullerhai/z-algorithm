package com.zen.spark.util

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
    fileSystem.copyFromLocalFile(new Path(srcPath),new Path(destPath))
  }

  final val config:Configuration = new Configuration()
  final val fileSystem: FileSystem = FileSystem.get(config)
  def get(sourcePath:String,descPath:String) = {

    val path = new Path(sourcePath)
    if (!fileSystem.exists(path)) {
      throw new Exception("File " + sourcePath + " does not exists")
    }

    val in: FSDataInputStream = fileSystem.open(path)

    val out: OutputStream = new BufferedOutputStream(new FileOutputStream(new File(descPath)))

    val b: Array[Byte] = new Array[Byte](1024)
    var numBytes: Int = in.read(b)

    while (numBytes > 0){
      out.write(b, 0, numBytes)
      numBytes = in.read(b)
    }

    in.close()
    out.close()
    fileSystem.close()
  }

  def fileExists(inputPath:String):Boolean = {

    val path = new Path(inputPath)
    fileSystem.exists(path)
  }

  def getFileName(path: String): String = {
    path.substring(path.lastIndexOf('/') + 1, path.length)
  }

  def listdir(folder:String):Array[String] = {
    val folderPath = new Path(folder)
    if (fileSystem.exists(folderPath)&&fileSystem.isDirectory(folderPath)){
      val fileStatusArray = fileSystem.listStatus(folderPath)
      val dataDate = fileStatusArray.filter(_.isDirectory).map(_.getPath).map(path=>getFileName(path.getName))
      dataDate
    }else{
      Array[String]()
    }
  }
}
