package com.knarusawa.webauthndemo.domain.credentials.converter

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.webauthn4j.data.attestation.statement.AttestationStatement
import java.util.*

class SerializedAttestationStatement @JsonCreator constructor(
        @field:JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "fmt"
        ) @field:JsonProperty("attStmt") @param:JsonProperty(
                "attStmt"
        ) val attestationStatement: AttestationStatement
) {
    @get:JsonProperty("fmt")
    val format: String
        get() = attestationStatement.format

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as SerializedAttestationStatement
        return attestationStatement == that.attestationStatement
    }

    override fun hashCode(): Int {
        return Objects.hash(attestationStatement)
    }
}