package com.knarusawa.webauthndemo.adapter.controller

import com.knarusawa.webauthndemo.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/logout")
class LogoutController {
  private val log = logger()

  @PostMapping
  fun apiV1LogoutPost(request: HttpServletRequest, response: HttpServletResponse) {
    val authentication = SecurityContextHolder.getContext().authentication
    if (authentication != null) {
      SecurityContextLogoutHandler().logout(request, response, authentication)
    }
  }
}