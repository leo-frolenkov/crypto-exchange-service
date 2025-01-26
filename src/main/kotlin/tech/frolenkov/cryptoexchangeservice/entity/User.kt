package tech.frolenkov.cryptoexchangeservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_users")
    @SequenceGenerator(name = "s_users", sequenceName = "s_users", allocationSize = 1)
    @Column(name = "id", nullable = false)
    val id: Long? = null,
    @Column(name = "username", nullable = false, length = 256)
    val username: String,
    @Column(name = "password", nullable = false)
    val password: String,
)