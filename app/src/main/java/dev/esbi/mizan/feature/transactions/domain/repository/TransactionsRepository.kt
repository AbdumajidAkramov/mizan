package dev.esbi.mizan.feature.transactions.domain.repository

import dev.esbi.mizan.feature.transactions.domain.model.Transaction
import dev.esbi.mizan.feature.transactions.domain.model.TransactionFilter
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Transactions feature
 */
interface TransactionsRepository {
    
    /**
     * Observe all transactions with optional filter and search
     */
    fun observeTransactions(
        filter: TransactionFilter = TransactionFilter.ALL,
        searchQuery: String = ""
    ): Flow<List<Transaction>>
    
    /**
     * Get transaction by ID
     */
    suspend fun getTransactionById(id: String): Transaction?
    
    /**
     * Refresh transactions data
     */
    suspend fun refreshTransactions()
}
