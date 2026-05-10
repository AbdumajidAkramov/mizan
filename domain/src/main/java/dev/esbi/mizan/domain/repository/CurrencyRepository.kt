package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.UnitPosition
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CurrencyRepository {
    // All currencies
    fun observeAllCurrencies(): Flow<List<Currency>>
    suspend fun getAllCurrencies(): List<Currency>
    suspend fun getCurrencyByCode(code: String): Currency?
    
    // Main currency (single primary)
    fun observeMainCurrency(): Flow<Currency?>
    suspend fun getMainCurrency(): Currency?
    suspend fun setMainCurrency(code: String)
    
    // Secondary currencies (quick access for transactions)
    fun observeSecondaryCurrencies(): Flow<List<Currency>>
    suspend fun getSecondaryCurrencies(): List<Currency>
    suspend fun toggleSecondaryStatus(code: String, isSecondary: Boolean)
    
    // Transaction entry currencies (main + secondary)
    fun observeTransactionCurrencies(): Flow<List<Currency>>
    
    // User-defined currencies
    fun observeUserDefinedCurrencies(): Flow<List<Currency>>
    suspend fun saveCurrency(currency: Currency)
    suspend fun deleteCurrency(code: String)
    
    // Currency settings
    suspend fun updateExchangeRate(code: String, rate: BigDecimal)
    suspend fun updateCurrencyOrder(currencies: List<Currency>)
    suspend fun updateCurrencySettings(
        code: String,
        exchangeRate: BigDecimal,
        unitPosition: UnitPosition,
        decimalDigits: Int
    )
    
    // Exchange rate sync
    suspend fun syncExchangeRates(): Result<Unit>
    
    // Validation
    suspend fun isCurrencyCodeUnique(code: String): Boolean
    
    // Legacy support
    fun observeCurrencies(): Flow<List<Currency>>
    suspend fun setBaseCurrency(code: String)
    suspend fun updateRate(code: String, rateToBase: Double)
}
