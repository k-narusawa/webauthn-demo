package com.knarusawa.webauthndemo.application.startWebauthnLogin

import com.webauthn4j.data.client.challenge.DefaultChallenge
import org.springframework.stereotype.Service

@Service
class StartWebauthnLoginService {
    fun exec(inputData: StartWebauthnLoginInputData) {
        val challenge = DefaultChallenge()

//        PublicKeyCredentialRequestOptions()
    }
}