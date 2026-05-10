package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.UnitPosition

fun CurrencyEntity.toDomain(): Currency {
    return Currency(
        code = code,
        name = name,
        symbol = symbol,
        exchangeRate = exchangeRate,
        unitPosition = when (unitPosition.uppercase()) {
            "FRONT" -> UnitPosition.FRONT
            "END" -> UnitPosition.END
            else -> UnitPosition.END
        },
        decimalDigits = decimalDigits,
        orderIndex = orderIndex,
        isMainCurrency = isMainCurrency,
        isSecondary = isSecondary,
        isUserDefined = isUserDefined
    )
}

fun Currency.toEntity(): CurrencyEntity {
    return CurrencyEntity(
        code = code,
        name = name,
        symbol = symbol,
        exchangeRate = exchangeRate,
        unitPosition = when (unitPosition) {
            UnitPosition.FRONT -> "FRONT"
            UnitPosition.END -> "END"
        },
        decimalDigits = decimalDigits,
        orderIndex = orderIndex,
        isMainCurrency = isMainCurrency,
        isSecondary = isSecondary,
        isUserDefined = isUserDefined,
        isBaseCurrency = isMainCurrency // Keep legacy field in sync
    )
}
