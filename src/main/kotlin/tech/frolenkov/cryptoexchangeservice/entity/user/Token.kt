package tech.frolenkov.cryptoexchangeservice.entity.user

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
