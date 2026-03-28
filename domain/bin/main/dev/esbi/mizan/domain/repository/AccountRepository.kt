package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeAccounts(): Flow<List<Account>>
    suspend fun getAccount(id: Long): Account?
    suspend fun createAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(id: Long)
    suspend fun updateBalance(id: Long, amount: Double)
}
