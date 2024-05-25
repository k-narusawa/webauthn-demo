package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.adapter.controller.dto.UserinfoGetResponse
import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/userinfo")
class UserinfoController {
  @GetMapping
  fun apiV1UserinfoGet(): UserinfoGetResponse {
    val authentication = SecurityContextHolder.getContext().authentication
    val user = authentication.principal as? LoginUserDetails
      ?: throw IllegalStateException("Principalが不正")

    return UserinfoGetResponse(
      username = user.username
    )
  }
}