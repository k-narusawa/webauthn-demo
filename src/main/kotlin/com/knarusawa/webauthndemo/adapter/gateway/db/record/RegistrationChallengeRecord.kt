package com.knarusawa.webauthndemo.adapter.gateway.db.record

import com.knarusawa.webauthndemo.domain.registrationChallenge.RegistrationChallenge
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "registration_challenges")
data class RegistrationChallengeRecord(
        @Id
        @Column(name = "flow_id")
        val flowId: String,

        @Column(name = "user_id")
        val userId: String,

        @Column(name = "challenge")
        val challenge: String,

        @Column(name = "expired_at")
        val expiredAt: LocalDateTime,

        @Column(name = "created_at", insertable = false, updatable = false)
        val createdAt: LocalDateTime? = null,

        @Column(name = "updated_at", insertable = false, updatable = false)
        val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(registrationChallenge: RegistrationChallenge) = RegistrationChallengeRecord(
                flowId = registrationChallenge.flowId.value(),
                userId = registrationChallenge.userId.toString(),
                challenge = registrationChallenge.challenge,
                expiredAt = registrationChallenge.expiredAt
        )
    }
}