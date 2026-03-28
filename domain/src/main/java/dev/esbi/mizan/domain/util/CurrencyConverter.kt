package dev.esbi.mizan.domain.util

interface CurrencyConverter {
    fun convertToBase(amount: Double, fromCurrencyCode: String): Double
    fun convert(amount: Double, fromCurrencyCode: String, toCurrencyCode: String): Double
    fun getBaseCurrencyCode(): String
}
