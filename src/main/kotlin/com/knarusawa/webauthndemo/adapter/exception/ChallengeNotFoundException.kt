package com.knarusawa.webauthndemo.adapter.exception

data class ChallengeNotFoundException(
    val userId: String,
    val challenge: String?,
    override val message: String = "Challengeが見つかりませんでした",
    override val cause: Throwable? = null
) : RuntimeException(message, cause)