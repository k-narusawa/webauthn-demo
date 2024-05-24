package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.ApiV1LoginWebauthnRequestGetResponse
import com.knarusawa.webauthndemo.application.startWebAuthnLogin.StartWebAuthnLoginService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/webauthn/login")
class WebAuthnLoginController(
        private val startWebauthnLoginService: StartWebAuthnLoginService
) {
    @GetMapping("/request")
    fun apiV1LoginWebauthnRequestGet(): ApiV1LoginWebauthnRequestGetResponse {
        val outputData = startWebauthnLoginService.exec()

        return ApiV1LoginWebauthnRequestGetResponse.from(
                options = outputData.options
        )
    }
}