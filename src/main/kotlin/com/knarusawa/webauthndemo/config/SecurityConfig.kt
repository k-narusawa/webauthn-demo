package com.knarusawa.webauthndemo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {
    @Autowired
    private lateinit var authenticationConfiguration: AuthenticationConfiguration

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {
            it.disable()
        }
        http.csrf {
            it.disable()
        }
        http.authorizeHttpRequests {
            it.requestMatchers("/login").permitAll()
            it.requestMatchers("/h2-console/**").permitAll()
            it.anyRequest().authenticated()
        }
        http.headers {
            it.frameOptions { it.disable() }
        }
        return http.build()
    }

    @Bean
    fun authenticationFilter(): UsernamePasswordAuthenticationFilter {
        val filter = UsernamePasswordAuthenticationFilter()
        filter.setAuthenticationManager(authenticationManager())
        return filter
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}