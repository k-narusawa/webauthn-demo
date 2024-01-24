package com.knarusawa.webauthndemo.application.startRegistrationWebAuthn

import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions

data class StartRegistrationWebAuthnOutputData(
        val flowId: String,
        val credentialCreationOptions: PublicKeyCredentialCreationOptions
)