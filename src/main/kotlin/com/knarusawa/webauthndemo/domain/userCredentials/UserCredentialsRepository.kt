package com.knarusawa.webauthndemo.domain.userCredentials

import com.knarusawa.webauthndemo.domain.user.UserId

interface UserCredentialsRepository {
    fun save(userCredentials: UserCredentials)
    fun findByUserId(userId: UserId): List<UserCredentials>
}