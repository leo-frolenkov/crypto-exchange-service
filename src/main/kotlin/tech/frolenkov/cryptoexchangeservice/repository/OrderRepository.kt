package tech.frolenkov.cryptoexchangeservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.frolenkov.cryptoexchangeservice.entity.orders.Order
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderStatus
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderType
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency

interface OrderRepository: JpaRepository<Order, Long> {
    fun findByCurrencyAndTypeAndStatus(currency: Currency, type: OrderType, status: OrderStatus): List<Order>
}