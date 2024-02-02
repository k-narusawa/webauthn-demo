package com.knarusawa.webauthndemo.application.finishWebauthnRegistration

import com.knarusawa.webauthndemo.domain.credentials.Credentials
import com.knarusawa.webauthndemo.domain.credentials.CredentialsRepository
import com.knarusawa.webauthndemo.domain.flow.FlowId
import com.knarusawa.webauthndemo.domain.flow.FlowRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.knarusawa.webauthndemo.domain.userCredentials.UserCredentials
import com.knarusawa.webauthndemo.domain.userCredentials.UserCredentialsRepository
import com.knarusawa.webauthndemo.util.logger
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.util.Base64UrlUtil
import com.webauthn4j.validator.exception.ValidationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class FinishWebauthnRegistrationService(
        private val flowRepository: FlowRepository,
        private val credentialsRepository: CredentialsRepository,
        private val userCredentialsRepository: UserCredentialsRepository
) {
    companion object {
        private const val PR_ID = "localhost"
        private val log = logger()
    }

    @Transactional
    fun exec(inputData: FinishWebauthnRegistrationInputData) {
        val origin = Origin.create("http://localhost:3000")
        val flow =
                flowRepository.findByFlowId(FlowId.from(inputData.flowId))

        val challenge = flow?.let { Base64UrlUtil.decode(it.challenge) }

        val attestationObject = Base64UrlUtil.decode(inputData.attestationObject)
        val clientDataJSON = Base64UrlUtil.decode(inputData.clientDataJSON)

        val serverProperty = ServerProperty(origin, PR_ID, DefaultChallenge(challenge), null)
        val registrationRequest = RegistrationRequest(attestationObject, clientDataJSON)
        val registrationParameters = RegistrationParameters(serverProperty, true)

        val registrationData =
                WebAuthnManager.createNonStrictWebAuthnManager().parse(registrationRequest);

        try {
            WebAuthnManager.createNonStrictWebAuthnManager()
                    .validate(registrationRequest, registrationParameters)
        } catch (ex: ValidationException) {
            throw ex
        }

        if (
                registrationData.attestationObject == null ||
                registrationData.attestationObject!!.authenticatorData.attestedCredentialData == null
        ) {
            throw RuntimeException("不正なデータ")
        }

        val authenticator = AuthenticatorImpl(
                registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!,
                registrationData.attestationObject!!.attestationStatement,
                registrationData.attestationObject!!.authenticatorData.signCount,
        )

        val credentialId =
                registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!.credentialId

        val objectConverter = ObjectConverter()
        val attestedCredentialDataConverter = AttestedCredentialDataConverter(objectConverter)

        val credentials = Credentials.of(
                credentialId = Base64UrlUtil.encodeToString(credentialId),
                serializedAttestedCredentialData = attestedCredentialDataConverter.convert(authenticator.attestedCredentialData),
                serializedEnvelope = objectConverter.cborConverter.writeValueAsBytes(authenticator.attestationStatement),
                serializedTransports = objectConverter.cborConverter.writeValueAsBytes(
                        objectConverter.cborConverter.writeValueAsBytes(
                                authenticator.attestationStatement
                        )
                ),
                serializedAuthenticatorExtensions = objectConverter.cborConverter.writeValueAsBytes(
                        authenticator.authenticatorExtensions
                ),
                serializedClientExtensions = objectConverter.cborConverter.writeValueAsBytes(
                        authenticator.authenticatorExtensions
                ),
                counter = authenticator.counter
        )
        val userCredentials = UserCredentials.of(
                credentialId = Base64UrlUtil.encodeToString(credentialId),
                userId = UserId.from(inputData.userId)
        )

        credentialsRepository.save(credentials)
        userCredentialsRepository.save(userCredentials)

        flowRepository.deleteByUserId(UserId.from(inputData.userId))

        log.info("Webauth登録完了 userId: ${inputData.userId}")
    }
}