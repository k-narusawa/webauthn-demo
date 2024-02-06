package com.knarusawa.webauthndemo.application.finishWebAuthnLogin

import com.knarusawa.webauthndemo.domain.credentials.CredentialsRepository
import com.knarusawa.webauthndemo.domain.flow.FlowId
import com.knarusawa.webauthndemo.domain.flow.FlowRepository
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.exception.DataConversionException
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.AuthenticationParameters
import com.webauthn4j.data.AuthenticationRequest
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.util.Base64UrlUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class FinishWebAuthnLoginService(
        private val flowRepository: FlowRepository,
        private val credentialsRepository: CredentialsRepository,
) {
    companion object {
        private const val PR_ID = "localhost"
    }

    @Transactional
    fun exec(inputData: FinishWebAuthnLoginInputData): FinishWebAuthnLoginOutputData {
        val origin = Origin.create("http://localhost:3000")
        val flow = flowRepository.findByFlowId(FlowId.from(inputData.flowId))
                ?: throw IllegalArgumentException("flow is not found")
        val challenge = flow.let { Base64UrlUtil.decode(it.challenge) }

        val serverProperty = ServerProperty(origin, PR_ID, DefaultChallenge(challenge), null)

        val credentials = credentialsRepository.findByCredentialId(inputData.credentialId)
                ?: throw IllegalArgumentException("credential is not found")

        val attestedCredentialDataConverter = AttestedCredentialDataConverter(ObjectConverter())

        val authenticator = AuthenticatorImpl(
                /* attestedCredentialData = */
                attestedCredentialDataConverter.convert(
                        Base64UrlUtil.decode(credentials.serializedAttestedCredentialData)
                ),
                /* attestationStatement = */   null,
                /* counter = */                credentials.counter
        )

        val authenticationParameter = AuthenticationParameters(
                serverProperty,
                authenticator,
                listOf(Base64UrlUtil.decode(credentials.credentialId)),
                false
        )

        val authenticationRequest = AuthenticationRequest(
                /* credentialId = */      Base64UrlUtil.decode(inputData.credentialId),
                /* userHandle = */        inputData.userHandle?.let { Base64UrlUtil.decode(it) },
                /* authenticatorData = */ Base64UrlUtil.decode(inputData.authenticatorData),
                /* clientDataJSON = */    Base64UrlUtil.decode(inputData.clientDataJSON),
                /* signature = */         Base64UrlUtil.decode(inputData.signature),
        )

        val authenticationData = try {
            WebAuthnManager.createNonStrictWebAuthnManager().parse(authenticationRequest);
        } catch (ex: DataConversionException) {
            ex.printStackTrace()
            throw ex
        }

        try {
            WebAuthnManager.createNonStrictWebAuthnManager()
                    .validate(authenticationRequest, authenticationParameter)
        } catch (ex: Exception) {
            throw ex
        }
        
        credentials.updateCounter(authenticationData.authenticatorData!!.signCount)
        credentialsRepository.save(credentials)

        return FinishWebAuthnLoginOutputData(userId = flow.userId)
    }
}