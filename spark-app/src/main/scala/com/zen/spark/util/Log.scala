package com.zen.spark.util

import org.apache.spark.internal.Logging

/**
 * @Author: xiongjun
 * @Date: 2020/6/8 10:18
 * @description
 * @reviewer
 */
trait Log extends Logging {
  val sign = "ALGORITHM LOG "

  override protected def logInfo(msg: => String): Unit = {
    val tmpMsg = sign + msg
    super.logInfo(tmpMsg)
  }

  override protected def logDebug(msg: => String): Unit = {
    val tmpMsg = sign + msg
    super.logDebug(tmpMsg)
  }

  override protected def logTrace(msg: => String): Unit = {
    val tmpMsg = sign + msg
    super.logTrace(tmpMsg)
  }

  override protected def logWarning(msg: => String): Unit = {
    val tmpMsg = sign + msg
    super.logWarning(tmpMsg)
  }

  override protected def logError(msg: => String): Unit = {
    val tmpMsg = sign + msg
    super.logError(tmpMsg)
  }

  override protected def logInfo(msg: => String, throwable: Throwable): Unit = {
    val tmpMsg = sign + msg
    super.logInfo(tmpMsg, throwable)
  }

  override protected def logDebug(msg: => String, throwable: Throwable): Unit = {
    val tmpMsg = sign + msg
    super.logDebug(tmpMsg, throwable)
  }

  override protected def logTrace(msg: => String, throwable: Throwable): Unit = {
    val tmpMsg = sign + msg
    super.logTrace(tmpMsg, throwable)
  }

  override protected def logWarning(msg: => String, throwable: Throwable): Unit = {
    val tmpMsg = sign + msg
    super.logWarning(tmpMsg, throwable)
  }

  override protected def logError(msg: => String, throwable: Throwable): Unit = {
    val tmpMsg = sign + msg
    super.logError(tmpMsg, throwable)
  }
}
