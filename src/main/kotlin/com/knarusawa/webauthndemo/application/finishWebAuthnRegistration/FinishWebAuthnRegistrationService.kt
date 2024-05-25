package com.knarusawa.webauthndemo.application.finishWebAuthnRegistration

import com.knarusawa.webauthndemo.config.WebAuthnConfig
import com.knarusawa.webauthndemo.domain.challenge.ChallengeDataRepository
import com.knarusawa.webauthndemo.domain.credentials.Credential
import com.knarusawa.webauthndemo.domain.credentials.CredentialRepository
import com.knarusawa.webauthndemo.util.logger
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.challenge.DefaultChallenge
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

    val challenge = challengeData?.let { DefaultChallenge(Base64UrlUtil.decode(it.challenge)) }
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

    val serverProperty = webAuthnConfig.serverProperty(challenge)
    val registrationRequest = RegistrationRequest(attestationObject, clientDataJSON)
    val registrationParameters = RegistrationParameters(serverProperty, pubKeys, true)

    val registrationData =
      webAuthnManager.parse(registrationRequest)

    if (
      registrationData.attestationObject == null ||
      registrationData.attestationObject!!.authenticatorData.attestedCredentialData == null
    ) {
      throw IllegalStateException("不正なデータ")
    }

    try {
      webAuthnManager.validate(registrationRequest, registrationParameters)
    } catch (e: WebAuthnException) {
      log.error("WebAuthnException", e)
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

    val credential = Credential.of(
      credentialId = credentialId,
      userId = inputData.userId,
      authenticator = authenticator,
    )

    log.info(credential.toString())

    credentialRepository.save(credential)
    challengeDataRepository.deleteByChallenge(Base64UrlUtil.encodeToString(challenge.value))
  }
}