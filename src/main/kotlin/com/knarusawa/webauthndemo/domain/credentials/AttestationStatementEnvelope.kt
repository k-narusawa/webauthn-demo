package com.knarusawa.webauthndemo.domain.credentials

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.webauthn4j.data.attestation.statement.AttestationStatement
import java.io.Serializable


internal class AttestationStatementEnvelope @JsonCreator constructor(
  @field:JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
    property = "fmt"
  ) @field:JsonProperty("attStmt") @param:JsonProperty(
    "attStmt"
  ) val attestationStatement: AttestationStatement
) : Serializable {

  @get:JsonProperty("fmt")
  val format: String
    get() = attestationStatement.format
}