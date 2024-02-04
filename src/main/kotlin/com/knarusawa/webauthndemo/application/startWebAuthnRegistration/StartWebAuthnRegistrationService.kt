package com.knarusawa.webauthndemo.application.startWebAuthnRegistration

import com.knarusawa.webauthndemo.domain.flow.Flow
import com.knarusawa.webauthndemo.domain.flow.FlowRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.webauthn4j.data.*
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.challenge.DefaultChallenge
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

        val user = PublicKeyCredentialUserEntity(
            /* id = */          inputData.userId.toByteArray(),
            /* name = */        inputData.username,
            /* displayName = */ inputData.username,
        )

        val authenticatorSelectionCriteria = AuthenticatorSelectionCriteria(
            AuthenticatorAttachment.CROSS_PLATFORM,
            false,
            UserVerificationRequirement.REQUIRED
        )

        val options = PublicKeyCredentialCreationOptions(
            PublicKeyCredentialRpEntity(rpId, "webauthn-demo"),
            user,
            challenge,
            pubKeys,
            TimeUnit.SECONDS.toMillis(6000),
            null,
            authenticatorSelectionCriteria,
            AttestationConveyancePreference.NONE,
            null,
        )

        val flow =
            Flow.of(userId = UserId.from(inputData.userId), challenge = challenge)

        flowRepository.save(flow)

        return StartWebAuthnRegistrationOutputData(flowId = flow.flowId, options = options)
    }
}