package dev.esbi.mizan.feature.addtransaction.domain.repository

import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAllAccounts(): Flow<List<Account>>
    suspend fun getAccountById(id: String): Account?
}
