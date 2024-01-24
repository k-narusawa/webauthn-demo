package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.RegistrationStartResponse
import com.knarusawa.webauthndemo.application.startRegistrationWebAuthn.StartRegistrationWebAuthnInputData
import com.knarusawa.webauthndemo.application.startRegistrationWebAuthn.StartRegistrationWebAuthnService
import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/users/webauthn")
class UsersWebauthnController(
        private val startRegistrationWebAuthnService: StartRegistrationWebAuthnService
) {
    @PostMapping("/start")
    fun apiV1UsersWebauthnStartPost(): RegistrationStartResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as? LoginUserDetails
                ?: throw IllegalStateException("Principalが不正")

        val inputData = StartRegistrationWebAuthnInputData(
                userId = user.userId,
                username = user.username
        )

        val outputData = startRegistrationWebAuthnService.exec(inputData = inputData)

        return RegistrationStartResponse(
                flowId = outputData.flowId,
                credentialCreationOptions = outputData.credentialCreationOptions
        )
    }
}