package com.knarusawa.webauthndemo.adapter.gateway.db

import com.knarusawa.webauthndemo.adapter.gateway.db.dao.CredentialsDao
import com.knarusawa.webauthndemo.application.dto.CredentialsDto
import com.knarusawa.webauthndemo.application.query.CredentialsDtoQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CredentialsDtoQueryServiceImpl(
  private val credentialsDao: CredentialsDao
) : CredentialsDtoQueryService {
  override fun findByUserId(userId: String): List<CredentialsDto> {
    val record = credentialsDao.findByUserId(userId)
    return record.map {
      CredentialsDto(
        credentialId = it.credentialId,
        userId = it.userId,
        serializedAttestedCredentialData = it.serializedAttestedCredentialData,
        serializedEnvelope = it.serializedEnvelope,
        serializedTransports = it.serializedTransports,
        serializedAuthenticatorExtensions = it.serializedAuthenticatorExtensions,
        serializedClientExtensions = it.serializedClientExtensions,
        counter = it.counter
      )
    }
  }
}