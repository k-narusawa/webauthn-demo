package com.knarusawa.webauthndemo.application.finishWebAuthnAuthenticate

import com.knarusawa.webauthndemo.domain.user.UserId

data class FinishWebAuthnAuthenticationOutputData(
  val userId: UserId
)