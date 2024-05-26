package com.knarusawa.webauthndemo.domain.credentials

import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.util.Base64UrlUtil

class Credential private constructor(
  val credentialId: String,
  val userId: String,
  val aaguid: AAGUID,
  label: String,
  val attestedCredentialData: String,
  val attestationStatementFormat: String,
  val transports: String,
  val authenticatorExtensions: String,
  val clientExtensions: String,
  counter: Long,
) {
  var label: String = label
    private set
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

      val serializedAttestedCredentialData =
        attestedCredentialDataConverter.convert(authenticator.attestedCredentialData)

      val rawAAGUID = authenticator.attestedCredentialData.aaguid.value.toString()
      val aaguid = AAGUID.fromAAGUID(rawAAGUID)

      return Credential(
        credentialId = Base64UrlUtil.encodeToString(credentialId),
        userId = userId,
        aaguid = aaguid,
        label = aaguid?.labael ?: "Unknown",
        attestedCredentialData = Base64UrlUtil.encodeToString(serializedAttestedCredentialData),
        attestationStatementFormat = authenticator.attestationStatement!!.format,
        transports = authenticator.transports?.let {
          objectConverter.jsonConverter.writeValueAsString(
            it
          )
        } ?: "[]",
        authenticatorExtensions = Base64UrlUtil.encodeToString(
          objectConverter.cborConverter.writeValueAsBytes(
            authenticator.authenticatorExtensions
          )
        ),
        clientExtensions = objectConverter.jsonConverter.writeValueAsString(
          authenticator.clientExtensions
        ),
        counter = authenticator.counter,
      )
    }

    fun from(record: CredentialsRecord) = Credential(
      credentialId = record.credentialId,
      userId = record.userId,
      aaguid = AAGUID.fromAAGUID(record.aaguid),
      label = record.label,
      attestedCredentialData = record.attestedCredentialData,
      attestationStatementFormat = record.attestationStatementFormat,
      transports = record.transports,
      authenticatorExtensions = record.authenticatorExtensions,
      clientExtensions = record.clientExtensions,
      counter = record.counter,
    )
  }

  fun changeLabel(label: String) {
    this.label = label
  }

  fun updateCounter(counter: Long) {
    this.counter = counter
  }

  override fun toString(): String {
    return """
      Credential(
      credentialId='$credentialId', 
      userId='$userId', 
      aaguid='$aaguid',
      label='$label',
      attestedCredentialData='$attestedCredentialData',
      attestationStatementFormat='$attestationStatementFormat',
      transports=$transports, 
      authenticatorExtensions='$authenticatorExtensions', 
      clientExtensions='$clientExtensions', 
      counter=$counter
    )
    """.trimIndent()
  }
}