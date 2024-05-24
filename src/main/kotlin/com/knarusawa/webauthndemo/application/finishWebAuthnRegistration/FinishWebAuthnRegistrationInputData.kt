package com.knarusawa.webauthndemo.application.finishWebAuthnRegistration

data class FinishWebAuthnRegistrationInputData(
    val userId: String,
    val username: String,
    val challenge: String,
    val id: String,
    val rawId: String,
    val type: String,
    val attestationObject: String,
    val clientDataJSON: String,
)
