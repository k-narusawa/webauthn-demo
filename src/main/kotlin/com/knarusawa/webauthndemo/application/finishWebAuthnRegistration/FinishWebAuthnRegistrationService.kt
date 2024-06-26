package com.knarusawa.webauthndemo.application.finishWebAuthnRegistration

import com.knarusawa.webauthndemo.config.WebAuthnConfig
import com.knarusawa.webauthndemo.domain.challenge.ChallengeDataRepository
import com.knarusawa.webauthndemo.domain.credentials.Credential
import com.knarusawa.webauthndemo.domain.credentials.CredentialRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.knarusawa.webauthndemo.util.logger
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.util.Base64UrlUtil
import com.webauthn4j.util.exception.WebAuthnException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class FinishWebAuthnRegistrationService(
  private val webAuthnConfig: WebAuthnConfig,
  private val webAuthnManager: WebAuthnManager,
  private val challengeDataRepository: ChallengeDataRepository,
  private val credentialRepository: CredentialRepository,
) {
  companion object {
    private val log = logger()
  }

  @Transactional
  fun exec(inputData: FinishWebAuthnRegistrationInputData) {
    val challengeData = challengeDataRepository.findByChallenge(inputData.challenge)
      ?: throw IllegalArgumentException("challenge is not found")

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

    val serverProperty = webAuthnConfig.serverProperty(challengeData.challenge)
    val registrationRequest = RegistrationRequest(attestationObject, clientDataJSON)
    val registrationParameters = RegistrationParameters(serverProperty, pubKeys, true)

    val registrationData = webAuthnManager.parse(registrationRequest)

    try {
      webAuthnManager.validate(registrationRequest, registrationParameters)
    } catch (e: WebAuthnException) {
      log.error("WebAuthnException", e)
      throw IllegalStateException("不正なデータ")
    }

    val credential = Credential.of(
      userId = UserId.from(inputData.userId),
      registrationData = registrationData
    )

    log.debug("Credential is created")
    log.debug(credential.toString())

    credentialRepository.save(credential)
    challengeDataRepository.deleteByChallenge(challengeData.getRawChallenge())
  }
}