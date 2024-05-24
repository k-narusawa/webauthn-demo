package com.knarusawa.webauthndemo.application.exception

class WebAuthnVerificationFailedException(
    val userId: String,
    val challenge: String?,
    override val message: String = "WebAuthnの検証に失敗しました",
    override val cause: Throwable? = null
) : RuntimeException(message, cause)