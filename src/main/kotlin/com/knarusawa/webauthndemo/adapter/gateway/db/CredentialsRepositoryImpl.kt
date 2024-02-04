package com.knarusawa.webauthndemo.adapter.gateway.db

import com.knarusawa.webauthndemo.adapter.gateway.db.dao.CredentialsDao
import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import com.knarusawa.webauthndemo.domain.credentials.Credentials
import com.knarusawa.webauthndemo.domain.credentials.CredentialsRepository
import org.springframework.stereotype.Repository

@Repository
class CredentialsRepositoryImpl(
        private val credentialsDao: CredentialsDao
) : CredentialsRepository {
    override fun save(credentials: Credentials) {
        credentialsDao.save(CredentialsRecord.from(credentials))
    }

    override fun findByCredentialId(credentialId: String): Credentials? {
        return credentialsDao.findByCredentialId(credentialId)?.let {
            Credentials.from(it)
        }
    }
}