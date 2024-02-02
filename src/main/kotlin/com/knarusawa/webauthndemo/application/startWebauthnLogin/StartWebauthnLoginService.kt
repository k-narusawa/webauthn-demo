package com.knarusawa.webauthndemo.application.startWebauthnLogin

import com.knarusawa.webauthndemo.domain.flow.Flow
import com.knarusawa.webauthndemo.domain.flow.FlowRepository
import com.knarusawa.webauthndemo.domain.user.UserId
import com.knarusawa.webauthndemo.domain.user.UserRepository
import com.knarusawa.webauthndemo.domain.user.Username
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
        private val userRepository: UserRepository,
) {
    companion object {
        private val RP = PublicKeyCredentialRpEntity("localhost", "localhost")
    }

    @Transactional
    fun exec(inputData: StartWebauthnLoginInputData): StartWebauthnLoginOutputData {
        val challenge = DefaultChallenge()
        val user = userRepository.findByUsername(Username.of(inputData.username))
                ?: throw IllegalArgumentException("User not found")

        val flow = Flow.of(userId = UserId.from(user.userId.value()), challenge = challenge)

        val userCredentials = userCredentialsRepository.findByUserId(UserId.from(user.userId.value()))

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