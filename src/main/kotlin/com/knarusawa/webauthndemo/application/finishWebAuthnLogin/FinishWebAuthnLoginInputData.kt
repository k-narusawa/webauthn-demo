package com.knarusawa.webauthndemo.application.finishWebAuthnLogin

data class FinishWebAuthnLoginInputData(
    val challenge: String,
    val credentialId: String,
    val clientDataJSON: String,
    val authenticatorData: String,
    val signature: String,
    val userHandle: String?
)