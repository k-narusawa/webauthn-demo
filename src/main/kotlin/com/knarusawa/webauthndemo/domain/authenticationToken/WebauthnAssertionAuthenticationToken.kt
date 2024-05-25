package com.knarusawa.webauthndemo.domain.authenticationToken

import com.knarusawa.webauthndemo.application.finishWebAuthnAuthenticate.FinishWebAuthnAuthenticationInputData
import org.springframework.security.authentication.AbstractAuthenticationToken

class WebauthnAssertionAuthenticationToken(
    val principal: String,
    val credentials: FinishWebAuthnAuthenticationInputData,
) : AbstractAuthenticationToken(listOf()) {

    override fun getPrincipal(): Any {
        return this.principal
    }

    override fun getCredentials(): Any {
        return this.credentials
    }
}