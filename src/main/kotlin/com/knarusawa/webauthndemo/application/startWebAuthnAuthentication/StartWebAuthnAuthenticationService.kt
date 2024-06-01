package com.knarusawa.webauthndemo.application.startWebAuthnAuthentication

import com.knarusawa.webauthndemo.config.WebAuthnConfig
import com.knarusawa.webauthndemo.domain.challenge.ChallengeData
import com.knarusawa.webauthndemo.domain.challenge.ChallengeDataRepository
import com.webauthn4j.data.PublicKeyCredentialDescriptor
import com.webauthn4j.data.PublicKeyCredentialRequestOptions
import com.webauthn4j.data.UserVerificationRequirement
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit


@Service
class StartWebAuthnAuthenticationService(
  private val webAuthnConfig: WebAuthnConfig,
  private val challengeDataRepository: ChallengeDataRepository,
) {
  @Transactional
  fun exec(): StartWebAuthnAuthenticationOutputData {
    val challengeData = ChallengeData.of()

    val options = PublicKeyCredentialRequestOptions(
      /* challenge =        */ challengeData.challenge,
      /* timeout =          */ TimeUnit.SECONDS.toMillis(webAuthnConfig.timeout),
      /* rpId =             */ webAuthnConfig.rpId,
      /* allowCredentials = */ listOf<PublicKeyCredentialDescriptor>(),
      /* userVerification = */ UserVerificationRequirement.REQUIRED,
      /* extensions =       */ null
    )
    challengeDataRepository.save(challengeData)

    return StartWebAuthnAuthenticationOutputData(
      challenge = challengeData.getRawChallenge(),
      options = options
    )
  }
}