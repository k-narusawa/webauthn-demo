package com.knarusawa.webauthndemo.config

import com.knarusawa.webauthndemo.adapter.middleware.AuthenticationFailureHandler
import com.knarusawa.webauthndemo.adapter.middleware.AuthenticationFilter
import com.knarusawa.webauthndemo.adapter.middleware.AuthenticationSuccessHandler
import com.knarusawa.webauthndemo.adapter.middleware.AuthorizeFilter
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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig {
    @Autowired
    private lateinit var authenticationConfiguration: AuthenticationConfiguration

    @Autowired
    private lateinit var authenticationSuccessHandler: AuthenticationSuccessHandler

    @Autowired
    private lateinit var authenticationFailureHandler: AuthenticationFailureHandler

    @Autowired
    private lateinit var authorizeFilter: AuthorizeFilter

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors {
            it.configurationSource(corsConfigurationSource())
        }
        http.csrf {
            // デモなのでcsrfは無効化
            // 実際に使う場合はcsrf用のエンドポイントを作成
            it.disable()
        }
        http.authorizeHttpRequests {
            it.requestMatchers("/v1/login").permitAll()
            it.requestMatchers("/v1/webauthn/authentication/options").permitAll()
            it.requestMatchers("/v1/logout").permitAll()
            it.requestMatchers("/h2-console/**").permitAll()
            it.anyRequest().authenticated()
        }
        http.headers {
            it.frameOptions { it.disable() }
        }
        http.addFilterBefore(authorizeFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    fun authenticationFilter(): UsernamePasswordAuthenticationFilter {
        val filter = AuthenticationFilter(authenticationManager())
        filter.setRequiresAuthenticationRequestMatcher {
            (it.method == "POST" && it.requestURI == "/v1/login") or
                    (it.method == "POST" && it.requestURI == "/v1/webauthn/authentication")
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

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.addAllowedMethod(CorsConfiguration.ALL)
        config.addAllowedHeader(CorsConfiguration.ALL)
        config.allowCredentials = true

        config.addAllowedOrigin("http://127.0.0.1:3000")
        config.addAllowedOrigin("http://localhost:3000")

        val corsSource = UrlBasedCorsConfigurationSource()
        corsSource.registerCorsConfiguration("/**", config)
        return corsSource
    }
}