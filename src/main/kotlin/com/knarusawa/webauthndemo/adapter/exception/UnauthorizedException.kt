package com.knarusawa.webauthndemo.adapter.exception

import org.springframework.security.core.AuthenticationException

data class UnauthorizedException(
        override val message: String = "認証エラーです",
        override val cause: Throwable? = null
) : AuthenticationException(message, cause)