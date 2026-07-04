package dev.esbi.mizan.design.components.currency

import androidx.compose.runtime.Immutable

@Immutable
data class CurrencyModel(
    val code: String,
    val isMainCurrency: Boolean,
    val orderIndex: Int = 0
)
