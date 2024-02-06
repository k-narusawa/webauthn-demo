package com.knarusawa.webauthndemo.domain.credentials

interface CredentialsRepository {
    fun save(credentials: Credentials)
    fun findByCredentialId(credentialId: String): Credentials?
    fun findByUserId(userId: String): List<Credentials>
}