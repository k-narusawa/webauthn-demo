package com.knarusawa.webauthndemo.adapter.gateway.db.dao

import com.knarusawa.webauthndemo.adapter.gateway.db.record.RegistrationChallengeRecord
import org.springframework.data.repository.CrudRepository

interface RegistrationChallengeDao : CrudRepository<RegistrationChallengeRecord, String> {
    fun findByFlowId(flowId: String): RegistrationChallengeRecord?
    fun findByUserId(userId: String): List<RegistrationChallengeRecord>
    fun deleteByUserId(userId: String)
}