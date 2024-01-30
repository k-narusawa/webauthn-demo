package com.knarusawa.webauthndemo.adapter.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.webauthn4j.data.*
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs
import com.webauthn4j.data.extension.client.RegistrationExtensionClientInput
import com.webauthn4j.util.Base64UrlUtil


data class WebauthnRegistrationStartGetResponse(
//        @JsonProperty("flowId")
//        val flowId: String,

        @JsonProperty("rp")
        val rp: PublicKeyCredentialRpEntity,

        @JsonProperty("user")
        val user: CustomPublicKeyCredentialUserEntity,

        @JsonProperty("challenge")
        val challenge: String,

        @JsonProperty("pubKeyCredParams")
        val pubKeyCredParams: List<PublicKeyCredentialParameters?>,

        @JsonProperty("timeout")
        val timeout: Long,

        @JsonProperty("excludeCredentials")
        val excludeCredentials: List<PublicKeyCredentialDescriptor?>,

        @JsonProperty("authenticatorSelection")
        val authenticatorSelection: AuthenticatorSelectionCriteria,

        @JsonProperty("attestation")
        val attestation: String,

        @JsonProperty("extensions")
        val extensions: AuthenticationExtensionsClientInputs<RegistrationExtensionClientInput?>?,
) {
    companion object {
        fun from(options: PublicKeyCredentialCreationOptions): WebauthnRegistrationStartGetResponse {

            return WebauthnRegistrationStartGetResponse(
                    rp = options.rp,
                    user = CustomPublicKeyCredentialUserEntity(
                            id = Base64UrlUtil.encodeToString(options.user.id),
                            name = options.user.name,
                            displayName = options.user.displayName,
                    ),
                    challenge = Base64UrlUtil.encodeToString(options.challenge.value),
                    pubKeyCredParams = options.pubKeyCredParams,
                    timeout = options.timeout ?: 0,
                    excludeCredentials = options.excludeCredentials?.toList() ?: listOf(),
                    authenticatorSelection = options.authenticatorSelection!!,
                    attestation = options.attestation.toString(),
                    extensions = options.extensions
            )
        }
    }

    data class CustomPublicKeyCredentialUserEntity(
            @JsonProperty("id")
            val id: String,

            @JsonProperty("name")
            val name: String,

            @JsonProperty("displayName")
            val displayName: String,
    )
}