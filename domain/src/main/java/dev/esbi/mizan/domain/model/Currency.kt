package dev.esbi.mizan.domain.model

import java.math.BigDecimal

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    val exchangeRate: BigDecimal,
    val unitPosition: UnitPosition = UnitPosition.FRONT,
    val decimalDigits: Int = 2,
    val orderIndex: Int = 0,
    val isMainCurrency: Boolean = false,
    val isUserDefined: Boolean = false
) {
    companion object {
        val UZS = Currency(
            code = "UZS",
            name = "O'zbek so'mi",
            symbol = "so'm",
            exchangeRate = BigDecimal.ONE,
            unitPosition = UnitPosition.FRONT,
            decimalDigits = 0,
            orderIndex = 0,
            isMainCurrency = true,
            isUserDefined = false
        )
    }
}
