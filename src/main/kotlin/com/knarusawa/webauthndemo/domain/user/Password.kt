package com.knarusawa.webauthndemo.domain.user

import com.knarusawa.webauthndemo.config.SecurityConfig

data class Password private constructor(
        private val value: String,
        private var consumed: Boolean
) {
    companion object {
        private val LENGTH_RANGE = (8..64)
        private val PATTERN = ".*[a-zA-Z0-9.!?-]{8,64}".toRegex()

        fun fromRawPassword(value: String): Password {
            return value
                    .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(value) }
                    ?.let {
                        Password(
                                value = SecurityConfig().passwordEncoder().encode(value),
                                consumed = false
                        )
                    }
                    ?: throw IllegalArgumentException("パスワードの値が不正です")
        }

        fun fromRecordValue(value: String): Password {
            return Password(
                    value = value,
                    consumed = false
            )
        }
    }

    fun value(): String {
        if (consumed) {
            throw IllegalStateException("すでに使用済みのパスワードオブジェクトです")
        }
        this.consumed = true
        return this.value
    }

    override fun toString(): String {
        return "Password{ [PROTECTED] }"
    }
}