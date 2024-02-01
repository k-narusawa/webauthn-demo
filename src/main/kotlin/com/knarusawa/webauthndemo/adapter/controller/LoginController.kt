package com.knarusawa.webauthndemo.adapter.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class LoginController {
    @GetMapping("/webauthn/start")
    fun apiV1LoginWebauthnStartGet() {

    }
}