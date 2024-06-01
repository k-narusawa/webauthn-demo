package com.knarusawa.webauthndemo.adapter.gateway.db.dao

import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import org.springframework.data.repository.CrudRepository

interface CredentialsDao : CrudRepository<CredentialsRecord, String> {
  fun findByCredentialId(credentialId: String): CredentialsRecord?
  fun findByUserId(userId: String): List<CredentialsRecord>
}