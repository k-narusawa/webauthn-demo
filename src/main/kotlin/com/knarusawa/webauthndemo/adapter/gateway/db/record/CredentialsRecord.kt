package com.knarusawa.webauthndemo.adapter.gateway.db.record

import com.knarusawa.webauthndemo.domain.credentials.AttestationStatementConverter
import com.knarusawa.webauthndemo.domain.credentials.Credential
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.AuthenticatorTransportConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.util.Base64UrlUtil
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

  @Column(name = "attestation_statement")
  val attestationStatement: String,

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
    private val objectConverter = ObjectConverter()
    private val attestedCredentialDataConverter = AttestedCredentialDataConverter(objectConverter)
    fun from(credential: Credential) = CredentialsRecord(
      credentialId = credential.credentialId,
      userId = credential.userId,
      aaguid = credential.aaguid.aaguid,
      label = credential.label,
      attestedCredentialData = Base64UrlUtil.encodeToString(
        attestedCredentialDataConverter.convert(
          credential.attestedCredentialData
        )
      ),
      attestationStatement = AttestationStatementConverter().convertToString(credential.attestationStatement),
      attestationStatementFormat = credential.attestationStatementFormat,
      transports = credential.transports.joinToString(",") {
        AuthenticatorTransportConverter().convertToString(it)
      },
      authenticatorExtensions = credential.authenticatorExtensions,
      clientExtensions = credential.clientExtensions,
      counter = credential.counter,
    )
  }
}