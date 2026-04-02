package dev.esbi.mizan.domain.model

import java.math.BigDecimal

data class CurrencyConfig(
    val code: String,
    val name: String,
    val symbol: String,
    val exchangeRate: BigDecimal,
    val unitPosition: UnitPosition = UnitPosition.FRONT,
    val decimalDigits: Int = 2,
    val orderIndex: Int = 0,
    val isMainCurrency: Boolean = false
)
