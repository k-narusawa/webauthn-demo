package com.knarusawa.webauthndemo.adapter.gateway.db.dao

import com.knarusawa.webauthndemo.adapter.gateway.db.record.CredentialsRecord
import org.springframework.data.repository.CrudRepository

interface CredentialsDao : CrudRepository<CredentialsRecord, String>