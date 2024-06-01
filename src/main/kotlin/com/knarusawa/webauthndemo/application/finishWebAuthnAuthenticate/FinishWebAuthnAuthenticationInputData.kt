package com.knarusawa.webauthndemo.application.finishWebAuthnAuthenticate

data class FinishWebAuthnAuthenticationInputData(
  val challenge: String,
  val credentialId: String,
  val clientDataJSON: String,
  val authenticatorData: String,
  val signature: String,
  val userHandle: String?
)