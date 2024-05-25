package com.knarusawa.webauthndemo.application.startWebAuthnRegistration

import com.knarusawa.webauthndemo.application.query.WebAuthnCredentialDtoQueryService
import com.knarusawa.webauthndemo.config.WebAuthnConfig
import com.knarusawa.webauthndemo.domain.challenge.ChallengeData
import com.knarusawa.webauthndemo.domain.challenge.ChallengeDataRepository
import com.webauthn4j.data.AttestationConveyancePreference
import com.webauthn4j.data.AuthenticatorSelectionCriteria
import com.webauthn4j.data.PublicKeyCredentialCreationOptions
import com.webauthn4j.data.PublicKeyCredentialDescriptor
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.PublicKeyCredentialUserEntity
import com.webauthn4j.data.ResidentKeyRequirement
import com.webauthn4j.data.UserVerificationRequirement
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.util.Base64UrlUtil
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit


@Service
class StartWebAuthnRegistrationService(
  private val webAuthnConfig: WebAuthnConfig,
  private val challengeDataRepository: ChallengeDataRepository,
  private val webAuthnCredentialDtoQueryService: WebAuthnCredentialDtoQueryService
) {
  @Transactional
  fun exec(inputData: StartWebAuthnRegistrationInputData): StartWebAuthnRegistrationOutputData {
    val challenge = DefaultChallenge()

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
      /* authenticatorAttachment = */ null,
      /* requireResidentKey =      */ true,
      /* residentKey =             */ ResidentKeyRequirement.REQUIRED,
      /* userVerification =        */ UserVerificationRequirement.REQUIRED
    )

    val registerdCreds = webAuthnCredentialDtoQueryService.findByUserId(inputData.userId)

    val excludeCredentials = registerdCreds.map {
      PublicKeyCredentialDescriptor(
        /* type =       */ PublicKeyCredentialType.PUBLIC_KEY,
        /* id =         */ Base64UrlUtil.decode(it.credentialId),
        /* transports = */ setOf() // TODO: これでいいっけ？
      )
    }

    val options = PublicKeyCredentialCreationOptions(
      /* rp =                     */ webAuthnConfig.publicKeyCredentialRpEntity(),
      /* user =                   */ user,
      /* challenge =              */ challenge,
      /* pubKeyCredParams =       */ pubKeyCredParams,
      /* timeout =                */ TimeUnit.SECONDS.toMillis(180000),
      /* excludeCredentials =     */ excludeCredentials,
      /* authenticatorSelection = */ authenticatorSelectionCriteria,
      /* attestation =            */ AttestationConveyancePreference.DIRECT,
      /* extensions =             */ null,
    )

    val challengeData = ChallengeData.of(challenge = challenge)

    challengeDataRepository.save(challengeData)

    return StartWebAuthnRegistrationOutputData(options = options)
  }
}