package dev.esbi.mizan.data.util

import dev.esbi.mizan.domain.util.CurrencyConverter
import javax.inject.Inject

class MockCurrencyConverter @Inject constructor() : CurrencyConverter {

    private val ratesRelativeToBase = mapOf(
        "UZS" to 1.0,
        "USD" to 12800.0,
        "EUR" to 14000.0,
        "RUB" to 142.0,
        "GBP" to 16500.0,
        "KZT" to 28.5,
        "KGS" to 145.0,
        "TRY" to 420.0,
        "CNY" to 1760.0,
        "JPY" to 85.0,
        "AED" to 3480.0,
        "SAR" to 3415.0
    )

    override fun convertToBase(amount: Double, fromCurrencyCode: String): Double {
        val rate = ratesRelativeToBase[fromCurrencyCode.uppercase()] ?: 1.0
        return amount * rate
    }

    override fun convert(
        amount: Double,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Double {
        val amountInBase = convertToBase(amount, fromCurrencyCode)
        val toRate = ratesRelativeToBase[toCurrencyCode.uppercase()] ?: 1.0
        return amountInBase / toRate
    }

    override fun getBaseCurrencyCode(): String = "UZS"
}
