package com.knarusawa.webauthndemo.domain.registrationChallenge

import com.knarusawa.webauthndemo.domain.user.UserId

interface RegistrationChallengeRepository {
    fun save(challenge: RegistrationChallenge)
    fun findByUserId(userId: UserId): RegistrationChallenge?
}