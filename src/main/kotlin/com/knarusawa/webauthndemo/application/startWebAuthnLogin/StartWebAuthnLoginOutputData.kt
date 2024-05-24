package com.knarusawa.webauthndemo.application.startWebAuthnLogin

import com.webauthn4j.data.PublicKeyCredentialRequestOptions

data class StartWebAuthnLoginOutputData(
        val challenge: String,
        val options: PublicKeyCredentialRequestOptions
)