package com.knarusawa.webauthndemo.adapter.middleware

import com.knarusawa.webauthndemo.adapter.exception.PasswordNotMatchException
import com.knarusawa.webauthndemo.application.LoginUserDetailsService
import com.knarusawa.webauthndemo.application.finishWebAuthnLogin.FinishWebAuthnLoginInputData
import com.knarusawa.webauthndemo.application.finishWebAuthnLogin.FinishWebAuthnLoginService
import com.knarusawa.webauthndemo.domain.authenticationToken.WebauthnAssertionAuthenticationToken
import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import com.knarusawa.webauthndemo.domain.user.UserRepository
import com.knarusawa.webauthndemo.util.logger
import com.webauthn4j.util.Base64UrlUtil
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthenticationProvider(
    private val userRepository: UserRepository,
    private val loginUserDetailsService: LoginUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val finishWebauthnLoginService: FinishWebAuthnLoginService,
) : org.springframework.security.authentication.AuthenticationProvider {
    private val log = logger()
    override fun supports(authentication: Class<*>?): Boolean {
        return AbstractAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication is WebauthnAssertionAuthenticationToken) {
            val webAuthnRequest = authentication.principal

            val inputData = FinishWebAuthnLoginInputData(
                flowId = authentication.flowId,
                credentialId = Base64UrlUtil.encodeToString(webAuthnRequest.credentialId),
                clientDataJSON = Base64UrlUtil.encodeToString(webAuthnRequest.clientDataJSON),
                authenticatorData = Base64UrlUtil.encodeToString(webAuthnRequest.authenticatorData),
                signature = Base64UrlUtil.encodeToString(webAuthnRequest.signature),
                userHandle = authentication.userHandle,
            )
            val outputData = finishWebauthnLoginService.exec(inputData)

            val user = userRepository.findByUserId(userId = outputData.userId)
            val loginUser = LoginUserDetails(user!!)

            return UsernamePasswordAuthenticationToken(loginUser, null)
        }

        val username = authentication.principal as String
        val password = authentication.credentials as String

        val user = loginUserDetailsService.loadUserByUsername(username)

        if (!passwordEncoder.matches(password, user.password)) {
            throw PasswordNotMatchException("パスワードが一致しませんでした")
        }

        return UsernamePasswordAuthenticationToken(user, password)
    }
}