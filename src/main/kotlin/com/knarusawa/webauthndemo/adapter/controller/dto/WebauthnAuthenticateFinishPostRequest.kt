package com.knarusawa.webauthndemo.adapter.controller.dto

data class WebauthnAuthenticateFinishPostRequest(
    val challenge: String,
    val id: String,
    val rawId: String,
    val type: String,
    val response: Response
) {
    data class Response(
        val authenticatorData: String,
        val clientDataJSON: String,
        val signature: String,
        val userHandle: String?
    )
}