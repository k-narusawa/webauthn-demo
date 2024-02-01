package com.knarusawa.webauthndemo.adapter.gateway.db.dao

import com.knarusawa.webauthndemo.adapter.gateway.db.record.UserCredentialsRecord
import org.springframework.data.repository.CrudRepository

interface UserCredentialsDao : CrudRepository<UserCredentialsRecord, String> {
    fun findByUserId(userId: String): List<UserCredentialsRecord>
}