package com.knarusawa.webauthndemo.adapter.controller.dto

data class ApiV1UsersCredentialsGet(
  val keys: List<Key>
) {
  data class Key(
    val userId: String,
    val credentialId: String,
    val aaguid: String,
    val label: String
  )
}
