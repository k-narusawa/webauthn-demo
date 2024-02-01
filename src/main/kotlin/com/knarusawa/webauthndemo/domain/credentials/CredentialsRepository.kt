package com.knarusawa.webauthndemo.domain.credentials

interface CredentialsRepository {
    fun save(credentials: Credentials)
}