package com.knarusawa.webauthndemo.adapter.exception

import org.springframework.security.core.AuthenticationException

data class PasswordNotMatchException(
        val username: String,
        override val message: String = "パスワードの検証に失敗しました",
        override val cause: Throwable? = null
) : AuthenticationException(message, cause)