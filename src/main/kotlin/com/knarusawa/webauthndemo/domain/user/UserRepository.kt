package com.knarusawa.webauthndemo.domain.user

interface UserRepository {
  fun save(user: User)
  fun findByUsername(username: Username): User?
  fun findByUserId(userId: UserId): User?
}