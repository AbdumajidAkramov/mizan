package dev.esbi.mizan.domain.model

interface Currency {
    val code: String    // "UZS", "USD", "EUR" - Primary Key bo'ladi

    val name: String    // "O'zbek so'mi", "US Dollar"
    val symbol: String  // "so'm", "$", "€"

    // Asosiy valyutaga nisbatan kursi (Reports va Total Balance uchun kerak)
    // Agar Code == BaseCurrency (UZS) bo'lsa, rate = 1.0 bo'ladi.
    val rateToBase: Double

    val isBaseCurrency: Boolean // Qaysi biri asosiy ekanligini bilish uchun

}
