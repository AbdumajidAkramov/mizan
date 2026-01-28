package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun observeCurrencies(): Flow<List<Currency>>
    suspend fun setBaseCurrency(code: String)
    suspend fun updateRate(code: String, rateToBase: Double)
    suspend fun getCurrencyByCode(code: String): Currency?
}
