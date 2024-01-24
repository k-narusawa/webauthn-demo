package com.knarusawa.webauthndemo.application.startRegistrationWebAuthn

import com.knarusawa.webauthndemo.domain.user.UserId

data class StartRegistrationWebAuthnInputData(
        val userId: UserId,
        val username: String
)