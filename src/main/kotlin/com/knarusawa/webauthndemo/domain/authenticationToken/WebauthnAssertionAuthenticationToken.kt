package com.knarusawa.webauthndemo.domain.authenticationToken

import com.knarusawa.webauthndemo.application.finishWebAuthnLogin.FinishWebAuthnLoginInputData
import org.springframework.security.authentication.AbstractAuthenticationToken

class WebauthnAssertionAuthenticationToken(
    val principal: String,
    val credentials: FinishWebAuthnLoginInputData,
) : AbstractAuthenticationToken(listOf()) {

    override fun getPrincipal(): Any {
        return this.principal
    }

    override fun getCredentials(): Any {
        return this.credentials
    }
}