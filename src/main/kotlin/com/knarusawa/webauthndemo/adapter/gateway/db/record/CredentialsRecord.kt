package com.knarusawa.webauthndemo.adapter.gateway.db.record

import com.knarusawa.webauthndemo.domain.credentials.Credentials
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "credentials")
data class CredentialsRecord(
        @Id
        @Column(name = "credential_id")
        val credentialId: String,

        @Column(name = "user_id")
        val userId: String,

        @Column(name = "serialized_attested_credential_data")
        val serializedAttestedCredentialData: String,

        @Column(name = "serialized_envelope")
        val serializedEnvelope: String,

        @Column(name = "serialized_transports")
        val serializedTransports: String,

        @Column(name = "serialized_authenticator_extensions")
        val serializedAuthenticatorExtensions: String,

        @Column(name = "serialized_client_extensions")
        val serializedClientExtensions: String,

        @Column(name = "counter")
        val counter: Long,
) {
    companion object {
        fun from(credentials: Credentials) = CredentialsRecord(
                credentialId = credentials.credentialId,
                userId = credentials.userId,
                serializedAttestedCredentialData = credentials.serializedAttestedCredentialData,
                serializedEnvelope = credentials.serializedEnvelope,
                serializedTransports = credentials.serializedTransports,
                serializedAuthenticatorExtensions = credentials.serializedAuthenticatorExtensions,
                serializedClientExtensions = credentials.serializedClientExtensions,
                counter = credentials.counter,
        )
    }
}