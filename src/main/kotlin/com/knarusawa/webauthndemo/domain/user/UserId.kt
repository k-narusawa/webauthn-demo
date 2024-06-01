package com.knarusawa.webauthndemo.domain.user

import java.util.*

data class UserId private constructor(
  private val value: UUID
) {
  companion object {
    fun of() = UserId(value = UUID.randomUUID())
    fun from(recordString: String): UserId {
      // 失敗したらIllegalArgumentExceptionが投げられる
      return UserId(UUID.fromString(recordString))
    }
  }

  fun value() = this.value.toString()

  override fun toString(): String {
    return this.value()
  }
}
