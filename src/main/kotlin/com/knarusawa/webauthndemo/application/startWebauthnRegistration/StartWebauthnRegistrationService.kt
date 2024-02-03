package com.knarusawa.webauthndemo.application.startWebauthnRegistration

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
class StartWebauthnRegistrationService(
    private val flowRepository: FlowRepository
) {
    companion object {
        private const val PR_ID = "localhost"
    }

    @Transactional
    fun exec(inputData: StartWebauthnRegistrationInputData): StartWebauthnRegistrationOutputData {
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
            inputData.userId.toByteArray(),
            inputData.username,
            inputData.username,
        )

        val authenticatorSelectionCriteria = AuthenticatorSelectionCriteria(
            AuthenticatorAttachment.CROSS_PLATFORM,
            false,
            UserVerificationRequirement.PREFERRED
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

        return StartWebauthnRegistrationOutputData(flowId = flow.flowId, options = options)
    }
}