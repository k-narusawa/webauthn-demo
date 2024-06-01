package com.knarusawa.webauthndemo.application.startWebAuthnAuthentication

import com.webauthn4j.data.PublicKeyCredentialRequestOptions

data class StartWebAuthnAuthenticationOutputData(
  val challenge: String,
  val options: PublicKeyCredentialRequestOptions
)