package dev.esbi.mizan.data.repository

import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun observeAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccountsWithCurrency().map { rows ->
            rows.map { it.toDomain() }
        }
    }

    override suspend fun getAccount(id: Long): Account? {
        return accountDao.getAccountWithCurrencyById(id.toString())?.toDomain()
    }
}
