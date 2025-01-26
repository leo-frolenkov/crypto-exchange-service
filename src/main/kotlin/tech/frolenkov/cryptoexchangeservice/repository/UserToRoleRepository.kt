package tech.frolenkov.cryptoexchangeservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import tech.frolenkov.cryptoexchangeservice.entity.UserToRole

interface UserToRoleRepository: JpaRepository<UserToRole, Long> {
    @Query(value = """
        select name
        from roles r
        left join user_to_roles utr on r.id = utr.role_id
        where utr.user_id = :userId
    """, nativeQuery = true)
    fun findAllByUserId(@Param("userId")userId: Long): List<String>
}