package com.knarusawa.webauthndemo.domain.registrationFlow

import java.util.*

data class RegistrationFlowId private constructor(
        val value: UUID
) {
    companion object {
        fun of(): RegistrationFlowId {
            return RegistrationFlowId(UUID.randomUUID())
        }
    }
}

