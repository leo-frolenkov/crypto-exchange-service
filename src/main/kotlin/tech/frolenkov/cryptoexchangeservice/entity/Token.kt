package tech.frolenkov.cryptoexchangeservice.entity

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
