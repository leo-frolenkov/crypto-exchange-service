package tech.frolenkov.cryptoexchangeservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "security")
data class SecurityProperties(
    var secret: String = "",
    var expire: Long = 0
)