package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.ApiV1LoginWebauthnRequestGetResponse
import com.knarusawa.webauthndemo.application.startWebauthnLogin.StartWebauthnLoginInputData
import com.knarusawa.webauthndemo.application.startWebauthnLogin.StartWebauthnLoginService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/webauthn/login")
class WebAuthnLoginController(
    private val startWebauthnLoginService: StartWebauthnLoginService
) {
    @GetMapping("/request")
    fun apiV1LoginWebauthnRequestGet(
        @RequestParam("username") username: String
    ): ApiV1LoginWebauthnRequestGetResponse {
        val inputData = StartWebauthnLoginInputData(
            username = username
        )

        val outputData = startWebauthnLoginService.exec(inputData)

        return ApiV1LoginWebauthnRequestGetResponse.from(
            flowId = outputData.flowId.value(),
            options = outputData.options
        )
    }
}