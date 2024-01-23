package com.knarusawa.webauthndemo.adapter.middleware

import com.knarusawa.webauthndemo.adapter.exception.PasswordNotMatchException
import com.knarusawa.webauthndemo.application.LoginUserDetailsService
import com.knarusawa.webauthndemo.util.logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthenticationProvider(
    private val loginUserDetailsService: LoginUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : org.springframework.security.authentication.AuthenticationProvider {
    private val log = logger()
    override fun supports(authentication: Class<*>?): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.principal as String
        val password = authentication.credentials as String

        log.info("username: $username")

        val user = loginUserDetailsService.loadUserByUsername(username)

        if (!passwordEncoder.matches(password, user.password)) {
            throw PasswordNotMatchException("パスワードが一致しませんでした")
        }

        return UsernamePasswordAuthenticationToken(user, password)
    }
}