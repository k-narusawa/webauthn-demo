package com.knarusawa.webauthndemo.application.query

import com.knarusawa.webauthndemo.application.dto.CredentialsDto

interface CredentialsDtoQueryService {
  fun findByUserId(userId: String): List<CredentialsDto>
}