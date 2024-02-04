package com.knarusawa.webauthndemo.adapter.gateway.db

import com.knarusawa.webauthndemo.adapter.gateway.db.dao.RegistrationChallengeDao
import com.knarusawa.webauthndemo.adapter.gateway.db.record.RegistrationChallengeRecord
import com.knarusawa.webauthndemo.domain.flow.Flow
import com.knarusawa.webauthndemo.domain.flow.FlowId
import com.knarusawa.webauthndemo.domain.flow.FlowRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import org.springframework.stereotype.Repository

@Repository
class FlowRepositoryImpl(
        private val registrationChallengeDao: RegistrationChallengeDao
) : FlowRepository {
    override fun save(challenge: Flow) {
        registrationChallengeDao.save(RegistrationChallengeRecord.from(challenge))
    }

    override fun findByFlowId(flowId: FlowId): Flow? {
        return registrationChallengeDao.findByFlowId(flowId.value())?.let {
            Flow.from(it)
        }
    }

    override fun findByUserId(userId: UserId): List<Flow> {
        val records = registrationChallengeDao.findByUserId(userId = userId.toString())

        return records.map {
            Flow.from(it)
        }
    }

    override fun deleteByUserId(userId: UserId) {
        registrationChallengeDao.deleteByUserId(userId = userId.value())
    }
}