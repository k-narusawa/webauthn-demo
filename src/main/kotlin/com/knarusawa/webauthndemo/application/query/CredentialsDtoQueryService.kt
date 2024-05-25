package com.knarusawa.webauthndemo.application.query

import com.knarusawa.webauthndemo.application.dto.WebAuthnCredentialDto

interface CredentialsDtoQueryService {
  fun findByUserId(userId: String): List<WebAuthnCredentialDto>
}