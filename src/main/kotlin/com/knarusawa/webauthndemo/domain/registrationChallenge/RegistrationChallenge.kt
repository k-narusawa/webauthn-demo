package com.knarusawa.webauthndemo.domain.registrationChallenge

import com.knarusawa.webauthndemo.adapter.gateway.db.record.RegistrationChallengeRecord
import com.knarusawa.webauthndemo.domain.user.UserId
import com.webauthn4j.data.client.challenge.Challenge
import java.time.LocalDateTime

class RegistrationChallenge private constructor(
    val userId: UserId,
    val challenge: String,
    val expiredAt: LocalDateTime,
) {
    companion object {
        fun of(userId: UserId, challenge: Challenge): RegistrationChallenge {
            return RegistrationChallenge(
                userId = userId,
                challenge = challenge.toString(),
                expiredAt = LocalDateTime.now().plusSeconds(60000)
            )
        }

        fun from(record: RegistrationChallengeRecord) = RegistrationChallenge(
            userId = UserId.from(record.userId),
            challenge = record.challenge,
            expiredAt = record.expiredAt
        )
    }

}