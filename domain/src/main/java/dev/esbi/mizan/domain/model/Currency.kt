package dev.esbi.mizan.domain.model

data class Currency(
    val code: String,    // "UZS", "USD", "EUR" - Primary Key bo'ladi
    val name: String,    // "O'zbek so'mi", "US Dollar"
    val symbol: String,  // "so'm", "$", "€"
    // Asosiy valyutaga nisbatan kursi (Reports va Total Balance uchun kerak)
    // Agar Code == BaseCurrency (UZS) bo'lsa, rate = 1.0 bo'ladi.
    val rateToBase: Double,
    val isBaseCurrency: Boolean // Qaysi biri asosiy ekanligini bilish uchun
) {
    companion object {
        val UZS = Currency(
            code = "UZS",
            name = "O'zbek so'mi",
            symbol = "so'm",
            rateToBase = 1.0,
            isBaseCurrency = true
        )
    }
}
