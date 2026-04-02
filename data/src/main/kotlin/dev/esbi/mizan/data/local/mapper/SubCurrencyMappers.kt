package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.currency.SubCurrencyEntity
import dev.esbi.mizan.domain.model.CurrencyConfig
import dev.esbi.mizan.domain.model.UnitPosition
import java.math.BigDecimal

fun SubCurrencyEntity.toDomain(): CurrencyConfig {
    return CurrencyConfig(
        code = code,
        name = name,
        symbol = symbol,
        exchangeRate = BigDecimal(exchangeRate),
        unitPosition = try {
            UnitPosition.valueOf(unitPosition)
        } catch (_: Exception) {
            UnitPosition.FRONT
        },
        decimalDigits = decimalDigits,
        orderIndex = orderIndex,
        isMainCurrency = isMainCurrency
    )
}

fun CurrencyConfig.toEntity(): SubCurrencyEntity {
    return SubCurrencyEntity(
        code = code,
        name = name,
        symbol = symbol,
        exchangeRate = exchangeRate.toPlainString(),
        unitPosition = unitPosition.name,
        decimalDigits = decimalDigits,
        orderIndex = orderIndex,
        isMainCurrency = isMainCurrency
    )
}
