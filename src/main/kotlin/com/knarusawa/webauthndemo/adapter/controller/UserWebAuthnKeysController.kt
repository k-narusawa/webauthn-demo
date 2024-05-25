package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.ApiV1UsersCredentialsGet
import com.knarusawa.webauthndemo.application.query.CredentialsDtoQueryService
import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users/webauthn/keys")
class UserWebAuthnKeysController(
  private val credentialsDtoQueryService: CredentialsDtoQueryService
) {
  @GetMapping
  fun v1UsersWebAuthnKeys(): ApiV1UsersCredentialsGet {
    val authentication = SecurityContextHolder.getContext().authentication
    val user = authentication.principal as? LoginUserDetails
      ?: throw IllegalStateException("Principalが不正")

    val dto = credentialsDtoQueryService.findByUserId(user.userId)

    return ApiV1UsersCredentialsGet(
      dto.map {
        ApiV1UsersCredentialsGet.Key(
          credentialId = it.credentialId,
          userId = it.userId,
          aaguid = it.aaguid,
          label = it.label
        )
      }
    )

  }
}
