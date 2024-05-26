package com.knarusawa.webauthndemo.adapter.gateway.db.record

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserRecord(
        @Id
        @Column(name = "user_id")
        val userId: String,

        @Column(name = "username")
        val username: String = "",

        @Column(name = "password")
        val password: String = "",

        @Column(name = "is_account_lock")
        val isAccountLock: Boolean = false,

        @Column(name = "failed_attempts")
        val failedAttempts: Int = 0,

        @Column(name = "is_disabled")
        val isDisabled: Boolean = false,

        @Column(name = "created_at", insertable = false, updatable = false)
        val createdAt: LocalDateTime? = null,

        @Column(name = "updated_at", insertable = false, updatable = false)
        val updatedAt: LocalDateTime? = null
)
