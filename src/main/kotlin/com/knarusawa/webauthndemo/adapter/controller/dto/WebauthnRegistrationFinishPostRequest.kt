package com.knarusawa.webauthndemo.adapter.controller.dto

data class WebauthnRegistrationFinishPostRequest(
        val id: String,
        val rawId: String,
        val type: String,
        val response: Response
) {
    data class Response(
            val attestationObject: String,
            val clientDataJSON: String
    )
}