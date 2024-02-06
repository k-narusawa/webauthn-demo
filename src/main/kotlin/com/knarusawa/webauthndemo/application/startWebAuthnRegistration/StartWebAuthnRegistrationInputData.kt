package com.knarusawa.webauthndemo.application.startWebAuthnRegistration

data class StartWebAuthnRegistrationInputData(
        val userId: String,
        val username: String,
        val authenticatorAttachment: AuthenticatorAttachment
) {
    enum class AuthenticatorAttachment {
        CROSS_PLATFORM,
        PLATFORM
    }
}