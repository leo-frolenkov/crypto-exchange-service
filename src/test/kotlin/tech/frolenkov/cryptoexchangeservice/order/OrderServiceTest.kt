package tech.frolenkov.cryptoexchangeservice.order

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import tech.frolenkov.cryptoexchangeservice.UserContext
import tech.frolenkov.cryptoexchangeservice.api.http.request.OrderRequest
import tech.frolenkov.cryptoexchangeservice.entity.orders.Order
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderKind
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderStatus
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderType
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Wallet
import tech.frolenkov.cryptoexchangeservice.entity.wallet.WalletType
import tech.frolenkov.cryptoexchangeservice.repository.OrderRepository
import tech.frolenkov.cryptoexchangeservice.service.order.OrderService
import tech.frolenkov.cryptoexchangeservice.service.wallet.WalletService
import java.math.BigDecimal

@SpringBootTest
@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @MockitoBean
    private lateinit var orderRepository: OrderRepository

    @MockitoBean
    private lateinit var walletService: WalletService

    @MockitoBean
    private lateinit var userContext: UserContext

    @Autowired
    private lateinit var orderService: OrderService

    @BeforeEach
    fun setUp() {
        val user = User(id = 1L, username = "testuser", password = "password")
        `when`(userContext.getCurrentUser()).thenReturn(user)
    }

    @Test
    fun `should create a buy order and match`() {
        val user = User(id = 1L, username = "testuser", password = "password")
        val orderRequest = OrderRequest(
            amount = BigDecimal.valueOf(1),
            price = BigDecimal.valueOf(50000),
            currency = Currency.BTC,
            type = OrderType.BUY,
            orderKind = OrderKind.LIMIT
        )

        val existingOrder = Order(
            id = 2L,
            user = user,
            amount = BigDecimal.valueOf(1),
            price = BigDecimal.valueOf(49000),
            currency = Currency.BTC,
            type = OrderType.SELL,
            orderKind = OrderKind.LIMIT
        )

        `when`(orderRepository.findByCurrencyAndTypeAndStatus(Currency.BTC, OrderType.SELL, OrderStatus.PENDING))
            .thenReturn(listOf(existingOrder))

        `when`(orderRepository.save(any())).thenAnswer { it.arguments[0] }

        orderService.createOrder(orderRequest)

        verify(orderRepository, times(3)).save(any())
    }

    @Test
    fun `should create a sell order and not match`() {
        val user = User(id = 1L, username = "testuser", password = "password")
        val orderRequest = OrderRequest(
            amount = BigDecimal.valueOf(1),
            price = BigDecimal.valueOf(50000),
            currency = Currency.BTC,
            type = OrderType.SELL,
            orderKind = OrderKind.LIMIT
        )

        // Мокирование кошелька пользователя
        val wallet = Wallet(
            id = 1L,
            user = user,
            currency = Currency.BTC,
            balance = BigDecimal.valueOf(2),
            type = WalletType.CRYPTO
        )
        `when`(walletService.findWalletByUser(Currency.BTC)).thenReturn(wallet)

        `when`(orderRepository.findByCurrencyAndTypeAndStatus(Currency.BTC, OrderType.BUY, OrderStatus.PENDING))
            .thenReturn(emptyList())

        `when`(orderRepository.save(any())).thenAnswer { it.arguments[0] }

        orderService.createOrder(orderRequest)

        verify(orderRepository, times(1)).save(any())
    }
}