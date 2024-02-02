package com.knarusawa.webauthndemo.domain.registrationChallenge

import com.knarusawa.webauthndemo.domain.user.UserId

interface RegistrationChallengeRepository {
    fun save(challenge: RegistrationChallenge)
    fun findByFlowId(flowId: FlowId): RegistrationChallenge?
    fun findByUserId(userId: UserId): List<RegistrationChallenge>
    fun deleteByUserId(userId: UserId)
}