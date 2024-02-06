package com.knarusawa.webauthndemo.application.startWebAuthnRegistration

import com.knarusawa.webauthndemo.domain.flow.Flow
import com.knarusawa.webauthndemo.domain.flow.FlowRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.webauthn4j.data.*
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.util.Base64UrlUtil
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit


@Service
class StartWebAuthnRegistrationService(
        private val flowRepository: FlowRepository
) {
    companion object {
        private const val PR_ID = "localhost"
    }

    @Transactional
    fun exec(inputData: StartWebAuthnRegistrationInputData): StartWebAuthnRegistrationOutputData {
        val rpId = PR_ID
        val challenge = DefaultChallenge()
        val authenticatorAttachment = when (inputData.authenticatorAttachment) {
            StartWebAuthnRegistrationInputData.AuthenticatorAttachment.CROSS_PLATFORM -> AuthenticatorAttachment.CROSS_PLATFORM
            StartWebAuthnRegistrationInputData.AuthenticatorAttachment.PLATFORM -> AuthenticatorAttachment.PLATFORM
        }

        val pubKeyCredParams = listOf(
                PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.ES256
                ),
                PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        COSEAlgorithmIdentifier.RS256
                ),
        )

        val user = PublicKeyCredentialUserEntity(
                /* id = */          Base64UrlUtil.decode(inputData.userId),
                /* name = */        inputData.username,
                /* displayName = */ inputData.username,
        )

        val authenticatorSelectionCriteria = AuthenticatorSelectionCriteria(
                /* authenticatorAttachment = */ authenticatorAttachment,
                /* requireResidentKey =      */ false,
                /* userVerification =        */ UserVerificationRequirement.REQUIRED
        )

        val options = PublicKeyCredentialCreationOptions(
                /* rp =                     */ PublicKeyCredentialRpEntity(rpId, "webauthn-demo"),
                /* user =                   */ user,
                /* challenge =              */ challenge,
                /* pubKeyCredParams =       */ pubKeyCredParams,
                /* timeout =                */ TimeUnit.SECONDS.toMillis(6000),
                /* excludeCredentials =     */ null,
                /* authenticatorSelection = */ authenticatorSelectionCriteria,
                /* attestation =            */ AttestationConveyancePreference.NONE,
                /* extensions =             */ null,
        )

        val flow =
                Flow.of(userId = UserId.from(inputData.userId), challenge = challenge)

        flowRepository.save(flow)

        return StartWebAuthnRegistrationOutputData(flowId = flow.flowId, options = options)
    }
}