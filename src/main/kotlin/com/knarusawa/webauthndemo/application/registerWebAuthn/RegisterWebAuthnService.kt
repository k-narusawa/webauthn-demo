package com.knarusawa.webauthndemo.application.registerWebAuthn

import com.webauthn4j.WebAuthnManager
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.springframework.security.exception.DataConversionException
import com.webauthn4j.springframework.security.exception.ValidationException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service


@Service
class RegisterWebAuthnService(
    private val webAuthnManager: WebAuthnManager
) {
    @Transactional
    fun exec() {
        val attestationObject: ByteArray? = null /* set attestationObject */
        val clientDataJSON: ByteArray? = null /* set clientDataJSON */
        val clientExtensionJSON: String? = null /* set clientExtensionJSON */
        val transports: Set<String>? = null /* set transports */

        val registrationRequest =
            RegistrationRequest(attestationObject, clientDataJSON, clientExtensionJSON, transports)

        val origin = Origin("http://localhost:8080")
        val rpId = "webauthn-demo"
        val challenge = DefaultChallenge()
        val tokenBindingId: ByteArray? = null /* set tokenBindingId */
        val serverProperty = ServerProperty(origin, rpId, challenge, tokenBindingId)

        val userVerificationRequired = false
        val userPresenceRequired = true

        // FIXME: 非推奨のコンストラクタなので注意
        val registrationParameters =
            RegistrationParameters(serverProperty, userVerificationRequired, userPresenceRequired)


        val registrationData = try {
            webAuthnManager.parse(registrationRequest)
        } catch (e: DataConversionException) {
            throw e
        }
        try {
            webAuthnManager.validate(registrationData, registrationParameters)
        } catch (e: ValidationException) {
            throw e
        }

        val authenticator =
            AuthenticatorImpl( // You may create your own Authenticator implementation to save friendly authenticator name
                registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!,
                registrationData.attestationObject!!.attestationStatement,
                registrationData.attestationObject!!.authenticatorData.signCount
            )
    }
}