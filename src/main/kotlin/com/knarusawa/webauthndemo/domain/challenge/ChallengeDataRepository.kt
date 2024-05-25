package com.knarusawa.webauthndemo.domain.challenge

import org.springframework.stereotype.Repository

@Repository
class ChallengeDataRepository {
  private val challenges = mutableMapOf<String, ChallengeData>()
  fun save(challengeData: ChallengeData) {
    challenges[challengeData.challenge] = challengeData
  }

  fun findByChallenge(challenge: String): ChallengeData? {
    return challenges[challenge]
  }

  fun deleteByChallenge(challenge: String) {
    challenges.remove(challenge)
  }
}