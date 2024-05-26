package com.knarusawa.webauthndemo.domain.credentials.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.webauthn4j.converter.jackson.deserializer.cbor.AuthenticationExtensionsAuthenticatorOutputsEnvelope
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.extension.authenticator.AuthenticationExtensionsAuthenticatorOutputs
import com.webauthn4j.data.extension.authenticator.ExtensionAuthenticatorOutput
import java.io.ByteArrayInputStream
import java.nio.Buffer
import java.nio.ByteBuffer

class AuthenticatorExtensionsConverter {
    companion object {
        private val cborConverter = ObjectConverter().cborConverter
    }

    fun <T : ExtensionAuthenticatorOutput?> convertToExtensions(byteArray: ByteArray): AuthenticationExtensionsAuthenticatorOutputs<T>? {
        val byteBuffer = ByteBuffer.wrap(byteArray)
        if (byteBuffer.remaining() == 0) {
            return AuthenticationExtensionsAuthenticatorOutputs()
        }
        val remaining = ByteArray(byteBuffer.remaining())
        byteBuffer[remaining]
        val byteArrayInputStream = ByteArrayInputStream(remaining)
        val envelope =
                cborConverter.readValue<AuthenticationExtensionsAuthenticatorOutputsEnvelope<T>>(
                        byteArrayInputStream,
                        object : TypeReference<AuthenticationExtensionsAuthenticatorOutputsEnvelope<T>?>() {
                        })
        if (envelope == null) {
            return null
        }
        val leftoverLength = remaining.size - envelope.length
        //This cast is necessary to be complied with JDK 17 when targeting JDK 8
        (byteBuffer as Buffer).position((byteBuffer as Buffer).position() - leftoverLength)
        return envelope.authenticationExtensionsAuthenticatorOutputs
    }
}