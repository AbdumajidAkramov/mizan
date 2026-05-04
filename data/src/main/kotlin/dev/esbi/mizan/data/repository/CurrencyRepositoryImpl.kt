package dev.esbi.mizan.data.repository

import dev.esbi.mizan.data.local.dao.CurrencyDao
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.data.local.mapper.toEntity
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.UnitPosition
import dev.esbi.mizan.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao
) : CurrencyRepository {

    // ========== All Currencies ==========
    
    override fun observeAllCurrencies(): Flow<List<Currency>> {
        return currencyDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getAllCurrencies(): List<Currency> {
        return currencyDao.getAll().map { it.toDomain() }
    }

    override suspend fun getCurrencyByCode(code: String): Currency? {
        return currencyDao.getCurrencyByCode(code)?.toDomain()
    }

    // ========== Main Currency ==========
    
    override fun observeMainCurrency(): Flow<Currency?> {
        return currencyDao.observeMainCurrency().map { it?.toDomain() }
    }
    
    override suspend fun getMainCurrency(): Currency? {
        return currencyDao.getMainCurrency()?.toDomain()
    }

    override suspend fun setMainCurrency(code: String) {
        currencyDao.setMainCurrency(code)
    }

    // ========== Secondary Currencies ==========
    
    override fun observeSecondaryCurrencies(): Flow<List<Currency>> {
        return currencyDao.observeSecondaryCurrencies().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getSecondaryCurrencies(): List<Currency> {
        return currencyDao.getSecondaryCurrencies().map { it.toDomain() }
    }
    
    override suspend fun toggleSecondaryStatus(code: String, isSecondary: Boolean) {
        currencyDao.updateSecondaryStatus(code, isSecondary)
    }

    // ========== Transaction Currencies ==========
    
    override fun observeTransactionCurrencies(): Flow<List<Currency>> {
        return currencyDao.observeTransactionCurrencies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // ========== User-Defined Currencies ==========
    
    override fun observeUserDefinedCurrencies(): Flow<List<Currency>> {
        return currencyDao.observeUserDefinedCurrencies().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun saveCurrency(currency: Currency) {
        currencyDao.insert(currency.toEntity())
    }
    
    override suspend fun deleteCurrency(code: String) {
        currencyDao.deleteByCode(code)
    }

    // ========== Currency Settings ==========
    
    override suspend fun updateExchangeRate(code: String, rate: BigDecimal) {
        currencyDao.updateExchangeRate(code, rate)
    }
    
    override suspend fun updateCurrencyOrder(currencies: List<Currency>) {
        currencies.forEach { currency ->
            currencyDao.updateOrderIndex(currency.code, currency.orderIndex)
        }
    }
    
    override suspend fun updateCurrencySettings(
        code: String,
        exchangeRate: BigDecimal,
        unitPosition: UnitPosition,
        decimalDigits: Int
    ) {
        currencyDao.updateSettings(
            code = code,
            exchangeRate = exchangeRate,
            unitPosition = when (unitPosition) {
                UnitPosition.FRONT -> "FRONT"
                UnitPosition.END -> "END"
            },
            decimalDigits = decimalDigits
        )
    }

    // ========== Exchange Rate Sync ==========
    
    override suspend fun syncExchangeRates(): Result<Unit> {
        // Placeholder for API integration (CBU / Fixer)
        // In production, this would call an exchange rate API and update all currencies
        return Result.success(Unit)
    }

    // ========== Validation ==========
    
    override suspend fun isCurrencyCodeUnique(code: String): Boolean {
        return currencyDao.getCurrencyByCode(code) == null
    }

    // ========== Legacy Support ==========
    
    override fun observeCurrencies(): Flow<List<Currency>> {
        return observeAllCurrencies()
    }
    
    override suspend fun setBaseCurrency(code: String) {
        setMainCurrency(code)
    }

    override suspend fun updateRate(code: String, rateToBase: Double) {
        updateExchangeRate(code, BigDecimal.valueOf(rateToBase))
    }
}
