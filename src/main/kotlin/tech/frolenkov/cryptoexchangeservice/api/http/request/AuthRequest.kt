package tech.frolenkov.cryptoexchangeservice.api.http.request

data class AuthRequest(
    var username: String,
    var password: String
)
