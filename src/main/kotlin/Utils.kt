fun usdToCurrency(usd: Double, currency: Currency): Double {
    return usd * when (currency) {
        Currency.USD -> 1.0
        Currency.EUR -> 0.91
        Currency.RUB -> 133.0
    }
}