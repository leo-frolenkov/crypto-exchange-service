package tech.frolenkov.cryptoexchangeservice.service.order

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tech.frolenkov.cryptoexchangeservice.UserContext
import tech.frolenkov.cryptoexchangeservice.api.http.request.OrderRequest
import tech.frolenkov.cryptoexchangeservice.entity.orders.Order
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderKind
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderStatus
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderType
import tech.frolenkov.cryptoexchangeservice.exception.BadRequestException
import tech.frolenkov.cryptoexchangeservice.repository.OrderRepository
import tech.frolenkov.cryptoexchangeservice.service.wallet.WalletService
import java.math.BigDecimal

@Service
class OrderService(
    private val repository: OrderRepository,
    private val userCtx: UserContext,
    private val walletService: WalletService,
) {

    fun createOrder(orderRequest: OrderRequest) {
        val user = userCtx.getCurrentUser()
        log.debug("User: ${user.username} create order")
        if (orderRequest.type == OrderType.SELL && orderRequest.orderKind == OrderKind.LIMIT) {
            val wallet = walletService.findWalletByUser(orderRequest.currency)
            if (wallet.balance < orderRequest.amount) {
                throw BadRequestException("There are not enough funds in the wallet")
            }
        }

        val order = Order(
            user = user,
            amount = orderRequest.amount,
            price = orderRequest.price,
            currency = orderRequest.currency,
            type = orderRequest.type,
            orderKind = orderRequest.orderKind
        )
        repository.save(order)

        matchOrders(order)
        log.debug("Order created successfully with ID: ${order.id}")
    }

    private fun matchOrders(newOrder: Order) {
        log.debug("matching orders")
        val matchingOrders = when (newOrder.type) {
            OrderType.BUY -> repository.findByCurrencyAndTypeAndStatus(newOrder.currency, OrderType.SELL, OrderStatus.PENDING)
            OrderType.SELL -> repository.findByCurrencyAndTypeAndStatus(newOrder.currency, OrderType.BUY, OrderStatus.PENDING)
        }

        for (matchingOrder in matchingOrders) {
            if (canMatchOrders(newOrder, matchingOrder)) {
                val matchedAmount = minOf(newOrder.amount, matchingOrder.amount)

                processMatchedOrders(newOrder, matchingOrder, matchedAmount)

                if (newOrder.amount == matchedAmount) break
            }
        }
    }

    private fun canMatchOrders(newOrder: Order, existingOrder: Order): Boolean {
        return when (newOrder.orderKind) {
            OrderKind.MARKET -> true
            OrderKind.LIMIT -> when (newOrder.type) {
                OrderType.BUY -> newOrder.price >= existingOrder.price
                OrderType.SELL -> newOrder.price <= existingOrder.price
            }
        }
    }

    private fun processMatchedOrders(newOrder: Order, existingOrder: Order, matchedAmount: BigDecimal) {
        if (newOrder.amount == matchedAmount) {
            newOrder.status = OrderStatus.COMPLETED
        } else {
            newOrder.amount.minus(matchedAmount)
        }

        if (existingOrder.amount == matchedAmount) {
            existingOrder.status = OrderStatus.COMPLETED
        } else {
            existingOrder.amount.minus(matchedAmount)
        }

        repository.save(newOrder)
        repository.save(existingOrder)
    }


    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}