package com.knarusawa.webauthndemo.domain.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class LoginUserDetails(private val user: User) : UserDetails {
    val userId: String
        get() = user.userId.value()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return ArrayList()
    }

    override fun getPassword(): String {
        return user.password.value()
    }

    override fun getUsername(): String {
        return user.username.value()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return !user.isAccountLock
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return !user.isDisabled
    }
}