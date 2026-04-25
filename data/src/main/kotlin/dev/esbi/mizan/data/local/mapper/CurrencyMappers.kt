package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.domain.model.Currency

fun CurrencyEntity.toDomain(): Currency {
    return Currency(
        code = code,
        name = name,
        symbol = symbol,
        exchangeRate = rateToBase,
        unitPosition = dev.esbi.mizan.domain.model.UnitPosition.FRONT,
        decimalDigits = 2,
        orderIndex = 0,
        isMainCurrency = isBaseCurrency,
        isUserDefined = false
    )
}
