package tech.frolenkov.cryptoexchangeservice.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import tech.frolenkov.cryptoexchangeservice.api.http.request.AuthRequest
import tech.frolenkov.cryptoexchangeservice.entity.user.Role
import tech.frolenkov.cryptoexchangeservice.entity.user.Token
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import tech.frolenkov.cryptoexchangeservice.entity.user.UserToRole
import tech.frolenkov.cryptoexchangeservice.exception.ConflictException
import tech.frolenkov.cryptoexchangeservice.exception.NotFoundException
import tech.frolenkov.cryptoexchangeservice.repository.RoleRepository
import tech.frolenkov.cryptoexchangeservice.repository.UserRepository
import tech.frolenkov.cryptoexchangeservice.repository.UserToRoleRepository
import tech.frolenkov.cryptoexchangeservice.service.auth.AuthService
import tech.frolenkov.cryptoexchangeservice.service.auth.TokenProvider

@SpringBootTest
class AuthServiceTest {

    @MockitoBean
    private lateinit var repository: UserRepository

    @MockitoBean
    private lateinit var roleRepository: RoleRepository

    @MockitoBean
    private lateinit var userToRoleRepository: UserToRoleRepository

    @MockitoBean
    private lateinit var tokenProvider: TokenProvider

    @MockitoBean
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var authService: AuthService

    @Test
    fun `should successfully register user`() {
        val request = AuthRequest(username = "test", password = "password", roles = listOf(1L))
        val encodedPassword = "encodedPassword"
        val user = user()
        val role = role()

        `when`(repository.existsByUsername(request.username)).thenReturn(false)
        `when`(passwordEncoder.encode(request.password)).thenReturn(encodedPassword)
        `when`(repository.save(any())).thenReturn(user)
        `when`(roleRepository.findAllByIdIn(request.roles)).thenReturn(listOf(role))
        `when`(userToRoleRepository.save(userToRole())).thenReturn(userToRole())
        `when`(userToRoleRepository.findAllByUserId(user.id!!)).thenReturn(listOf(role.name))
        `when`(tokenProvider.getToken(user, listOf(role.name))).thenReturn(token())

        val result = authService.register(request)

        assertNotNull(result)
        assertEquals("token", result.accessToken)
        assertEquals("refreshToken", result.refreshToken)
        verify(repository).existsByUsername(request.username)
        verify(repository).save(any())
        verify(roleRepository).findAllByIdIn(request.roles)
    }

    @Test
    fun `should throw conflict exception`() {
        val request = AuthRequest(username = "testuser", password = "password", roles = listOf(1L))

        `when`(repository.existsByUsername(request.username)).thenReturn(true)

        val exception = assertThrows<ConflictException> {
            authService.register(request)
        }

        assertEquals("User already exists", exception.message)
        verify(repository).existsByUsername(request.username)
        verify(repository, never()).save(any())
    }

    @Test
    fun `should successfully login user`() {
        val request = AuthRequest(username = "testuser", password = "password")
        val user = user()

        `when`(repository.findByUsername(request.username)).thenReturn(user)
        `when`(tokenProvider.getToken(user, listOf())).thenReturn(token())

        val result = authService.login(request)

        assertNotNull(result)
        assertEquals("token", result.accessToken)
        assertEquals("refreshToken", result.refreshToken)
        verify(repository).findByUsername(request.username)
    }

    @Test
    fun `should throw not found exception`() {
        val request = AuthRequest(username = "nonexistent", password = "password")

        `when`(repository.findByUsername(request.username)).thenReturn(null)

        val exception = assertThrows<NotFoundException> {
            authService.login(request)
        }

        assertEquals("User not found", exception.message)
        verify(repository).findByUsername(request.username)
    }

    private fun token() =
        Token(
            accessToken = "token",
            refreshToken = "refreshToken",
        )

    private fun user() =
        User(
            id = 1L,
            username = "username",
            password = "password",
        )

    private fun role() =
        Role(
            id = 1L,
            name = "ROLE_USER",
            description = "User role",
        )

    private fun userToRole() =
        UserToRole(
            user = user(),
            role = role(),
        )
}

