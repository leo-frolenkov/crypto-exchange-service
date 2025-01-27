package tech.frolenkov.cryptoexchangeservice.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.frolenkov.cryptoexchangeservice.api.controller.WalletController.Companion.uri
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import tech.frolenkov.cryptoexchangeservice.service.wallet.WalletService
import java.math.BigDecimal

@RestController
@RequestMapping(uri)
class WalletController(
    private val service: WalletService
) {

    @Operation(summary = "Create a new wallet for the user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Wallet created successfully"),
        ApiResponse(responseCode = "409", description = "Wallet already exists")
    ])
    @PostMapping("/create")
    fun create(@RequestParam currency: Currency): ResponseEntity<String> {
        service.createWallet(currency)
        return ResponseEntity.ok("Wallet created successfully for ${currency.name}")
    }

    @Operation(summary = "Deposit funds into the user's wallet")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Funds deposited successfully"),
        ApiResponse(responseCode = "404", description = "User not found"),
        ApiResponse(responseCode = "400", description = "Insufficient funds")
    ])
    @PostMapping("/deposit")
    fun deposit(@RequestParam currency: Currency, @RequestParam amount: BigDecimal): ResponseEntity<String> {
        service.deposit(currency, amount)
        return ResponseEntity.ok("Deposited $amount to ${currency.name} wallet")
    }

    @Operation(summary = "Withdraw funds from the user's wallet")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Funds withdrawn successfully"),
        ApiResponse(responseCode = "404", description = "User not found"),
        ApiResponse(responseCode = "400", description = "Insufficient funds")
    ])
    @PostMapping("/withdraw")
    fun withdraw(@RequestParam currency: Currency, @RequestParam amount: BigDecimal): ResponseEntity<String> {
        service.withdraw(currency, amount)
        return ResponseEntity.ok("Withdrew $amount from ${currency.name} wallet")
    }


    companion object {
        const val uri = "/api/v1/wallets"
    }
}