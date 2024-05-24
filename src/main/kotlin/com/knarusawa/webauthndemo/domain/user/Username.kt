package com.knarusawa.webauthndemo.domain.user

data class Username private constructor(
    private val value: String
) {
    companion object {
        private val LENGTH_RANGE = (10..100)

        fun of(value: String): Username {
            return value
                .takeIf { LENGTH_RANGE.contains(value = it.length) }
                ?.let { Username(value = value) }
                ?: throw IllegalArgumentException("usernameの値が不正です")
        }
    }

    fun value() = this.value

    override fun toString(): String {
        return "Username: { value: ***** }"
    }
}