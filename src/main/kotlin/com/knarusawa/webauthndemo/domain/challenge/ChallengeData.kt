package com.knarusawa.webauthndemo.domain.challenge

import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.util.Base64UrlUtil

class ChallengeData private constructor(
    val challenge: String,
) {
    companion object {
        fun of(challenge: DefaultChallenge): ChallengeData {
            return ChallengeData(
                challenge = Base64UrlUtil.encodeToString(challenge.value),
            )
        }
    }
}