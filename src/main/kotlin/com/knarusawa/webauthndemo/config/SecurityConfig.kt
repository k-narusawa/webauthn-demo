package com.knarusawa.webauthndemo.config

import com.knarusawa.webauthndemo.adapter.middleware.AuthenticationFailureHandler
import com.knarusawa.webauthndemo.adapter.middleware.AuthenticationFilter
import com.knarusawa.webauthndemo.adapter.middleware.AuthenticationSuccessHandler
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

    @Autowired
    private lateinit var authenticationSuccessHandler: AuthenticationSuccessHandler

    @Autowired
    private lateinit var authenticationFailureHandler: AuthenticationFailureHandler

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {
            it.disable()
        }
        http.csrf {
            it.disable()
        }
        http.authorizeHttpRequests {
            it.requestMatchers("/api/v1/login").permitAll()
            it.requestMatchers("/h2-console/**").permitAll()
            it.anyRequest().authenticated()
        }
        http.headers {
            it.frameOptions { it.disable() }
        }
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    fun authenticationFilter(): UsernamePasswordAuthenticationFilter {
        val filter = AuthenticationFilter(authenticationManager())
        filter.setRequiresAuthenticationRequestMatcher {
            it.method == "POST" && it.requestURI == "/api/v1/login"
        }
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler)
        filter.setAuthenticationFailureHandler(authenticationFailureHandler)
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