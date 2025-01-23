package tech.frolenkov.cryptoexchangeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CryptoExchangeServiceApplication

fun main(args: Array<String>) {
    runApplication<CryptoExchangeServiceApplication>(*args)
}
