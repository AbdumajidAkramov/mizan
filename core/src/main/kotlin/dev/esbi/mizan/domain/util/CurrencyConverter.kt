package dev.esbi.mizan.domain.util

interface CurrencyConverter {
    fun convertToBase(amount: java.math.BigDecimal, fromCurrencyCode: String): java.math.BigDecimal
    fun convert(amount: java.math.BigDecimal, fromCurrencyCode: String, toCurrencyCode: String): java.math.BigDecimal
    fun getBaseCurrencyCode(): String
}
