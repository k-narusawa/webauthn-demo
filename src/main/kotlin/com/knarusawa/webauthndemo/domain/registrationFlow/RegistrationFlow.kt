package com.knarusawa.webauthndemo.domain.registrationFlow

import com.knarusawa.webauthndemo.domain.user.UserId

class RegistrationFlow private constructor(
        val flowId: RegistrationFlowId,
        val userId: UserId,
        registrationResult: String?,
        val createOptions: String,
) {
    var registrationResult = registrationResult
        private set

    companion object {
        fun of(userId: UserId, createOptions: String): RegistrationFlow {
            return RegistrationFlow(
                    flowId = RegistrationFlowId.of(),
                    userId = userId,
                    registrationResult = null,
                    createOptions = createOptions
            )
        }
    }
}