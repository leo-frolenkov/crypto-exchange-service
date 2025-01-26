package tech.frolenkov.cryptoexchangeservice.service.user

import org.springframework.stereotype.Service
import tech.frolenkov.cryptoexchangeservice.repository.RoleRepository
import tech.frolenkov.cryptoexchangeservice.repository.UserRepository
import tech.frolenkov.cryptoexchangeservice.repository.UserToRoleRepository

@Service
class UserService(
    private val repository: UserRepository,
    private val roleRepository: RoleRepository,
    private val userToRoleRepository: UserToRoleRepository
) {
}