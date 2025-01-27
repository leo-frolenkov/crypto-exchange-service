package tech.frolenkov.cryptoexchangeservice

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import tech.frolenkov.cryptoexchangeservice.exception.NotFoundException
import tech.frolenkov.cryptoexchangeservice.repository.UserRepository

@Service
class UserContext(
    private val repository: UserRepository
) {
    fun getCurrentUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        val username = auth.name
        return repository.findByUsername(username) ?: throw NotFoundException("User $username not found")
    }
}