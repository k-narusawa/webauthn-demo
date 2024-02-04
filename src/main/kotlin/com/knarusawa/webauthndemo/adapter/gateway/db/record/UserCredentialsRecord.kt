package com.knarusawa.webauthndemo.adapter.gateway.db.record

import com.knarusawa.webauthndemo.domain.userCredentials.UserCredentials
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_credentials")
data class UserCredentialsRecord(
    @Id
    @Column(name = "credential_id")
    val credentialId: String,
    
    @Column(name = "user_id")
    val userId: String,

    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(userCredentials: UserCredentials) = UserCredentialsRecord(
            credentialId = userCredentials.credentialId,
            userId = userCredentials.userId.value(),
        )
    }
}