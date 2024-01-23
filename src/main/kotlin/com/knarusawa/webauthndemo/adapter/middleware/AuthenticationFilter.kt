package com.knarusawa.webauthndemo.adapter.middleware

import com.knarusawa.webauthndemo.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository

class AuthenticationFilter(
    private val authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {
    private var customSecurityContextRepository: SecurityContextRepository? = null
    private val log = logger()

    init {
        this.customSecurityContextRepository = DelegatingSecurityContextRepository(
            RequestAttributeSecurityContextRepository(),
            HttpSessionSecurityContextRepository()
        )
        super.setSecurityContextRepository(customSecurityContextRepository)
    }

    override fun attemptAuthentication(
        request: HttpServletRequest, response: HttpServletResponse
    ): Authentication {
        log.info("Start Authentication")
        saveContext(request, response)

        val username = obtainUsername(request)
        val password = obtainPassword(request)

        log.info("username: $username")

        val authRequest = UsernamePasswordAuthenticationToken(username, password)

        setDetails(request, authRequest)
        return this.authenticationManager.authenticate(authRequest)
    }

    // https://qiita.com/k-taichi/items/1787144fcad5d7bc8e41
    private fun saveContext(request: HttpServletRequest, response: HttpServletResponse?) {
        val securityContext = SecurityContextHolder.getContext()
        SecurityContextHolder.setContext(securityContext)
        customSecurityContextRepository!!.saveContext(securityContext, request, response)
    }
}