package com.knarusawa.webauthndemo.application.startWebAuthnRegistration

import com.webauthn4j.data.PublicKeyCredentialCreationOptions

class StartWebAuthnRegistrationOutputData(
        val options: PublicKeyCredentialCreationOptions
) {
    fun from(
            options: PublicKeyCredentialCreationOptions
    ): StartWebAuthnRegistrationOutputData {
        return StartWebAuthnRegistrationOutputData(
                options = options
        )
    }
}