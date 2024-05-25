package com.knarusawa.webauthndemo.application.finishWebAuthnAuthenticate

import com.knarusawa.webauthndemo.config.WebAuthnConfig
import com.knarusawa.webauthndemo.domain.challenge.ChallengeDataRepository
import com.knarusawa.webauthndemo.domain.credentials.CredentialRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.exception.DataConversionException
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.AuthenticationParameters
import com.webauthn4j.data.AuthenticationRequest
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.util.Base64UrlUtil
import com.webauthn4j.util.exception.WebAuthnException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class FinishWebAuthnAuthenticationService(
  private val webAuthnConfig: WebAuthnConfig,
  private val webAuthnManager: WebAuthnManager,
  private val challengeDataRepository: ChallengeDataRepository,
  private val credentialRepository: CredentialRepository,
) {
  companion object {
    private val attestedCredentialDataConverter =
      AttestedCredentialDataConverter(ObjectConverter())
  }

  @Transactional
  fun exec(inputData: FinishWebAuthnAuthenticationInputData): FinishWebAuthnAuthenticationOutputData {
    val userId = inputData.userHandle
      ?: throw RuntimeException("ユーザーの識別に失敗しました")

    val challengeData = challengeDataRepository.findByChallenge(inputData.challenge)
      ?: throw IllegalArgumentException("flow is not found")

    val challenge = DefaultChallenge(Base64UrlUtil.decode(challengeData.challenge))

    val serverProperty = webAuthnConfig.serverProperty(challenge)

    val credentials = credentialRepository.findByCredentialId(inputData.credentialId)
      ?: throw IllegalArgumentException("credential is not found")

    val attestedCredentialData = attestedCredentialDataConverter.convert(
      Base64UrlUtil.decode(credentials.serializedAttestedCredentialData)
    )

    val authenticator = AuthenticatorImpl(
      /* attestedCredentialData = */ attestedCredentialData,
      /* attestationStatement   = */ null,
      /* counter                = */ credentials.counter
    )

    val authenticationParameter = AuthenticationParameters(
      serverProperty,
      authenticator,
      listOf(Base64UrlUtil.decode(credentials.credentialId)),
      false
    )

    val authenticationRequest = AuthenticationRequest(
      /* credentialId = */      Base64UrlUtil.decode(inputData.credentialId),
      /* userHandle = */        inputData.userHandle.let { Base64UrlUtil.decode(it) },
      /* authenticatorData = */ Base64UrlUtil.decode(inputData.authenticatorData),
      /* clientDataJSON = */    Base64UrlUtil.decode(inputData.clientDataJSON),
      /* signature = */         Base64UrlUtil.decode(inputData.signature),
    )

    val authenticationData = try {
      webAuthnManager.parse(authenticationRequest);
    } catch (ex: DataConversionException) {
      ex.printStackTrace()
      throw ex
    }

    try {
      webAuthnManager.validate(authenticationRequest, authenticationParameter)
    } catch (ex: WebAuthnException) {
      ex.printStackTrace()
      throw ex
    }

    credentials.updateCounter(authenticationData.authenticatorData!!.signCount)
    credentialRepository.save(credentials)
    challengeDataRepository.deleteByChallenge(inputData.challenge)

    return FinishWebAuthnAuthenticationOutputData(userId = UserId.from(userId))
  }
}