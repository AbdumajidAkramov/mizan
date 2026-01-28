package dev.esbi.mizan.data.repository

import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.data.local.mapper.toEntity
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionsDao: TransactionsDao
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

    override suspend fun saveTransaction(transaction: Transaction): Result<Unit> {
        return try {
            transactionsDao.insertTransaction(transaction.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
