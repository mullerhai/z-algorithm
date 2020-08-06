package com.zen.mleap.util

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

/**
 * @Author: xiongjun
 * @Date: 2020/6/4 18:10
 * @description
 * @reviewer
 */
object SerializationUtil {
  def serialization(obj: Any): Array[Byte] = {
    val bo = new ByteArrayOutputStream()
    val oo = new ObjectOutputStream(bo)
    oo.writeObject(obj)
    val bytes = bo.toByteArray
    bo.close()
    oo.close()
    bytes
  }

  def deserialization(bytes: Array[Byte]): Any = {
    val bi = new ByteArrayInputStream(bytes)
    val oi = new ObjectInputStream(bi)

    val obj = oi.readObject()
    bi.close()
    oi.close()
    obj
  }
}
