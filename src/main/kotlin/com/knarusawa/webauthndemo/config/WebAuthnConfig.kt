package com.knarusawa.webauthndemo.config

import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean

@Configurable
class WebAuthnConfig {

    @Bean
    fun serverProperty(challenge: ByteArray): ServerProperty {
        val origin = Origin.create("http://localhost:3000")
        val rpId = "localhost"

        return ServerProperty(origin, rpId, DefaultChallenge(challenge), null)
    }
}