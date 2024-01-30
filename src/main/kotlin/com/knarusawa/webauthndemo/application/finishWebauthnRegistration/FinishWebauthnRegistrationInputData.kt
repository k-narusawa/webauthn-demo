package com.knarusawa.webauthndemo.application.finishWebauthnRegistration

import com.knarusawa.webauthndemo.domain.user.LoginUserDetails

data class FinishWebauthnRegistrationInputData(
        val user: LoginUserDetails
)
