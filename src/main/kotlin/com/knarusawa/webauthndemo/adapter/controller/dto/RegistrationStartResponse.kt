package com.knarusawa.webauthndemo.adapter.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions

data class RegistrationStartResponse(
        @JsonProperty("flow_id")
        val flowId: String,
        @JsonProperty("credential_creation_options")
        val credentialCreationOptions: PublicKeyCredentialCreationOptions
)
