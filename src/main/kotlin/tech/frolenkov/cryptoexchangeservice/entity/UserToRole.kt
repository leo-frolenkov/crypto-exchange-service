package tech.frolenkov.cryptoexchangeservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_to_roles")
data class UserToRole(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_user_to_roles")
    @SequenceGenerator(name = "s_user_to_roles", sequenceName = "s_user_to_roles", allocationSize = 1)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    val role: Role
)