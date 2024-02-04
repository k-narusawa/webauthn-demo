package com.knarusawa.webauthndemo.adapter.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.webauthn4j.data.PublicKeyCredentialDescriptor
import com.webauthn4j.data.PublicKeyCredentialRequestOptions
import com.webauthn4j.data.extension.client.AuthenticationExtensionClientInput
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs
import com.webauthn4j.util.Base64UrlUtil

data class WebauthnAuthenticateStartGetResponse(
        @JsonProperty("flowId")
        val flowId: String,

        @JsonProperty("challenge")
        val challenge: String,

        @JsonProperty("timeout")
        val timeout: Long,

        @JsonProperty("rpId")
        val rpId: String,

        @JsonProperty("allowCredentials")
        val allowCredentials: List<PublicKeyCredentialDescriptor>,

        @JsonProperty("userVerification")
        val userVerification: String,

        @JsonProperty("extensions")
        val extensions: AuthenticationExtensionsClientInputs<AuthenticationExtensionClientInput>?,
) {
    companion object {
        fun from(flowId: String, options: PublicKeyCredentialRequestOptions) = WebauthnAuthenticateStartGetResponse(
                flowId = flowId,
                challenge = Base64UrlUtil.encodeToString(options.challenge.value),
                timeout = options.timeout ?: 0,
                rpId = options.rpId!!,
                allowCredentials = options.allowCredentials!!,
                userVerification = options.userVerification.toString(),
                extensions = options.extensions
        )
    }
}
