package com.knarusawa.webauthndemo.adapter.gateway.db.dao

import com.knarusawa.webauthndemo.adapter.gateway.db.record.RegistrationChallengeRecord
import org.springframework.data.repository.CrudRepository

interface RegistrationChallengeDao : CrudRepository<RegistrationChallengeRecord, String> {
    fun findByUserId(userId: String): RegistrationChallengeRecord?
}