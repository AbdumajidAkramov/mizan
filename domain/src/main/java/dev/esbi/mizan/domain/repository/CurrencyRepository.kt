package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.CurrencyConfig
import dev.esbi.mizan.domain.model.UnitPosition
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CurrencyRepository {
    fun observeCurrencies(): Flow<List<Currency>>
    suspend fun setBaseCurrency(code: String)
    suspend fun updateRate(code: String, rateToBase: Double)
    suspend fun getCurrencyByCode(code: String): Currency?

    // Sub-currency management
    fun observeSubCurrencies(): Flow<List<CurrencyConfig>>
    suspend fun getSubCurrency(code: String): CurrencyConfig?
    suspend fun saveSubCurrency(config: CurrencyConfig)
    suspend fun deleteSubCurrency(code: String)
    suspend fun updateSubCurrencyOrder(configs: List<CurrencyConfig>)
    suspend fun updateSubCurrencySettings(
        code: String,
        exchangeRate: BigDecimal,
        unitPosition: UnitPosition,
        decimalDigits: Int
    )
    suspend fun syncExchangeRates(): Result<Unit>
    
    suspend fun isCurrencyCodeUnique(code: String): Boolean
}
