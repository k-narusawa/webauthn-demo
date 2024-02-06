package com.knarusawa.webauthndemo.adapter.gateway.db

import com.knarusawa.webauthndemo.adapter.gateway.db.dao.UserCredentialsDao
import com.knarusawa.webauthndemo.adapter.gateway.db.record.UserCredentialsRecord
import com.knarusawa.webauthndemo.domain.user.UserId
import com.knarusawa.webauthndemo.domain.userCredentials.UserCredentials
import com.knarusawa.webauthndemo.domain.userCredentials.UserCredentialsRepository
import org.springframework.stereotype.Repository

@Repository
class UserCredentialsRepositoryImpl(
        private val userCredentialsDao: UserCredentialsDao
) : UserCredentialsRepository {
    override fun save(userCredentials: UserCredentials) {
        userCredentialsDao.save(UserCredentialsRecord.from(userCredentials))
    }

    override fun findByUserId(userId: UserId): List<UserCredentials> {
        val records = userCredentialsDao.findByUserId(userId = userId.value())

        return records.map {
            UserCredentials.fromRecord(it)
        }
    }
}