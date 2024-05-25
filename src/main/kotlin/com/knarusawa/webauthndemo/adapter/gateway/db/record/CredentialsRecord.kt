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

  @Column(name = "aaguid")
  val aaguid: String,

  @Column(name = "label")
  val label: String,

  @Column(name = "attested_credential_data")
  val attestedCredentialData: String,

  @Column(name = "attestation_statement_format")
  val attestationStatementFormat: String,

  @Column(name = "transports")
  val transports: String,

  @Column(name = "authenticator_extensions")
  val authenticatorExtensions: String,

  @Column(name = "client_extensions")
  val clientExtensions: String,

  @Column(name = "counter")
  val counter: Long,
) {
  companion object {
    fun from(credential: Credential) = CredentialsRecord(
      credentialId = credential.credentialId,
      userId = credential.userId,
      aaguid = credential.aaguid,
      label = credential.label,
      attestedCredentialData = credential.attestedCredentialData,
      attestationStatementFormat = credential.attestationStatementFormat,
      transports = credential.transports,
      authenticatorExtensions = credential.authenticatorExtensions,
      clientExtensions = credential.clientExtensions,
      counter = credential.counter,
    )
  }
}