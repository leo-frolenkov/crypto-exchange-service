package tech.frolenkov.cryptoexchangeservice.wallets

import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tech.frolenkov.cryptoexchangeservice.api.controller.WalletController
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import tech.frolenkov.cryptoexchangeservice.exception.BadRequestException
import tech.frolenkov.cryptoexchangeservice.exception.ConflictException
import tech.frolenkov.cryptoexchangeservice.exception.NotFoundException
import tech.frolenkov.cryptoexchangeservice.service.auth.TokenProvider
import tech.frolenkov.cryptoexchangeservice.service.wallet.WalletService
import java.math.BigDecimal

@WebMvcTest(WalletController::class)
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var walletService: WalletService

    @MockitoBean lateinit var provider: TokenProvider


    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `create wallet should return 200`() {
        val currency = Currency.USD

        mockMvc.perform(
            post("/api/v1/wallets/create")
                .param("currency", currency.name)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Wallet created successfully for ${currency.name}"))

        verify(walletService).createWallet(currency)
    }

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `deposit should return 200`() {
        val currency = Currency.USD
        val amount = BigDecimal("100.00")

        mockMvc.perform(
            post("/api/v1/wallets/deposit")
                .param("currency", currency.name)
                .param("amount", amount.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Deposited $amount to ${currency.name} wallet"))

        verify(walletService).deposit(currency, amount)
    }

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `deposit should return 404`() {
        val currency = Currency.USD
        val amount = BigDecimal("100.00")
        doThrow(NotFoundException("Wallet not found"))
            .`when`(walletService).deposit(currency, amount)

        mockMvc.perform(
            post("/api/v1/wallets/deposit")
                .param("currency", currency.name)
                .param("amount", amount.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `withdraw should return 200`() {
        val currency = Currency.USD
        val amount = BigDecimal("50.00")

        mockMvc.perform(
            post("/api/v1/wallets/withdraw")
                .param("currency", currency.name)
                .param("amount", amount.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Withdrew $amount from ${currency.name} wallet"))

        verify(walletService).withdraw(currency, amount)
    }

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `withdraw should return 400`() {
        val currency = Currency.USD
        val amount = BigDecimal("50.00")
        doThrow(BadRequestException("Insufficient funds"))
            .`when`(walletService).withdraw(currency, amount)

        mockMvc.perform(
            post("/api/v1/wallets/withdraw")
                .param("currency", currency.name)
                .param("amount", amount.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `withdraw should return 404`() {
        val currency = Currency.USD
        val amount = BigDecimal("50.00")
        doThrow(NotFoundException("Wallet not found"))
            .`when`(walletService).withdraw(currency, amount)

        mockMvc.perform(
            post("/api/v1/wallets/withdraw")
                .param("currency", currency.name)
                .param("amount", amount.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }
}