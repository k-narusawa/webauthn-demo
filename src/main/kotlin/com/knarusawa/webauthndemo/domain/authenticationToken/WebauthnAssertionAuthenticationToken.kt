package com.knarusawa.webauthndemo.domain.authenticationToken

import com.webauthn4j.springframework.security.WebAuthnAuthenticationParameters
import com.webauthn4j.springframework.security.WebAuthnAuthenticationRequest
import org.springframework.security.authentication.AbstractAuthenticationToken

class WebauthnAssertionAuthenticationToken(
    val flowId: String,
    val userHandle: String,
    val principal: WebAuthnAuthenticationRequest,
    val credentials: WebAuthnAuthenticationParameters,
) : AbstractAuthenticationToken(listOf()) {

    override fun getPrincipal(): Any {
        return this.principal
    }

    override fun getCredentials(): Any {
        return this.credentials
    }


}