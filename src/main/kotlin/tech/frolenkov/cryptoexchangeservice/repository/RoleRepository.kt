package tech.frolenkov.cryptoexchangeservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.frolenkov.cryptoexchangeservice.entity.user.Role

interface RoleRepository: JpaRepository<Role, Long> {
    fun findByNameStartsWithIgnoreCase(name: String): List<Role>

    fun findAllByIdIn(ids: Collection<Long>): List<Role>
}