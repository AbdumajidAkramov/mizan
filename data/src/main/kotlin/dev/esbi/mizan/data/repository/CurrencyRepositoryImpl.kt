package dev.esbi.mizan.data.repository

import dev.esbi.mizan.data.local.dao.CurrencyDao
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao
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
        currencyDao.updateRate(code, rateToBase)
    }

    override suspend fun getCurrencyByCode(code: String): Currency? {
        return currencyDao.getCurrencyByCode(code)?.toDomain()
    }
}
