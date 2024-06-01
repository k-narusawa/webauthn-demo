package com.knarusawa.webauthndemo.adapter.middleware

import com.knarusawa.webauthndemo.adapter.exception.PasswordNotMatchException
import com.knarusawa.webauthndemo.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureHandler() :
  org.springframework.security.web.authentication.AuthenticationFailureHandler {
  private val log = logger()
  override fun onAuthenticationFailure(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    exception: AuthenticationException?
  ) {
    val remoteAddr = request?.remoteAddr
    val userAgent = request?.getHeader("User-Agent")

    when (exception) {
      is PasswordNotMatchException -> {
        log.warn("認証失敗 username: ${exception.username}")
      }

      else -> {
        exception?.printStackTrace()
        log.error("想定外の認証失敗")
      }
    }

    response?.status = HttpServletResponse.SC_UNAUTHORIZED
    response?.contentType = "application/json"
    response?.writer?.write("{\"message\":\"unauthorized\"}")
  }
}