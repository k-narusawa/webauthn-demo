package com.knarusawa.webauthndemo.application.startWebauthnRegistration

import com.knarusawa.webauthndemo.domain.flow.FlowId
import com.webauthn4j.data.PublicKeyCredentialCreationOptions

class StartWebauthnRegistrationOutputData(
    val flowId: FlowId,
    val options: PublicKeyCredentialCreationOptions
) {
    fun from(
        flowId: FlowId,
        options: PublicKeyCredentialCreationOptions
    ): StartWebauthnRegistrationOutputData {
        return StartWebauthnRegistrationOutputData(
            flowId = flowId,
            options = options
        )
    }
}