package tech.frolenkov.cryptoexchangeservice.entity.user

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_roles")
    @SequenceGenerator(name = "s_roles", sequenceName = "s_roles", allocationSize = 1)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "name", nullable = false, unique = true, length = 256)
    val name: String,

    @Column(name = "description", length = 1000)
    val description: String?,
)