package com.knarusawa.webauthndemo.domain.user

import com.knarusawa.webauthndemo.adapter.gateway.db.record.UserRecord

class User private constructor(
    val userId: UserId,
    username: Username,
    password: Password,
    isAccountLock: Boolean,
    failedAttempts: Int,
    isDisabled: Boolean,
) {
    var username = username
        private set
    var password = password
        private set
    var isAccountLock = isAccountLock
        private set
    var failedAttempts = failedAttempts
        private set
    var isDisabled = isDisabled
        private set

    companion object {
        fun of(username: String, password: String): User {
            return User(
                userId = UserId.of(),
                username = Username.of(value = username),
                password = Password.fromRawPassword(value = password),
                isAccountLock = false,
                failedAttempts = 0,
                isDisabled = false
            )
        }

        fun from(record: UserRecord) = User(
            userId = UserId.from(recordString = record.userId),
            username = Username.of(value = record.username),
            password = Password.fromRecordValue(value = record.password),
            isAccountLock = record.isAccountLock,
            failedAttempts = record.failedAttempts,
            isDisabled = record.isDisabled
        )
    }

    fun changeUsername(value: String) {
        this.username = Username.of(value = value)
    }

    fun changePassword(value: String) {
        this.password = Password.fromRawPassword(value = value)
    }
}