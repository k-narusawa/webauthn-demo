package com.knarusawa.webauthndemo.domain.credentials

import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import com.webauthn4j.util.Base64UrlUtil

class Credentials private constructor(
        val credentialId: String,
        val serializedAttestedCredentialData: String,
        val serializedEnvelope: String,
        val serializedTransports: String,
        val serializedAuthenticatorExtensions: String,
        val serializedClientExtensions: String,
        val counter: Long,
) {
    companion object {
        fun of(
                credentialId: String,
                serializedAttestedCredentialData: ByteArray,
                serializedEnvelope: ByteArray,
                serializedTransports: ByteArray,
                serializedAuthenticatorExtensions: ByteArray,
                serializedClientExtensions: ByteArray,
                counter: Long,
        ) = Credentials(
                credentialId = credentialId,
                serializedAttestedCredentialData = Base64UrlUtil.encodeToString(serializedAttestedCredentialData),
                serializedEnvelope = Base64UrlUtil.encodeToString(serializedEnvelope),
                serializedTransports = Base64UrlUtil.encodeToString(serializedTransports),
                serializedAuthenticatorExtensions = Base64UrlUtil.encodeToString(serializedAuthenticatorExtensions),
                serializedClientExtensions = Base64UrlUtil.encodeToString(serializedClientExtensions),
                counter = counter,
        )

        fun from(record: CredentialsRecord) = Credentials(
                credentialId = record.credentialId,
                serializedAttestedCredentialData = record.serializedAttestedCredentialData,
                serializedEnvelope = record.serializedEnvelope,
                serializedTransports = record.serializedTransports,
                serializedAuthenticatorExtensions = record.serializedAuthenticatorExtensions,
                serializedClientExtensions = record.serializedClientExtensions,
                counter = record.counter,
        )
    }
}