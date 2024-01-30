package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.WebauthnRegistrationStartGetResponse
import com.knarusawa.webauthndemo.application.startWebAuthnRegistration.StartWebAuthnRegistrationInputData
import com.knarusawa.webauthndemo.application.startWebAuthnRegistration.StartWebAuthnRegistrationService
import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/webauthn")
class WebauthnController(
        private val startWebAuthnRegistrationService: StartWebAuthnRegistrationService
) {
    @GetMapping("/registration/start")
    fun webauthnRegistrationStartGet(): WebauthnRegistrationStartGetResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as? LoginUserDetails
                ?: throw IllegalStateException("Principalが不正")

        val inputData = StartWebAuthnRegistrationInputData(
                userId = user.userId,
                username = user.username,
        )
        val outputData = startWebAuthnRegistrationService.exec(inputData)

        return WebauthnRegistrationStartGetResponse.from(options = outputData.options)
    }
}