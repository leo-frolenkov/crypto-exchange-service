package tech.frolenkov.cryptoexchangeservice.entity.orders

import jakarta.persistence.*
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_orders")
    @SequenceGenerator(name = "s_orders", sequenceName = "s_orders", allocationSize = 1)
    @Column(name = "id", nullable = false)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @Column(name = "amount")
    val amount: BigDecimal,
    @Column(name = "price")
    val price: BigDecimal,
    @Enumerated(EnumType.STRING)
    val currency: Currency,
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: OrderStatus = OrderStatus.PENDING,
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: OrderType,
    @Enumerated(EnumType.STRING)
    @Column(name = "order_kind")
    val orderKind: OrderKind,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)