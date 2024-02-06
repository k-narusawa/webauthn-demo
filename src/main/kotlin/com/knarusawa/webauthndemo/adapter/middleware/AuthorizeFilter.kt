package com.knarusawa.webauthndemo.adapter.middleware

import com.knarusawa.webauthndemo.domain.user.LoginUserDetails
import com.knarusawa.webauthndemo.util.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthorizeFilter : OncePerRequestFilter() {
    private val log = logger()

    val matchers = listOf(
            AntPathRequestMatcher("/api/v1/login"),
            AntPathRequestMatcher("/api/v1/webauthn/login/request"),
            AntPathRequestMatcher("/api/v1/webauthn/login")
    )
    val combinedMatcher = OrRequestMatcher(matchers)

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        if (!combinedMatcher.matches(request)) {
            val user = request.session.getAttribute("user") as? LoginUserDetails

            if (user == null) {
                log.info("Request is Unauthorized")
                log.info("METHOD: [${request.method}], URL: [${request.requestURL}]")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                return
            }

            log.info("user_id: ${user.userId}")
            SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(user, null, ArrayList())
        }

        filterChain.doFilter(request, response)
    }
}