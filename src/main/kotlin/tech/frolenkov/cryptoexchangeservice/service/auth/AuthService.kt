package tech.frolenkov.cryptoexchangeservice.service.auth

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tech.frolenkov.cryptoexchangeservice.api.http.request.AuthRequest
import tech.frolenkov.cryptoexchangeservice.api.http.response.TokenResponse
import tech.frolenkov.cryptoexchangeservice.entity.User
import tech.frolenkov.cryptoexchangeservice.exception.ConflictException
import tech.frolenkov.cryptoexchangeservice.repository.UserRepository

/**
 * Service for authentication and authorization
 */
@Service
class AuthService(
    private val repository: UserRepository,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    /**
     * register new user
     * and get token
     * @param request request register data
     * @exception ConflictException already exists
     */
    fun register(request: AuthRequest): TokenResponse {
        log.debug("Register new user: ${request.username}")
        val existsUser = repository.existsByUsername(request.username)

        if (existsUser) {
            throw ConflictException("User already exists")
        }

        repository.save(
            User(
                id = 0,
                username = request.username,
                password = passwordEncoder.encode(request.password)
            )
        )

        val token = tokenProvider.createToken(request.username)
        return TokenResponse(token)
    }

    /**
     * login user
     * @param request login data
     */
    fun login(request: AuthRequest): TokenResponse {
        log.debug("Login user: ${request.username}")

        val token = tokenProvider.createToken(request.username)
        return TokenResponse(token)
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}