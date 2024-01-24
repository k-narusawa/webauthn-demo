package com.knarusawa.webauthndemo.config

import com.yubico.webauthn.CredentialRepository
import com.yubico.webauthn.RelyingParty
import com.yubico.webauthn.data.RelyingPartyIdentity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class YubicoConfig {
    @Bean
    fun relyingParty(credentialRepository: CredentialRepository): RelyingParty {
        val rpIdentity = RelyingPartyIdentity.builder()
                .id("localhost")
                .name("Example Application")
                .build()
        return RelyingParty.builder()
                .identity(rpIdentity)
                .credentialRepository(credentialRepository)
                .allowOriginPort(true)
                .build()
    }
}