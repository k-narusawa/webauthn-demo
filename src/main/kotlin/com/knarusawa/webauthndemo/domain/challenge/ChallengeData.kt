package com.knarusawa.webauthndemo.domain.challenge

import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.util.Base64UrlUtil

class ChallengeData private constructor(
  val challenge: DefaultChallenge,
) {
  companion object {
    fun of(): ChallengeData {
      return ChallengeData(
        challenge = DefaultChallenge(),
      )
    }
  }

  fun getRawChallenge(): String {
    return Base64UrlUtil.encodeToString(challenge.value)
  }
}