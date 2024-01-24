package com.knarusawa.webauthndemo.application.startRegistrationWebAuthn

import com.knarusawa.webauthndemo.domain.registrationFlow.RegistrationFlow
import com.knarusawa.webauthndemo.util.YubicoUtil
import com.yubico.webauthn.RelyingParty
import com.yubico.webauthn.StartRegistrationOptions
import com.yubico.webauthn.data.AuthenticatorSelectionCriteria
import com.yubico.webauthn.data.UserIdentity
import com.yubico.webauthn.data.UserVerificationRequirement
import org.springframework.stereotype.Service
import java.util.*

@Service
class StartRegistrationWebAuthnService(
        private val relyingParty: RelyingParty
) {
    fun exec(inputData: StartRegistrationWebAuthnInputData): StartRegistrationWebAuthnOutputData {
        val userIdentity = UserIdentity.builder()
                .name(inputData.username)
                .displayName(inputData.username)
                .id(YubicoUtil.toByteArray(UUID.fromString(inputData.userId.value())))
                .build()

        val authenticatorSelectionCriteria = AuthenticatorSelectionCriteria.builder()
                .userVerification(UserVerificationRequirement.DISCOURAGED)
                .build()
        val startRegistrationOptions = StartRegistrationOptions.builder()
                .user(userIdentity)
                .timeout(60000)
                .authenticatorSelection(authenticatorSelectionCriteria)
                .build()

        val options = this.relyingParty.startRegistration(startRegistrationOptions)
        val flow = RegistrationFlow.of(
                userId = inputData.userId,
                createOptions = options.toJson()
        )

        return StartRegistrationWebAuthnOutputData(
                flowId = flow.flowId.toString(),
                credentialCreationOptions = options
        )
    }
}