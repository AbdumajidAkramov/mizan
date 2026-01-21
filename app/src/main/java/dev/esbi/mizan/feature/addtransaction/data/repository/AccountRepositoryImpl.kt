package dev.esbi.mizan.feature.addtransaction.data.repository

import android.util.Log
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.feature.addtransaction.data.mapper.toDomain
import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {
    
    private val tag = "AccountRepository"
    
    override fun getAllAccounts(): Flow<List<Account>> {
        Log.d(tag, "Getting all accounts")
        return accountDao.getAllAccounts().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getAccountById(id: String): Account? {
        Log.d(tag, "Getting account by id: $id")
        return accountDao.getAccountById(id)?.toDomain()
    }
}
