package com.knarusawa.webauthndemo.domain.flow

import com.knarusawa.webauthndemo.domain.user.UserId

interface FlowRepository {
    fun save(challenge: Flow)
    fun findByFlowId(flowId: FlowId): Flow?
    fun findByUserId(userId: UserId): List<Flow>
    fun deleteByUserId(userId: UserId)
}