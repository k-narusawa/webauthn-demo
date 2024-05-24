package com.knarusawa.webauthndemo.adapter.gateway.db

import com.knarusawa.webauthndemo.adapter.gateway.db.dao.CredentialsDao
import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import com.knarusawa.webauthndemo.domain.credentials.Credential
import com.knarusawa.webauthndemo.domain.credentials.CredentialRepository
import org.springframework.stereotype.Repository

@Repository
class CredentialRepositoryImpl(
    private val credentialsDao: CredentialsDao
) : CredentialRepository {
    override fun save(credential: Credential) {
        credentialsDao.save(CredentialsRecord.from(credential))
    }

    override fun findByCredentialId(credentialId: String): Credential? {
        return credentialsDao.findByCredentialId(credentialId)?.let {
            Credential.from(it)
        }
    }

    override fun findByUserId(userId: String): List<Credential> {
        return credentialsDao.findByUserId(userId).map {
            Credential.from(it)
        }
    }
}