package com.knarusawa.webauthndemo.domain.registrationChallenge

import java.util.*

data class FlowId private constructor(
        val value: UUID
) {
    companion object {
        fun of() = FlowId(value = UUID.randomUUID())
        fun from(value: String) = FlowId(value = UUID.fromString(value))
    }

    fun value() = value.toString()
}
