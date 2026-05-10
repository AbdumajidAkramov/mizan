package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface TransactionRepository {
    fun observeTransactions(): Flow<List<Transaction>>

    suspend fun observeTransactionsByMonth(yearMonth: YearMonth): Result<List<Transaction>>

    fun observeTransactionsByType(type: Transaction.Type): Flow<List<Transaction>>
    fun observeTransactionsByAccountId(accountId: Long): Flow<List<Transaction>>
    suspend fun saveTransaction(transaction: Transaction): Result<Unit>
    suspend fun getTransactionById(id: Long): Transaction?
    suspend fun updateTransaction(transaction: Transaction): Result<Unit>
    suspend fun deleteTransaction(transactionId: Long): Result<Unit>
}
