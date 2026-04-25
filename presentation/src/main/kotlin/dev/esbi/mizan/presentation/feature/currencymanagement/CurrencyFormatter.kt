package dev.esbi.mizan.presentation.feature.currencymanagement

import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.UnitPosition
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyFormatter {

    /**
     * Format an amount using the given Currency.
     * Example: 15000.0 with USD config -> "$15,000.00"
     * Example: 15000.0 with UZS config -> "15,000 so'm"
     * 
     * @param isAbbreviated If true, formats large numbers with K/M suffixes
     */
    fun format(amount: BigDecimal, config: Currency, isAbbreviated: Boolean = false): String {
        if (isAbbreviated) {
            return formatAbbreviated(amount, config)
        }
        val rounded = amount.setScale(config.decimalDigits, RoundingMode.HALF_UP)

        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = ','
            decimalSeparator = '.'
        }

        val pattern = buildPattern(config.decimalDigits)
        val formatter = DecimalFormat(pattern, symbols)
        val formatted = formatter.format(rounded)

        return when (config.unitPosition) {
            UnitPosition.FRONT -> "${config.symbol}$formatted"
            UnitPosition.END -> "$formatted ${config.symbol}"
        }
    }

    /**
     * Format a Double amount using the given Currency.
     */
    fun format(amount: Double, config: Currency, isAbbreviated: Boolean = false): String {
        return format(BigDecimal.valueOf(amount), config, isAbbreviated)
    }

    /**
     * Format amount with K/M abbreviation for large numbers.
     * - >= 1,000,000: Shows as "X.X M"
     * - >= 1,000: Shows as "X.X K"
     * - < 1,000: Shows full amount
     */
    private fun formatAbbreviated(amount: BigDecimal, config: Currency): String {
        val absAmount = amount.abs()
        val million = BigDecimal("1000000")
        val thousand = BigDecimal("1000")

        val (value, suffix) = when {
            absAmount >= million -> {
                val scaled = amount.divide(million, 1, RoundingMode.HALF_EVEN)
                Pair(scaled, "M")
            }
            absAmount >= thousand -> {
                val scaled = amount.divide(thousand, 1, RoundingMode.HALF_EVEN)
                Pair(scaled, "K")
            }
            else -> {
                return format(amount, config, isAbbreviated = false)
            }
        }

        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = ','
            decimalSeparator = '.'
        }

        val formatter = DecimalFormat("#,##0.0", symbols)
        val formatted = formatter.format(value)

        val withSuffix = "$formatted $suffix"
        return when (config.unitPosition) {
            UnitPosition.FRONT -> "${config.symbol}$withSuffix"
            UnitPosition.END -> "$withSuffix ${config.symbol}"
        }
    }

    /**
     * Convert an amount from one currency to another via the main currency.
     * Both exchange rates are relative to the main currency.
     */
    fun convert(
        amount: BigDecimal,
        fromConfig: Currency,
        toConfig: Currency
    ): BigDecimal {
        if (fromConfig.code == toConfig.code) return amount
        if (fromConfig.exchangeRate.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO

        // Convert to main currency first, then to target
        val inMainCurrency = amount.multiply(fromConfig.exchangeRate)
        return if (toConfig.exchangeRate.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal.ZERO
        } else {
            inMainCurrency.divide(toConfig.exchangeRate, 12, RoundingMode.HALF_UP)
        }
    }

    private fun buildPattern(decimalDigits: Int): String {
        return if (decimalDigits == 0) {
            "#,##0"
        } else {
            "#,##0." + "0".repeat(decimalDigits)
        }
    }
}
