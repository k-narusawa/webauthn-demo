package com.knarusawa.webauthndemo.application.startWebAuthnAuthentication

import com.knarusawa.webauthndemo.domain.challenge.ChallengeData
import com.knarusawa.webauthndemo.domain.challenge.ChallengeDataRepository
import com.webauthn4j.data.PublicKeyCredentialDescriptor
import com.webauthn4j.data.PublicKeyCredentialRequestOptions
import com.webauthn4j.data.PublicKeyCredentialRpEntity
import com.webauthn4j.data.UserVerificationRequirement
import com.webauthn4j.data.client.challenge.DefaultChallenge
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class StartWebAuthnAuthenticationService(
    private val challengeDataRepository: ChallengeDataRepository,
) {
    companion object {
        private val RP = PublicKeyCredentialRpEntity("localhost", "localhost")
    }

    @Transactional
    fun exec(): StartWebAuthnAuthenticationOutputData {
        val challenge = DefaultChallenge()

        val challengeData = ChallengeData.of(challenge = challenge)

        val options = PublicKeyCredentialRequestOptions(
            challenge,
            60000,
            RP.id,
            listOf<PublicKeyCredentialDescriptor>(),
            UserVerificationRequirement.REQUIRED,
            null
        )
        challengeDataRepository.save(challengeData)

        return StartWebAuthnAuthenticationOutputData(
            challenge = challengeData.challenge,
            options = options
        )
    }
}