package com.knarusawa.webauthndemo.application.dto

data class CredentialsDto(
    val credentialId: String,
    val userId: String,
    val serializedAttestedCredentialData: String,
    val serializedEnvelope: String,
    val serializedTransports: String,
    val serializedAuthenticatorExtensions: String,
    val serializedClientExtensions: String,
    val counter: Long,
)
