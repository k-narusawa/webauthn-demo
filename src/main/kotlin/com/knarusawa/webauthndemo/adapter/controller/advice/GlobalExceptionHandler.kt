package com.knarusawa.webauthndemo.adapter.controller.advice

import com.knarusawa.webauthndemo.adapter.controller.dto.ErrorResponse
import com.knarusawa.webauthndemo.util.logger
import com.webauthn4j.validator.exception.ValidationException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = logger()

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(
        ex: ValidationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.error("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        log.warn(ex.stackTraceToString())
        return ResponseEntity(
            ErrorResponse.of(
                exception = ex,
                errorMessage = ex.message ?: "予期せぬエラーが発生しました",
                logLevel = LogLevel.ERROR
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.error("message: ${ex.message}, cause: ${ex.cause}, ex: $ex")
        log.warn(ex.stackTraceToString())
        return ResponseEntity(
            ErrorResponse.of(
                exception = ex,
                errorMessage = ex.message ?: "予期せぬエラーが発生しました",
                logLevel = LogLevel.ERROR
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}