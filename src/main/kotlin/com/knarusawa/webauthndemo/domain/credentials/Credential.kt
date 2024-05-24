package com.knarusawa.webauthndemo.domain.credentials

import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.util.Base64UrlUtil

class Credential private constructor(
        val credentialId: String,
        val userId: String,
        val serializedAttestedCredentialData: String,
        val serializedEnvelope: String,
        val serializedTransports: String,
        val serializedAuthenticatorExtensions: String,
        val serializedClientExtensions: String,
        counter: Long,
) {
    var counter: Long = counter
        private set

    companion object {
        private val objectConverter = ObjectConverter()

        fun of(
                credentialId: ByteArray?,
                userId: String,
                authenticator: AuthenticatorImpl,
        ): Credential {
            val attestedCredentialDataConverter = AttestedCredentialDataConverter(objectConverter)
            val attestationStatementEnvelope =
                    AttestationStatementEnvelope(authenticator.attestationStatement!!)
            val serializedEnvelope =
                    objectConverter.cborConverter.writeValueAsBytes(attestationStatementEnvelope);

            val serializedAttestedCredentialData =
                    attestedCredentialDataConverter.convert(authenticator.attestedCredentialData)

            return Credential(
                    credentialId = Base64UrlUtil.encodeToString(credentialId),
                    userId = userId,
                    serializedAttestedCredentialData = Base64UrlUtil.encodeToString(
                            serializedAttestedCredentialData
                    ),
                    serializedEnvelope = Base64UrlUtil.encodeToString(serializedEnvelope),
                    serializedTransports = Base64UrlUtil.encodeToString(
                            objectConverter.cborConverter.writeValueAsBytes(
                                    objectConverter.cborConverter.writeValueAsBytes(
                                            authenticator.transports
                                    )
                            )
                    ),
                    serializedAuthenticatorExtensions = Base64UrlUtil.encodeToString(
                            objectConverter.cborConverter.writeValueAsBytes(
                                    authenticator.authenticatorExtensions
                            )
                    ),
                    serializedClientExtensions = Base64UrlUtil.encodeToString(
                            objectConverter.cborConverter.writeValueAsBytes(
                                    authenticator.clientExtensions
                            )
                    ),
                    counter = authenticator.counter,
            )
        }

        fun from(record: CredentialsRecord) = Credential(
                credentialId = record.credentialId,
                userId = record.userId,
                serializedAttestedCredentialData = record.serializedAttestedCredentialData,
                serializedEnvelope = record.serializedEnvelope,
                serializedTransports = record.serializedTransports,
                serializedAuthenticatorExtensions = record.serializedAuthenticatorExtensions,
                serializedClientExtensions = record.serializedClientExtensions,
                counter = record.counter,
        )
    }

    fun updateCounter(counter: Long) {
        this.counter = counter
    }
}