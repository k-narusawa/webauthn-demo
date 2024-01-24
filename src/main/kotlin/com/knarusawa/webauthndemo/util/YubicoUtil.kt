package com.knarusawa.webauthndemo.util

import com.yubico.webauthn.data.ByteArray
import java.nio.ByteBuffer
import java.util.*


object YubicoUtil {
    fun toByteArray(uuid: UUID): ByteArray {
        val buffer = ByteBuffer.wrap(ByteArray(16))
        buffer.putLong(uuid.mostSignificantBits)
        buffer.putLong(uuid.leastSignificantBits)
        return ByteArray(buffer.array())
    }

    fun toUUID(byteArray: ByteArray): UUID {
        val byteBuffer = ByteBuffer.wrap(byteArray.bytes)
        val high = byteBuffer.getLong()
        val low = byteBuffer.getLong()
        return UUID(high, low)
    }
}