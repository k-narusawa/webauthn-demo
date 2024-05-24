package com.knarusawa.webauthndemo.adapter.gateway.db.record

import com.knarusawa.webauthndemo.domain.credentials.Credential
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
        fun from(credential: Credential) = CredentialsRecord(
                credentialId = credential.credentialId,
                userId = credential.userId,
                serializedAttestedCredentialData = credential.serializedAttestedCredentialData,
                serializedEnvelope = credential.serializedEnvelope,
                serializedTransports = credential.serializedTransports,
                serializedAuthenticatorExtensions = credential.serializedAuthenticatorExtensions,
                serializedClientExtensions = credential.serializedClientExtensions,
                counter = credential.counter,
        )
    }
}