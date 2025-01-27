package tech.frolenkov.cryptoexchangeservice.entity.wallet

enum class Currency(val type: WalletType = WalletType.CRYPTO) {
    USDT(WalletType.CRYPTO),
    BTC(WalletType.CRYPTO),
    ETH(WalletType.CRYPTO),
    USD(WalletType.FIAT),
    EUR(WalletType.FIAT);

    companion object {
        fun fromString(currency: String): Currency? =
            entries.find { it.name.equals(currency, ignoreCase = true) }
    }
}