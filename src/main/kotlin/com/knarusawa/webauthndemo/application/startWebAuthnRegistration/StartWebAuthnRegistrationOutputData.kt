package com.knarusawa.webauthndemo.application.startWebAuthnRegistration

import com.knarusawa.webauthndemo.domain.flow.FlowId
import com.webauthn4j.data.PublicKeyCredentialCreationOptions

class StartWebAuthnRegistrationOutputData(
    val flowId: FlowId,
    val options: PublicKeyCredentialCreationOptions
) {
    fun from(
        flowId: FlowId,
        options: PublicKeyCredentialCreationOptions
    ): StartWebAuthnRegistrationOutputData {
        return StartWebAuthnRegistrationOutputData(
            flowId = flowId,
            options = options
        )
    }
}