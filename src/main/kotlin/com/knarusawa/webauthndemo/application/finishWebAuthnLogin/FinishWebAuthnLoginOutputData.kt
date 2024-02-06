package com.knarusawa.webauthndemo.application.finishWebAuthnLogin

import com.knarusawa.webauthndemo.domain.user.UserId

data class FinishWebAuthnLoginOutputData(
        val userId: UserId
)