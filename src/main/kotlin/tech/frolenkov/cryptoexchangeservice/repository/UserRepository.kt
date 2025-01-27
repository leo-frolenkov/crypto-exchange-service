package tech.frolenkov.cryptoexchangeservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.frolenkov.cryptoexchangeservice.entity.user.User

interface UserRepository: JpaRepository<User, Long> {

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User?
}