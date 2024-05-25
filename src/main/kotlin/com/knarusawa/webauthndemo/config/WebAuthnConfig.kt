package com.knarusawa.webauthndemo.config

import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "env.webauthn")
class WebAuthnConfig {
  var rpId: String? = null
  var origins: List<String> = mutableListOf()

  fun serverProperty(challenge: DefaultChallenge): ServerProperty {
    val origins = this.origins.map {
      Origin.create(it)
    }.toSet()
    val rpId = this.rpId ?: throw RuntimeException("rpIdが設定されていません")

    return ServerProperty(origins, rpId, challenge, null)
  }
}