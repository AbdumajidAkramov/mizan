package dev.esbi.mizan.data.repository

import dev.esbi.mizan.data.local.dao.CurrencyDao
import dev.esbi.mizan.data.local.dao.SubCurrencyDao
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.data.local.mapper.toEntity
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.CurrencyConfig
import dev.esbi.mizan.domain.model.UnitPosition
import dev.esbi.mizan.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

import java.math.BigDecimal

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val subCurrencyDao: SubCurrencyDao
) : CurrencyRepository {

    override fun observeCurrencies(): Flow<List<Currency>> {
        return currencyDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun setBaseCurrency(code: String) {
        currencyDao.setBaseCurrency(code)
    }

    override suspend fun updateRate(code: String, rateToBase: Double) {
        currencyDao.updateRate(code, BigDecimal.valueOf(rateToBase))
    }

    override suspend fun getCurrencyByCode(code: String): Currency? {
        return currencyDao.getCurrencyByCode(code)?.toDomain()
    }

    // Sub-currency management

    override fun observeSubCurrencies(): Flow<List<CurrencyConfig>> {
        return subCurrencyDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSubCurrency(code: String): CurrencyConfig? {
        return subCurrencyDao.getByCode(code)?.toDomain()
    }

    override suspend fun saveSubCurrency(config: CurrencyConfig) {
        subCurrencyDao.insert(config.toEntity())
    }

    override suspend fun deleteSubCurrency(code: String) {
        subCurrencyDao.deleteByCode(code)
    }

    override suspend fun updateSubCurrencyOrder(configs: List<CurrencyConfig>) {
        configs.forEachIndexed { index, config ->
            subCurrencyDao.updateOrder(config.code, index)
        }
    }

    override suspend fun updateSubCurrencySettings(
        code: String,
        exchangeRate: BigDecimal,
        unitPosition: UnitPosition,
        decimalDigits: Int
    ) {
        subCurrencyDao.updateSettings(
            code = code,
            exchangeRate = exchangeRate.toPlainString(),
            unitPosition = unitPosition.name,
            decimalDigits = decimalDigits
        )
    }

    override suspend fun syncExchangeRates(): Result<Unit> {
        // Placeholder for API integration (CBU / Fixer)
        // In production, this would call an exchange rate API and update all sub-currencies
        return Result.success(Unit)
    }

    override suspend fun isCurrencyCodeUnique(code: String): Boolean {
        return subCurrencyDao.getByCode(code) == null
    }
}
