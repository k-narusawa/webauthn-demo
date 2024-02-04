package com.knarusawa.webauthndemo.adapter.middleware

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.knarusawa.webauthndemo.adapter.controller.dto.WebauthnAuthenticateFinishPostRequest
import com.knarusawa.webauthndemo.config.WebAuthnConfig
import com.knarusawa.webauthndemo.domain.authenticationToken.WebauthnAssertionAuthenticationToken
import com.knarusawa.webauthndemo.util.logger
import com.webauthn4j.springframework.security.WebAuthnAuthenticationParameters
import com.webauthn4j.springframework.security.WebAuthnAuthenticationRequest
import com.webauthn4j.util.Base64UrlUtil
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
        private val objectMapper = ObjectMapper()
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
//            val req = request.getReader().lines().collect(Collectors.joining("\r\n"));
//            log.info(req)

            val webAuthnRequest = jacksonObjectMapper().readValue(
                request.inputStream,
                WebauthnAuthenticateFinishPostRequest::class.java
            )

            log.info("WebAuthn login challenge id is ${webAuthnRequest.flowId}")
            webAuthnRequest.response.userHandle

            val webAuthnAuthenticationRequest = WebAuthnAuthenticationRequest(
                Base64UrlUtil.decode(webAuthnRequest.rawId),
                Base64UrlUtil.decode(webAuthnRequest.response.clientDataJSON),
                Base64UrlUtil.decode(webAuthnRequest.response.authenticatorData),
                Base64UrlUtil.decode(webAuthnRequest.response.signature),
                null,
            )

            val webAuthnAuthenticationParameters = WebAuthnAuthenticationParameters(
                WebAuthnConfig().serverProperty(challenge = "".toByteArray()),
                true,
                true
            )
            println(webAuthnRequest.response.userHandle)
            val authRequest: AbstractAuthenticationToken =
                WebauthnAssertionAuthenticationToken(
                    flowId = webAuthnRequest.flowId,
                    principal = webAuthnAuthenticationRequest,
                    credentials = webAuthnAuthenticationParameters,
                    userHandle = webAuthnRequest.response.userHandle,
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