package tech.frolenkov.cryptoexchangeservice.order

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tech.frolenkov.cryptoexchangeservice.api.controller.OrderController
import tech.frolenkov.cryptoexchangeservice.api.http.request.OrderRequest
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderKind
import tech.frolenkov.cryptoexchangeservice.entity.orders.OrderType
import tech.frolenkov.cryptoexchangeservice.entity.wallet.Currency
import tech.frolenkov.cryptoexchangeservice.exception.ExceptionResponse
import tech.frolenkov.cryptoexchangeservice.exception.NotFoundException
import tech.frolenkov.cryptoexchangeservice.service.auth.TokenProvider
import tech.frolenkov.cryptoexchangeservice.service.order.OrderService
import java.math.BigDecimal

@WebMvcTest(OrderController::class)
class OrderControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var orderService: OrderService

    @MockitoBean lateinit var provider: TokenProvider

    private val objectMapper = ObjectMapper()

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `should create a buy order`() {
        val orderRequest = OrderRequest(
            amount = BigDecimal.valueOf(1),
            price = BigDecimal.valueOf(50000),
            currency = Currency.BTC,
            type = OrderType.BUY,
            orderKind = OrderKind.LIMIT
        )

        doNothing().`when`(orderService).createOrder(orderRequest)

        mockMvc.perform(
            post("${OrderController.uri}/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest))
                .with(csrf())
        )
            .andExpect(status().isCreated)
            .andExpect(content().string("Order created successfully"))

        verify(orderService, times(1)).createOrder(orderRequest)
    }

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `should create a sell order`() {
        val orderRequest = OrderRequest(
            amount = BigDecimal.valueOf(1),
            price = BigDecimal.valueOf(50000),
            currency = Currency.BTC,
            type = OrderType.SELL,
            orderKind = OrderKind.LIMIT
        )

        doNothing().`when`(orderService).createOrder(orderRequest)

        mockMvc.perform(
            post("${OrderController.uri}/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest))
                .with(csrf())
        )
            .andExpect(status().isCreated)
            .andExpect(content().string("Order created successfully"))

        verify(orderService, times(1)).createOrder(orderRequest)
    }

    @Test
    @WithMockUser(username = "test", authorities = ["CLIENT"])
    fun `should throw not found`() {
        val orderRequest = OrderRequest(
            amount = BigDecimal.valueOf(1),
            price = BigDecimal.valueOf(50000),
            currency = Currency.BTC,
            type = OrderType.BUY,
            orderKind = OrderKind.LIMIT
        )

        doThrow(NotFoundException("User not found"))
            .`when`(orderService).createOrder(orderRequest)

        mockMvc.perform(
            post("${OrderController.uri}/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest))
                .with(csrf())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().json(objectMapper.writeValueAsString(ExceptionResponse(404, "User not found"))))

        verify(orderService, times(1)).createOrder(orderRequest)
    }
}