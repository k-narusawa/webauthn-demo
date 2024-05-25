package com.knarusawa.webauthndemo.adapter.gateway.db

import com.knarusawa.webauthndemo.adapter.gateway.db.dao.CredentialsDao
import com.knarusawa.webauthndemo.application.dto.WebAuthnCredentialDto
import com.knarusawa.webauthndemo.application.query.CredentialsDtoQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WebAuthnCredentialDtoQueryServiceImpl(
  private val credentialsDao: CredentialsDao
) : CredentialsDtoQueryService {
  override fun findByUserId(userId: String): List<WebAuthnCredentialDto> {
    val record = credentialsDao.findByUserId(userId)
    return record.map {
      WebAuthnCredentialDto(
        credentialId = it.credentialId,
        userId = it.userId,
        aaguid = it.aaguid,
        label = it.label,
        counter = it.counter
      )
    }
  }
}