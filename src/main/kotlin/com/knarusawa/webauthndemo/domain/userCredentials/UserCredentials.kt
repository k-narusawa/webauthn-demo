package com.knarusawa.webauthndemo.domain.userCredentials

import com.knarusawa.webauthndemo.adapter.gateway.db.record.UserCredentialsRecord
import com.knarusawa.webauthndemo.domain.user.UserId

class UserCredentials(
        val credentialId: String,
        val userId: UserId,
) {
    companion object {
        fun of(credentialId: String, userId: UserId) = UserCredentials(
                credentialId = credentialId,
                userId = userId
        )

        fun fromRecord(record: UserCredentialsRecord) = UserCredentials(
                credentialId = record.credentialId,
                userId = UserId.from(record.userId)
        )
    }
}