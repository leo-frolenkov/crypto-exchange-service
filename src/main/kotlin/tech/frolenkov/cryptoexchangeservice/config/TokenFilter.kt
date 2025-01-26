package tech.frolenkov.cryptoexchangeservice.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tech.frolenkov.cryptoexchangeservice.service.auth.TokenProvider

@Component
class TokenFilter(
    private val service: TokenProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = service.resolveToken(request)

        if (token != null) {
            val username = service.getUsernameFromToken(token) ?: throw BadCredentialsException("You are not authorized")
            if (service.validateToken(token, username)) {
                val auth = service.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
        }

        filterChain.doFilter(request, response)
    }

}