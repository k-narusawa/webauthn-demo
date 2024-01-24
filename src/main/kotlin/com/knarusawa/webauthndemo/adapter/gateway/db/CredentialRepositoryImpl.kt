package com.knarusawa.webauthndemo.adapter.gateway.db

import com.yubico.webauthn.CredentialRepository
import com.yubico.webauthn.RegisteredCredential
import com.yubico.webauthn.data.ByteArray
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CredentialRepositoryImpl : CredentialRepository {
    override fun getCredentialIdsForUsername(username: String?): MutableSet<PublicKeyCredentialDescriptor> {
        TODO("Not yet implemented")
    }

    override fun getUserHandleForUsername(username: String?): Optional<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun getUsernameForUserHandle(userHandle: ByteArray?): Optional<String> {
        TODO("Not yet implemented")
    }

    override fun lookup(credentialId: ByteArray?, userHandle: ByteArray?): Optional<RegisteredCredential> {
        TODO("Not yet implemented")
    }

    override fun lookupAll(credentialId: ByteArray?): MutableSet<RegisteredCredential> {
        TODO("Not yet implemented")
    }
}