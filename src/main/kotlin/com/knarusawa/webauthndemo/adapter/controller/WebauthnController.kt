package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.WebauthnAuthenticateFinishPostRequest
import com.knarusawa.webauthndemo.adapter.controller.dto.WebauthnAuthenticateStartGetResponse
import com.knarusawa.webauthndemo.adapter.controller.dto.WebauthnRegistrationFinishPostRequest
import com.knarusawa.webauthndemo.adapter.controller.dto.WebauthnRegistrationStartGetResponse
import com.knarusawa.webauthndemo.application.finishWebauthnLogin.FinishWebauthnLoginInputData
import com.knarusawa.webauthndemo.application.finishWebauthnLogin.FinishWebauthnLoginService
import com.knarusawa.webauthndemo.application.finishWebauthnRegistration.FinishWebauthnRegistrationInputData
import com.knarusawa.webauthndemo.application.finishWebauthnRegistration.FinishWebauthnRegistrationService
import com.knarusawa.webauthndemo.application.startWebAuthnRegistration.StartWebAuthnRegistrationInputData
import com.knarusawa.webauthndemo.application.startWebAuthnRegistration.StartWebAuthnRegistrationService
import com.knarusawa.webauthndemo.application.startWebauthnLogin.StartWebauthnLoginInputData
import com.knarusawa.webauthndemo.application.startWebauthnLogin.StartWebauthnLoginService
import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/webauthn")
class WebauthnController(
        private val startWebAuthnRegistrationService: StartWebAuthnRegistrationService,
        private val finishWebAuthnRegistrationService: FinishWebauthnRegistrationService,
        private val startWebauthnLoginService: StartWebauthnLoginService,
        private val finishWebauthnLoginService: FinishWebauthnLoginService
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

        return WebauthnRegistrationStartGetResponse.from(flowId = outputData.flowId, options = outputData.options)
    }

    @PostMapping("/registration/finish")
    fun webauthnRegistrationFinishPost(
            @RequestBody body: WebauthnRegistrationFinishPostRequest
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as? LoginUserDetails
                ?: throw IllegalStateException("Principalが不正")

        val inputData = FinishWebauthnRegistrationInputData(
                flowId = body.flowId,
                userId = user.userId,
                username = user.username,
                id = body.id,
                rawId = body.rawId,
                type = body.type,
                attestationObject = body.response.attestationObject,
                clientDataJSON = body.response.clientDataJSON,
        )
        finishWebAuthnRegistrationService.exec(inputData)
    }

    @GetMapping("/authenticate/start")
    fun webauthnAuthenticateStartGet(): WebauthnAuthenticateStartGetResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as? LoginUserDetails
                ?: throw IllegalStateException("Principalが不正")

        val inputData = StartWebauthnLoginInputData(
                username = user.username
        )

        val outputData = startWebauthnLoginService.exec(inputData)

        return WebauthnAuthenticateStartGetResponse.from(
                flowId = outputData.flowId.value(),
                options = outputData.options
        )
    }

    @PostMapping("/authenticate/finish")
    fun webauthnAuthenticateFinishPost(
            @RequestBody body: WebauthnAuthenticateFinishPostRequest
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as? LoginUserDetails
                ?: throw IllegalStateException("Principalが不正")

        val inputData = FinishWebauthnLoginInputData(
                flowId = body.flowId,
                userId = user.userId,
                credentialId = body.rawId,
                clientDataJSON = body.response.clientDataJSON,
                authenticatorData = body.response.authenticatorData,
                signature = body.response.clientDataJSON,
                userHandle = body.response.userHandle
        )
        finishWebauthnLoginService.exec(inputData)
    }
}