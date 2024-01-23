package com.knarusawa.webauthndemo.adapter.gateway.db.dao

import com.knarusawa.webauthndemo.adapter.gateway.db.record.UserRecord
import org.springframework.data.repository.CrudRepository

interface UserDao : CrudRepository<UserRecord, String> {
    fun findByUsername(username: String): UserRecord?
}