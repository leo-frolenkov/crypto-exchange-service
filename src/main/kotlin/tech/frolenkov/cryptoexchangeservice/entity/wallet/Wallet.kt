package tech.frolenkov.cryptoexchangeservice.entity.wallet

import jakarta.persistence.*
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import java.math.BigDecimal

@Entity
@Table(name = "wallets")
data class Wallet(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_wallets")
    @SequenceGenerator(name = "s_wallets", sequenceName = "s_wallets", allocationSize = 1)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    val currency: Currency,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: WalletType,

    @Column(name = "balance")
    var balance: BigDecimal = BigDecimal.ZERO,
 )
