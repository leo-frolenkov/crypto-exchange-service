package tech.frolenkov.cryptoexchangeservice.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.frolenkov.cryptoexchangeservice.api.controller.AuthController.Companion.uri
import tech.frolenkov.cryptoexchangeservice.api.http.request.AuthRequest
import tech.frolenkov.cryptoexchangeservice.service.auth.AuthService

@Tag(name = "Authorization and Authentication")
@RestController
@RequestMapping(uri)
class AuthController(
    private val service: AuthService
) {

    @Operation(summary = "Registration user", description = "Registration user", responses = [
        ApiResponse(responseCode = "200", description = "User create and get token"),
        ApiResponse(responseCode = "400", description = "User already exists"),
    ])
    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequest) =
        service.register(request)

    @Operation(summary = "Login user", description = "login user", responses = [
        ApiResponse(responseCode = "200", description = "Success login"),
        ApiResponse(responseCode = "404", description = "User does not exists"),
    ])
    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest) =
        service.login(request)

    companion object {
        const val uri = "/api/v1/auth"
    }
}