package com.knarusawa.webauthndemo.application.startWebauthnLogin

import com.knarusawa.webauthndemo.domain.flow.FlowId
import com.webauthn4j.data.PublicKeyCredentialRequestOptions

data class StartWebauthnLoginOutputData(
        val flowId: FlowId,
        val options: PublicKeyCredentialRequestOptions
)