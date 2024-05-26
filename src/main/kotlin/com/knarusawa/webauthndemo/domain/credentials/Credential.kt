package com.knarusawa.webauthndemo.domain.credentials

import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import com.knarusawa.webauthndemo.domain.credentials.converter.AttestationStatementConverter
import com.knarusawa.webauthndemo.domain.credentials.converter.AuthenticatorExtensionsConverter
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.AuthenticationExtensionsClientOutputsConverter
import com.webauthn4j.converter.AuthenticatorTransportConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.AuthenticatorTransport
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData
import com.webauthn4j.data.attestation.statement.AttestationStatement
import com.webauthn4j.data.extension.authenticator.AuthenticationExtensionsAuthenticatorOutputs
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientOutputs
import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput
import com.webauthn4j.util.Base64UrlUtil

class Credential private constructor(
        val credentialId: String,
        val userId: String,
        val aaguid: AAGUID,
        label: String,
        val attestedCredentialData: AttestedCredentialData,
        val attestationStatement: AttestationStatement,
        val attestationStatementFormat: String,
        val transports: Set<AuthenticatorTransport>,
        val authenticatorExtensions: AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>?,
        val clientExtensions: AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput>?,
        counter: Long,
) {
    var label: String = label
        private set
    var counter: Long = counter
        private set

    companion object {
        private val objectConverter = ObjectConverter()
        private val attestedCredentialDataConverter = AttestedCredentialDataConverter(objectConverter)
        private val transportConverter = AuthenticatorTransportConverter()
        private val clientOutputsConverter =
                AuthenticationExtensionsClientOutputsConverter(objectConverter)

        fun of(
                credentialId: ByteArray?,
                userId: String,
                authenticator: AuthenticatorImpl,
        ): Credential {
            val rawAAGUID = authenticator.attestedCredentialData.aaguid.value.toString()
            val aaguid = AAGUID.fromAAGUID(rawAAGUID)

            return Credential(
                    credentialId = Base64UrlUtil.encodeToString(credentialId),
                    userId = userId,
                    aaguid = aaguid,
                    label = aaguid.labael,
                    attestedCredentialData = authenticator.attestedCredentialData,
                    attestationStatement = authenticator.attestationStatement
                            ?: throw IllegalArgumentException("AttestationStatement is not found"),
                    attestationStatementFormat = authenticator.attestationStatement!!.format,
                    transports = authenticator.transports ?: emptySet(),
                    authenticatorExtensions = authenticator.authenticatorExtensions,
                    clientExtensions = authenticator.clientExtensions,
                    counter = authenticator.counter,
            )
        }

        fun from(record: CredentialsRecord) = Credential(
                credentialId = record.credentialId,
                userId = record.userId,
                aaguid = AAGUID.fromAAGUID(record.aaguid),
                label = record.label,
                attestedCredentialData = attestedCredentialDataConverter.convert(
                        Base64UrlUtil.decode(record.attestedCredentialData)
                ),
                attestationStatement = AttestationStatementConverter().convertToEntityAttribute(record.attestationStatement),
                attestationStatementFormat = record.attestationStatementFormat,
                transports = record.transports.split(",").map {
                    transportConverter.convert(it)
                }.toSet(),
                authenticatorExtensions = AuthenticatorExtensionsConverter().convertToExtensions(
                        Base64UrlUtil.decode(record.authenticatorExtensions)
                ),
                clientExtensions = record.clientExtensions?.let {
                    clientOutputsConverter.convert(record.clientExtensions)
                },
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
        return """Credential(
      credentialId='$credentialId', 
      userId='$userId', 
      aaguid='$aaguid',
      label='$label',
      attestedCredentialData='$attestedCredentialData',
      attestationStatement='$attestationStatement',
      attestationStatementFormat='$attestationStatementFormat',
      transports=$transports, 
      authenticatorExtensions='$authenticatorExtensions', 
      clientExtensions='$clientExtensions', 
      counter=$counter
    )
    """.trimIndent()
    }
}