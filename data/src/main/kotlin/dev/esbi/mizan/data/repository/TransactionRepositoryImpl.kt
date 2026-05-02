package dev.esbi.mizan.data.repository

import dev.esbi.mizan.data.local.dao.FinanceDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.data.local.mapper.toEntity
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionsDao: TransactionsDao,
    private val financeDao: FinanceDao
) : TransactionRepository {

    override fun observeTransactions(): Flow<List<Transaction>> {
        return transactionsDao.observeAllTransactionsWithCurrency().map { rows ->
            rows.map { it.toDomain() }
        }
    }

    override fun observeTransactionsByType(type: Transaction.Type): Flow<List<Transaction>> {
        return transactionsDao.observeTransactionsByTypeWithCurrency(type.name).map { rows ->
            rows.map { it.toDomain() }
        }
    }

    override fun observeTransactionsByAccountId(accountId: Long): Flow<List<Transaction>> {
        return transactionsDao.observeTransactionsByAccountIdWithCurrency(accountId).map { rows ->
            rows.map { it.toDomain() }
        }
    }

    override suspend fun saveTransaction(transaction: Transaction): Result<Unit> {
        return try {
            // Use atomic operation that updates both transaction and account balance
            financeDao.insertTransactionWithBalanceUpdate(transaction.toEntity())
            Result.success(Unit)
        } catch (e: IllegalArgumentException) {
            // Validation errors (missing accountId, etc.)
            Result.failure(e)
        } catch (e: IllegalStateException) {
            // Business logic errors (insufficient funds, etc.)
            Result.failure(e)
        } catch (e: Exception) {
            // Database errors
            Result.failure(e)
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return try {
            transactionsDao.getTransactionByIdWithCurrency(id)?.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        return try {
            // Get the old transaction first
            val oldTransaction = transactionsDao.getTransactionById(transaction.id.toString())
                ?: return Result.failure(IllegalArgumentException("Transaction not found"))

            // Use atomic operation that reverses old balance changes and applies new ones
            financeDao.updateTransactionWithBalanceUpdate(
                oldTransaction = oldTransaction,
                newTransaction = transaction.toEntity()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: Long): Result<Unit> {
        return try {
            // Get the transaction first
            val transaction = transactionsDao.getTransactionById(transactionId.toString())
                ?: return Result.failure(IllegalArgumentException("Transaction not found"))

            // Use atomic operation that reverses balance changes and deletes transaction
            financeDao.deleteTransactionWithBalanceUpdate(transaction)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
