package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeTransactions(): Flow<List<Transaction>>
    fun observeTransactionsByType(type: Transaction.Type): Flow<List<Transaction>>
    suspend fun saveTransaction(transaction: Transaction): Result<Unit>
}
