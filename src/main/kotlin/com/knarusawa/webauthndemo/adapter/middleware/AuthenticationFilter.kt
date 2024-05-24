package com.knarusawa.webauthndemo.adapter.middleware

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.knarusawa.webauthndemo.adapter.controller.dto.WebauthnAuthenticateFinishPostRequest
import com.knarusawa.webauthndemo.application.finishWebAuthnLogin.FinishWebAuthnLoginInputData
import com.knarusawa.webauthndemo.domain.authenticationToken.WebauthnAssertionAuthenticationToken
import com.knarusawa.webauthndemo.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AbstractAuthenticationToken
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
    companion object {
        private val log = logger()
    }

    private var customSecurityContextRepository: SecurityContextRepository? = null

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
        log.info("METHOD: [${request.method}], URL: [${request.requestURI}]")
        saveContext(request, response)

        if (request.requestURI == "/api/v1/webauthn/login") {
            val webAuthnRequest = jacksonObjectMapper().readValue(
                request.inputStream,
                WebauthnAuthenticateFinishPostRequest::class.java
            )

            log.info("WebAuthn login challenge is ${webAuthnRequest.challenge}")

            val credentials = FinishWebAuthnLoginInputData(
                challenge = webAuthnRequest.challenge,
                credentialId = webAuthnRequest.rawId,
                clientDataJSON = webAuthnRequest.response.clientDataJSON,
                authenticatorData = webAuthnRequest.response.authenticatorData,
                signature = webAuthnRequest.response.signature,
                userHandle = webAuthnRequest.response.userHandle
            )

            val authRequest: AbstractAuthenticationToken =
                WebauthnAssertionAuthenticationToken(
                    principal = webAuthnRequest.challenge,
                    credentials = credentials,
                )

//            setDetails(request, authRequest)

            return this.authenticationManager.authenticate(authRequest)
        }

        val username = obtainUsername(request)
        val password = obtainPassword(request)

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