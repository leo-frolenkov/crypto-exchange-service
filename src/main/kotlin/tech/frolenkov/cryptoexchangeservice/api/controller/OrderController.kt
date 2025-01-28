package tech.frolenkov.cryptoexchangeservice.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.frolenkov.cryptoexchangeservice.api.controller.OrderController.Companion.uri
import tech.frolenkov.cryptoexchangeservice.api.http.request.OrderRequest
import tech.frolenkov.cryptoexchangeservice.service.order.OrderService

@Tag(name = "Orders")
@RestController
@RequestMapping(uri)
class OrderController(
    private val service: OrderService
) {

    @Operation(summary = "create a new order")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Order created successfully"),
        ApiResponse(responseCode = "400", description = "Bad request"),
        ApiResponse(responseCode = "404", description = "User not found")
    ])
    @PostMapping("/create")
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<String> {
        service.createOrder(orderRequest)
        return ResponseEntity("Order created successfully",HttpStatus.CREATED)
    }


    companion object {
        const val uri = "/api/v1/orders"
    }
}