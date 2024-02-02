package com.knarusawa.webauthndemo.adapter.gateway.db

import com.knarusawa.webauthndemo.adapter.gateway.db.dao.RegistrationChallengeDao
import com.knarusawa.webauthndemo.adapter.gateway.db.record.RegistrationChallengeRecord
import com.knarusawa.webauthndemo.domain.registrationChallenge.RegistrationChallenge
import com.knarusawa.webauthndemo.domain.registrationChallenge.RegistrationChallengeRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import org.springframework.stereotype.Repository

@Repository
class RegistrationChallengeRepositoryImpl(
        private val registrationChallengeDao: RegistrationChallengeDao
) : RegistrationChallengeRepository {
    override fun save(challenge: RegistrationChallenge) {
        registrationChallengeDao.save(RegistrationChallengeRecord.from(challenge))
    }

    override fun findByUserId(userId: UserId): List<RegistrationChallenge> {
        val records = registrationChallengeDao.findByUserId(userId = userId.toString())

        return records.map {
            RegistrationChallenge.from(it)
        }
    }

    override fun deleteByUserId(userId: UserId) {
        registrationChallengeDao.deleteByUserId(userId = userId.value())
    }
}