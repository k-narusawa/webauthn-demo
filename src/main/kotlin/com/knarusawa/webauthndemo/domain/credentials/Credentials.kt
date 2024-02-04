package com.knarusawa.webauthndemo.domain.credentials

import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.util.ObjectConverter
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
            authenticator: AuthenticatorImpl,
            credentialId: ByteArray?
        ): Credentials {
            val objectConverter = ObjectConverter()
            val attestedCredentialDataConverter = AttestedCredentialDataConverter(objectConverter)
            val attestationStatementEnvelope =
                AttestationStatementEnvelope(authenticator.attestationStatement!!)
            val serializedEnvelope =
                objectConverter.cborConverter.writeValueAsBytes(attestationStatementEnvelope);

            val serializedAttestedCredentialData =
                attestedCredentialDataConverter.convert(authenticator.attestedCredentialData)

            return Credentials(
                credentialId = Base64UrlUtil.encodeToString(credentialId),
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