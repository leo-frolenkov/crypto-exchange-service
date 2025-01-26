package tech.frolenkov.cryptoexchangeservice.service.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import tech.frolenkov.cryptoexchangeservice.config.SecurityProperties
import tech.frolenkov.cryptoexchangeservice.entity.Token
import tech.frolenkov.cryptoexchangeservice.entity.User
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import javax.crypto.spec.SecretKeySpec

/**
 * Service for work with jwt-token
 */
@Component
class TokenProvider(
    private val properties: SecurityProperties,
) {

    fun getToken(user: User, roles: List<String> = emptyList()): Token {
        return Token(
            accessToken = createToken(user, roles),
            refreshToken = createRefreshToken(user, roles),
        )
    }

    private fun createToken(user: User, roles: List<String> = emptyList()): String {
        val minutes = properties.expire * 60 * 1000

        val jwtBuilder = Jwts.builder()
            .subject(user.username)
            .issuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
            .claim("id", user.id)
            .claim("username", user.username)
            .claim("roles", roles.joinToString(separator = ","))

        return jwtBuilder
            .expiration(Date.from(LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(key())
            .compact()
    }

    private fun createRefreshToken(user: User, roles: List<String> = emptyList()): String {
        val minutes = properties.expire * 60 * 1000

        val jwtBuilder = Jwts.builder()
            .subject(user.username)
            .issuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
            .claim("username", user.username)
            .claim("roles", roles.joinToString(separator = ","))

        return jwtBuilder
            .expiration(Date.from(LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(key())
            .compact()
    }

    fun getUsernameFromToken(token: String): String? {
        return getAllClaimsFromToken(token)["username"] as String?
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .payload

        val username = claims.subject
        val authorities = claims.getOrDefault("roles", "").toString().split(",")
        val grantedAuthorities = authorities.map { SimpleGrantedAuthority(it) }
        return UsernamePasswordAuthenticationToken(username, "", grantedAuthorities)
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val token = request.getHeader("Authorization")
        return if (token != null && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }

    fun validateToken(tokenValue: String, username: String): Boolean {
        val claims = getAllClaimsFromToken(tokenValue)
        return claims["username"] == username && !isExpired(tokenValue)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return try {
            Jwts.parser()
                .setSigningKey(properties.secret.toByteArray())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .body
        } catch(e: ExpiredJwtException) {
            throw e
        }
    }

    private fun isExpired(token: String) =
        getAllClaimsFromToken(token).expiration.before(Date())

    private fun key() =
        SecretKeySpec(properties.secret.toByteArray(), "HmacSHA256")
}