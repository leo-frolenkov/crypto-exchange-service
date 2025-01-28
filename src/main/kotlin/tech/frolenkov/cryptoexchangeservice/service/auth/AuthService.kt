package tech.frolenkov.cryptoexchangeservice.service.auth

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tech.frolenkov.cryptoexchangeservice.api.http.request.AuthRequest
import tech.frolenkov.cryptoexchangeservice.entity.user.Token
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import tech.frolenkov.cryptoexchangeservice.entity.user.UserToRole
import tech.frolenkov.cryptoexchangeservice.exception.ConflictException
import tech.frolenkov.cryptoexchangeservice.exception.NotFoundException
import tech.frolenkov.cryptoexchangeservice.repository.RoleRepository
import tech.frolenkov.cryptoexchangeservice.repository.UserRepository
import tech.frolenkov.cryptoexchangeservice.repository.UserToRoleRepository

/**
 * Service for authentication and authorization
 */
@Service
class AuthService(
    private val repository: UserRepository,
    private val roleRepository: RoleRepository,
    private val userToRoleRepository: UserToRoleRepository,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    /**
     * register new user
     * and get token
     * @param request request register data
     * @exception ConflictException already exists
     */
    fun register(request: AuthRequest): Token {
        log.debug("Register new user: ${request.username}")
        val existsUser = repository.existsByUsername(request.username)

        if (existsUser) {
            throw ConflictException("User already exists")
        }

        val user = repository.save(
            User(
                username = request.username,
                password = passwordEncoder.encode(request.password),
            )
        )

        val roles = roles(user, request.roles)

        return tokenProvider.getToken(user, roles)
    }

    /**
     * login user
     * @param request login data
     */
    fun login(request: AuthRequest): Token {
        log.debug("Login user: ${request.username}")
        val user = repository.findByUsername(request.username)
            ?: throw NotFoundException("User not found")

        val roles = roles(user)
        return tokenProvider.getToken(user, roles)
    }

    private fun roles(user: User, roleIds: List<Long> = listOf()): List<String> {
        if (roleIds.isNotEmpty()) {
            roleRepository.findAllByIdIn(roleIds)
                .forEach {
                    userToRoleRepository.save(UserToRole(user = user, role = it))
                }
        }

        return user.id?.let { userToRoleRepository.findAllByUserId(it) } ?: emptyList()
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}