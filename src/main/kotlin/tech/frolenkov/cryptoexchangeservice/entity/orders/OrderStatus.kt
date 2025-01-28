package tech.frolenkov.cryptoexchangeservice.entity.orders

enum class OrderStatus {
    PENDING,
    APPROVE,
    UNAPPROVE,
    CANCELED,
    COMPLETED,
}