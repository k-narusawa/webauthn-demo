package com.knarusawa.webauthndemo.application.finishWebauthnRegistration

import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import org.springframework.stereotype.Service


@Service
class FinishWebauthnRegistrationService(
//        private val webAuthnManager: WebAuthnManager
) {
    companion object {
        private const val PR_ID = "localhost"
    }

    fun exec(inputData: FinishWebauthnRegistrationInputData) {
        val origin = Origin.create("http://localhost")

        val serverProperty = ServerProperty(origin, PR_ID, DefaultChallenge(), null)
//        val registrationRequest = RegistrationRequest(attestationObject, clientDataJSON)
//        val registrationParameters = RegistrationParameters(serverProperty, true)

    }
}