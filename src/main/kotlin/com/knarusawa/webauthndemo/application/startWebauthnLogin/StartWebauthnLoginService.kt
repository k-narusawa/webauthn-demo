package com.knarusawa.webauthndemo.application.startWebauthnLogin

import com.knarusawa.webauthndemo.domain.flow.Flow
import com.knarusawa.webauthndemo.domain.flow.FlowRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.knarusawa.webauthndemo.domain.userCredentials.UserCredentialsRepository
import com.webauthn4j.data.*
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.util.Base64Util
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class StartWebauthnLoginService(
        private val flowRepository: FlowRepository,
        private val userCredentialsRepository: UserCredentialsRepository,
) {
    companion object {
        private val RP = PublicKeyCredentialRpEntity("localhost", "localhost")
    }

    @Transactional
    fun exec(inputData: StartWebauthnLoginInputData): StartWebauthnLoginOutputData {
        val challenge = DefaultChallenge()

        val flow = Flow.of(userId = UserId.from(inputData.userId), challenge = challenge)

        val userCredentials = userCredentialsRepository.findByUserId(UserId.from(inputData.userId))

        val allowCredentials = userCredentials.map {
            PublicKeyCredentialDescriptor(
                    PublicKeyCredentialType.PUBLIC_KEY,
                    Base64Util.decode(it.credentialId),
                    HashSet(listOf(AuthenticatorTransport.USB,
                            AuthenticatorTransport.BLE,
                            AuthenticatorTransport.INTERNAL,
                            AuthenticatorTransport.NFC)
                    )
            )
        }

        val options = PublicKeyCredentialRequestOptions(
                challenge,
                60000,
                RP.id,
                allowCredentials,
                UserVerificationRequirement.PREFERRED,
                null
        )
        flowRepository.save(flow)

        return StartWebauthnLoginOutputData(
                flowId = flow.flowId,
                options = options
        )
    }
}