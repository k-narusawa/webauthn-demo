package com.knarusawa.webauthndemo.application.finishWebAuthnAuthenticate

import com.knarusawa.webauthndemo.config.WebAuthnConfig
import com.knarusawa.webauthndemo.domain.challenge.ChallengeDataRepository
import com.knarusawa.webauthndemo.domain.credentials.CredentialRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.knarusawa.webauthndemo.util.logger
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.data.AuthenticationParameters
import com.webauthn4j.data.AuthenticationRequest
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
    private val log = logger()
  }

  @Transactional
  fun exec(inputData: FinishWebAuthnAuthenticationInputData): FinishWebAuthnAuthenticationOutputData {
    val userId = inputData.userHandle
      ?: throw RuntimeException("ユーザーの識別に失敗しました")

    val challengeData = challengeDataRepository.findByChallenge(inputData.challenge)
      ?: throw IllegalArgumentException("flow is not found")

    val credential = credentialRepository.findByCredentialId(inputData.credentialId)
      ?: throw IllegalArgumentException("credential is not found")

    val authenticator = credential.getAuthenticator()

    val authenticationParameter = AuthenticationParameters(
      /* serverProperty =           */ webAuthnConfig.serverProperty(challengeData.challenge),
      /* authenticator =            */ authenticator,
      /* allowCredentials =         */ listOf(Base64UrlUtil.decode(credential.credentialId)),
      /* userVerificationRequired = */ true
    )

    val authenticationRequest = AuthenticationRequest(
      /* credentialId =      */ Base64UrlUtil.decode(inputData.credentialId),
      /* userHandle =        */ inputData.userHandle.let { Base64UrlUtil.decode(it) },
      /* authenticatorData = */ Base64UrlUtil.decode(inputData.authenticatorData),
      /* clientDataJSON =    */ Base64UrlUtil.decode(inputData.clientDataJSON),
      /* signature =         */ Base64UrlUtil.decode(inputData.signature),
    )

    val result = credential.isValid(
      webAuthnManager = webAuthnManager,
      authenticationRequest = authenticationRequest,
      authenticationParameter = authenticationParameter
    )

    if (!result) {
      throw WebAuthnException("Credential is not valid.")
    }

    credentialRepository.save(credential)
    challengeDataRepository.deleteByChallenge(inputData.challenge)

    log.debug("Credential is authenticated successfully.")
    log.debug(credential.toString())

    return FinishWebAuthnAuthenticationOutputData(userId = UserId.from(userId))
  }
}