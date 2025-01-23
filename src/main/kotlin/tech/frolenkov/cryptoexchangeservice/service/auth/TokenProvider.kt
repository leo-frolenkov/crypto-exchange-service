package tech.frolenkov.cryptoexchangeservice.service.auth

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import tech.frolenkov.cryptoexchangeservice.config.SecurityProperties
import java.util.*

/**
 * Service for work with jwt-token
 */
@Component
class TokenProvider(
    val properties: SecurityProperties
) {
    fun createToken(username: String): String {
        val claims: Map<String, String> = mapOf("username" to username)
        val now = Date()
        val validity = Date(now.time + properties.expire * 60 * 1000)
        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(validity)
            .signWith(key(), Jwts.SIG.HS512 )
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .payload

        val username = claims.subject
        val authorities = claims["auth"] as Collection<Map<String, String>>
        val grantedAuthorities = authorities.map { SimpleGrantedAuthority(it["authority"]) }
        return UsernamePasswordAuthenticationToken(username, "", grantedAuthorities)
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val token = request.getHeader("Authorization")
        return if (token != null && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }

    fun validate(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .payload
            return true
        } catch (e: MalformedJwtException) {
            throw RuntimeException("JWT token is malformed.")
        } catch (e: ExpiredJwtException) {
            throw RuntimeException("JWT token is expired.")
        } catch (e: Exception) {
            throw RuntimeException("JWT token validation failed.")
        }
    }

    private fun key() = Keys.hmacShaKeyFor(properties.secret.toByteArray())

}