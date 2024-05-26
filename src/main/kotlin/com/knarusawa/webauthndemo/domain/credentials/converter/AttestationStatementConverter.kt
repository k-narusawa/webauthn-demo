package com.knarusawa.webauthndemo.domain.credentials.converter

import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.attestation.statement.AttestationStatement
import com.webauthn4j.util.Base64UrlUtil

class AttestationStatementConverter {

  companion object {
    private var cborConverter = ObjectConverter().cborConverter
  }

  fun convertToString(attribute: AttestationStatement): String {
    val container = AttestationStatementSerializationContainer(attribute)
    return Base64UrlUtil.encodeToString(cborConverter.writeValueAsBytes(container))
  }

  fun convertToEntityAttribute(rawData: String): AttestationStatement {
    val data = Base64UrlUtil.decode(rawData)
    val container = cborConverter.readValue(
      data, AttestationStatementSerializationContainer::class.java
    )
    return container?.attestationStatement
      ?: throw IllegalArgumentException("attestationStatement is not found")
  }
}