package com.knarusawa.webauthndemo.adapter.controller.dto

data class ApiV1UsersCredentialsGet(
  val credentials: List<Credential>
) {
  data class Credential(
    val userId: String,
    val credentialId: String,
    val serializedAttestedCredentialData: String,
    val serializedEnvelope: String,
    val serializedTransports: String,
    val serializedAuthenticatorExtensions: String,
    val serializedClientExtensions: String,
    val counter: Long
  )
}
