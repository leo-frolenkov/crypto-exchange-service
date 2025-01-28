package tech.frolenkov.cryptoexchangeservice.api.http.request

import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderKind
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderType
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import java.math.BigDecimal

data class OrderRequest(
    val amount: BigDecimal,
    val price: BigDecimal,
    val currency: Currency,
    val type: OrderType,
    val orderKind: OrderKind
)
