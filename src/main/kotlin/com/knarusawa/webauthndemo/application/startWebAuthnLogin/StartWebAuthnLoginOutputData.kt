package com.knarusawa.webauthndemo.application.startWebAuthnLogin

import com.knarusawa.webauthndemo.domain.flow.FlowId
import com.webauthn4j.data.PublicKeyCredentialRequestOptions

data class StartWebAuthnLoginOutputData(
    val flowId: FlowId,
    val options: PublicKeyCredentialRequestOptions
)