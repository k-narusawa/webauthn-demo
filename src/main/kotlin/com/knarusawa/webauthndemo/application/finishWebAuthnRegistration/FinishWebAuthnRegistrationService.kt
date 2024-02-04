package com.knarusawa.webauthndemo.application.finishWebAuthnRegistration

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
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.util.Base64UrlUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class FinishWebAuthnRegistrationService(
    private val flowRepository: FlowRepository,
    private val credentialsRepository: CredentialsRepository,
    private val userCredentialsRepository: UserCredentialsRepository
) {
    companion object {
        private const val PR_ID = "localhost"
        private val log = logger()
    }

    @Transactional
    fun exec(inputData: FinishWebAuthnRegistrationInputData) {
        val origin = Origin.create("http://localhost:3000")
        val flow =
            flowRepository.findByFlowId(FlowId.from(inputData.flowId))

        val challenge = flow?.let { Base64UrlUtil.decode(it.challenge) }
        val attestationObject = Base64UrlUtil.decode(inputData.attestationObject)
        val clientDataJSON = Base64UrlUtil.decode(inputData.clientDataJSON)

        val pubKeys = listOf(
            PublicKeyCredentialParameters(
                PublicKeyCredentialType.PUBLIC_KEY,
                COSEAlgorithmIdentifier.ES256
            ),
            PublicKeyCredentialParameters(
                PublicKeyCredentialType.PUBLIC_KEY,
                COSEAlgorithmIdentifier.RS256
            ),
        )

        val serverProperty = ServerProperty(origin, PR_ID, DefaultChallenge(challenge), null)
        val registrationRequest = RegistrationRequest(attestationObject, clientDataJSON)
        val registrationParameters = RegistrationParameters(serverProperty, pubKeys, true)

        val registrationData =
            WebAuthnManager.createNonStrictWebAuthnManager().parse(registrationRequest);

        WebAuthnManager.createNonStrictWebAuthnManager()
            .validate(registrationRequest, registrationParameters)


        if (
            registrationData.attestationObject == null ||
            registrationData.attestationObject!!.authenticatorData.attestedCredentialData == null
        ) {
            throw IllegalStateException("不正なデータ")
        }

        val authenticator = AuthenticatorImpl(
            /* attestedCredentialData = */
            registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!,
            /* attestationStatement =   */
            registrationData.attestationObject!!.attestationStatement,
            /* counter =                */
            registrationData.attestationObject!!.authenticatorData.signCount,
        )

        val credentialId =
            registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!.credentialId

        val credentials = Credentials.of(authenticator = authenticator, credentialId = credentialId)

        val userCredentials = UserCredentials.of(
            credentialId = Base64UrlUtil.encodeToString(credentialId),
            userId = UserId.from(inputData.userId)
        )

        credentialsRepository.save(credentials)
        userCredentialsRepository.save(userCredentials)
        flowRepository.deleteByUserId(UserId.from(inputData.userId))
    }
}