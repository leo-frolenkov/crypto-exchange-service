package tech.frolenkov.cryptoexchangeservice.service.user

import org.springframework.stereotype.Service
import tech.frolenkov.cryptoexchangeservice.entity.user.Role
import tech.frolenkov.cryptoexchangeservice.exception.NotFoundException
import tech.frolenkov.cryptoexchangeservice.repository.RoleRepository

@Service
class RoleService(
    private val repository: RoleRepository
) {

    fun createRole(role: Role): Role =
        repository.save(role)

    fun findByNameStartWith(roleName: String?): List<Role> =
        roleName?.let { repository.findByNameStartsWithIgnoreCase(it) } ?: repository.findAll()

    fun findById(id: Long): Role =
        repository.findById(id).orElseThrow { NotFoundException("Role with id $id not found") }

}