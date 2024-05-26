package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.ApiV1LoginWebauthnRequestGetResponse
import com.knarusawa.webauthndemo.application.startWebAuthnAuthentication.StartWebAuthnAuthenticationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/webauthn/authentication")
class WebauthnLoginController(
        private val startWebauthnAuthenticationService: StartWebAuthnAuthenticationService
) {
    @GetMapping("/options")
    fun apiV1LoginWebauthnRequestGet(): ApiV1LoginWebauthnRequestGetResponse {
        val outputData = startWebauthnAuthenticationService.exec()

        return ApiV1LoginWebauthnRequestGetResponse.from(
                options = outputData.options
        )
    }
}