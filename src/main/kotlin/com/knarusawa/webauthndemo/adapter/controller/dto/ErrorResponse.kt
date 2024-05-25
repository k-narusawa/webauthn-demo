package com.knarusawa.webauthndemo.adapter.controller.dto

import com.knarusawa.webauthndemo.util.logger
import org.springframework.boot.logging.LogLevel

data class ErrorResponse(

  val errorMessage: String
) {
  private val log = logger()

  companion object {
    fun of(exception: Exception, errorMessage: String, logLevel: LogLevel) =
      ErrorResponse(errorMessage = errorMessage)
        .also { it.log(exception, logLevel, errorMessage) }
  }

  private fun log(ex: Exception, logLevel: LogLevel, message: String) {
    when (logLevel) {
      LogLevel.OFF -> Unit
      LogLevel.TRACE -> log.trace("$message, ex: ${ex.message} cause:${ex.cause}")
      LogLevel.DEBUG -> log.debug("$message, ex: ${ex.message} cause:${ex.cause}")
      LogLevel.INFO -> log.info("$message, ex: ${ex.message} cause:${ex.cause}")
      LogLevel.WARN -> log.warn("$message, ex: ${ex.message} cause:${ex.cause}")
      LogLevel.ERROR -> log.error("$message, ex: ${ex.message} cause:${ex.cause}")
      LogLevel.FATAL -> log.error("$message, ex: ${ex.message} cause:${ex.cause}")
    }
  }
}