package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.AccountGroup
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    // Accounts
    fun observeAccounts(): Flow<List<Account>>
    suspend fun getAccount(id: Long): Account?
    suspend fun createAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(id: Long)
    suspend fun markAccountAsDeleted(id: Long)
    suspend fun updateBalance(id: Long, amount: Double)

    // Account Groups
    fun observeAccountGroups(): Flow<List<AccountGroup>>
    suspend fun saveAccountGroup(group: AccountGroup)
    suspend fun deleteAccountGroup(id: Long)
    suspend fun isSystemGroup(groupId: Long): Boolean
}
