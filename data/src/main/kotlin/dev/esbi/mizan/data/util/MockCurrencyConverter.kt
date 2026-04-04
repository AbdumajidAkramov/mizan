package dev.esbi.mizan.data.util

import dev.esbi.mizan.domain.util.CurrencyConverter
import javax.inject.Inject

import java.math.BigDecimal
import java.math.RoundingMode

class MockCurrencyConverter @Inject constructor() : CurrencyConverter {

    private val ratesRelativeToBase = mapOf(
        "UZS" to BigDecimal("1.0"),
        "USD" to BigDecimal("12800.0"),
        "EUR" to BigDecimal("14000.0"),
        "RUB" to BigDecimal("142.0"),
        "GBP" to BigDecimal("16500.0"),
        "KZT" to BigDecimal("28.5"),
        "KGS" to BigDecimal("145.0"),
        "TRY" to BigDecimal("420.0"),
        "CNY" to BigDecimal("1760.0"),
        "JPY" to BigDecimal("85.0"),
        "AED" to BigDecimal("3480.0"),
        "SAR" to BigDecimal("3415.0")
    )

    override fun convertToBase(amount: BigDecimal, fromCurrencyCode: String): BigDecimal {
        val rate = ratesRelativeToBase[fromCurrencyCode.uppercase()] ?: BigDecimal.ONE
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN)
    }

    override fun convert(
        amount: BigDecimal,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): BigDecimal {
        val amountInBase = convertToBase(amount, fromCurrencyCode)
        val toRate = ratesRelativeToBase[toCurrencyCode.uppercase()] ?: BigDecimal.ONE
        return amountInBase.divide(toRate, 2, RoundingMode.HALF_EVEN)
    }

    override fun getBaseCurrencyCode(): String = "UZS"
}
