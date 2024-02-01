package com.knarusawa.webauthndemo.application.finishWebauthnRegistration

data class FinishWebauthnRegistrationInputData(
        val userId: String,
        val username: String,
        val id: String,
        val rawId: String,
        val type: String,
        val attestationObject: String,
        val clientDataJSON: String,
)
