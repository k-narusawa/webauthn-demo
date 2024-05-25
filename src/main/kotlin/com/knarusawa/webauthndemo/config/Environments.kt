package com.knarusawa.webauthndemo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Environments {
    @Value("$\\{env.webauthn.rp-id\\}")
    private lateinit var rpId: String

    @Value("$\\{env.webauthn.origins\\}")
    private lateinit var origins: String
}