package com.knarusawa.webauthndemo.application.finishWebauthnLogin

data class FinishWebauthnLoginInputData(
    val flowId: String,
    val credentialId: String,
    val clientDataJSON: String,
    val authenticatorData: String,
    val signature: String,
    val userHandle: String
)