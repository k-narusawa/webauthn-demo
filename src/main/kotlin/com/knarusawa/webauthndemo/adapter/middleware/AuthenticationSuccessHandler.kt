package com.knarusawa.webauthndemo.adapter.middleware

import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import com.knarusawa.webauthndemo.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class AuthenticationSuccessHandler() :
    org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    private val log = logger()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        log.info("onAuthenticationSuccess")

        val user = authentication?.principal as LoginUserDetails

        val session = request?.session
        session?.setAttribute("user", user)
        response?.status = HttpServletResponse.SC_OK
    }
}