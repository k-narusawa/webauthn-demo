package com.knarusawa.webauthndemo.application.finishWebauthnLogin

import com.knarusawa.webauthndemo.domain.user.UserId

data class FinishWebauthnLoginOutputData(
    val userId: UserId
)