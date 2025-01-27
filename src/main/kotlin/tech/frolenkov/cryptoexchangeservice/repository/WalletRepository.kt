package tech.frolenkov.cryptoexchangeservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Wallet

interface WalletRepository: JpaRepository<Wallet, Long> {

    fun findByUserAndCurrency(user: User, currency: Currency): Wallet?
}