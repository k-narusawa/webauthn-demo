package com.knarusawa.webauthndemo.application.dto

data class WebAuthnCredentialDto(
  val credentialId: String,
  val userId: String,
  val aaguid: String,
  val label: String,
  val counter: Long,
)
