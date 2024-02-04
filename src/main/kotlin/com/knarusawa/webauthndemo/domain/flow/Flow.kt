package com.knarusawa.webauthndemo.domain.flow

import com.knarusawa.webauthndemo.adapter.gateway.db.record.RegistrationChallengeRecord
import com.knarusawa.webauthndemo.domain.user.UserId
import com.webauthn4j.data.client.challenge.Challenge
import com.webauthn4j.util.Base64UrlUtil
import java.time.LocalDateTime

class Flow private constructor(
        val flowId: FlowId,
        val userId: UserId,
        val challenge: String,
        val expiredAt: LocalDateTime,
) {
    companion object {
        fun of(userId: UserId, challenge: Challenge): Flow {
            return Flow(
                    flowId = FlowId.of(),
                    userId = userId,
                    challenge = Base64UrlUtil.encodeToString(challenge.value),
                    expiredAt = LocalDateTime.now().plusSeconds(60000)
            )
        }

        fun from(record: RegistrationChallengeRecord) = Flow(
                flowId = FlowId.from(record.flowId),
                userId = UserId.from(record.userId),
                challenge = record.challenge,
                expiredAt = record.expiredAt
        )
    }
}