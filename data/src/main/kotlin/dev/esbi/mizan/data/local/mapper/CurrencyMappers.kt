package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.domain.model.Currency

fun CurrencyEntity.toDomain(): Currency {
    return Currency(
        code = code,
        name = name,
        symbol = symbol,
        rateToBase = rateToBase,
        isBaseCurrency = isBaseCurrency
    )
}
