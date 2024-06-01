package com.knarusawa.webauthndemo.domain.credentials

import org.springframework.stereotype.Repository

@Repository
interface CredentialRepository {
  fun save(credential: Credential)
  fun findByCredentialId(credentialId: String): Credential?
  fun findByUserId(userId: String): List<Credential>
}