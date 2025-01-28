package tech.frolenkov.cryptoexchangeservice.service.wallet

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tech.frolenkov.cryptoexchangeservice.UserContext
import tech.frolenkov.cryptoexchangeservice.entity.user.User
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Wallet
import tech.frolenkov.cryptoexchangeservice.exception.BadRequestException
import tech.frolenkov.cryptoexchangeservice.exception.ConflictException
import tech.frolenkov.cryptoexchangeservice.exception.NotFoundException
import tech.frolenkov.cryptoexchangeservice.repository.WalletRepository
import java.math.BigDecimal

@Service
class WalletService(
    private val repository: WalletRepository,
    private val userCtx: UserContext
) {

    fun createWallet(currency: Currency) {
        val user = userCtx.getCurrentUser()
        logger.debug("User: ${user.username} create a wallet")
        val existingWallet = repository.findByUserAndCurrency(user, currency)
        if (existingWallet != null) throw ConflictException("Wallet already exists for this currency")

        val wallet = Wallet(user = user, currency = currency, type = currency.type)
        repository.save(wallet)
    }

    fun deposit(currency: Currency, amount: BigDecimal) {
        val user = userCtx.getCurrentUser()
        logger.debug("User: ${user.username} deposit")
        val wallet = repository.findByUserAndCurrency(user, currency) ?: throw NotFoundException("Wallet not found")
        wallet.apply {
            this.balance = balance.plus(amount)
        }
        repository.save(wallet)
    }

    fun withdraw(currency: Currency, amount: BigDecimal) {
        val user = userCtx.getCurrentUser()
        logger.debug("User: ${user.username} withdraw")
        val wallet = repository.findByUserAndCurrency(user, currency) ?: throw NotFoundException("Wallet not found")
        if (wallet.balance < amount) throw BadRequestException("Insufficient funds")
        wallet.apply {
            this.balance = balance.minus(amount)
        }
        repository.save(wallet)
    }

    fun findWalletByUser(currency: Currency): Wallet {
        val user = userCtx.getCurrentUser()
        return repository.findByUserAndCurrency(user, currency) ?: throw NotFoundException("Wallet not found")
    }


    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}