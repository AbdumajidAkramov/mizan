package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.currency.SubCurrencyEntity
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.UnitPosition
import java.math.BigDecimal

fun SubCurrencyEntity.toDomain(): Currency {
    return Currency(
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
        isMainCurrency = isMainCurrency,
        isUserDefined = isUserDefined
    )
}

fun Currency.toEntity(): SubCurrencyEntity {
    return SubCurrencyEntity(
        code = code,
        name = name,
        symbol = symbol,
        exchangeRate = exchangeRate.toPlainString(),
        unitPosition = unitPosition.name,
        decimalDigits = decimalDigits,
        orderIndex = orderIndex,
        isMainCurrency = isMainCurrency,
        isUserDefined = isUserDefined
    )
}
