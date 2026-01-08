package dev.esbi.mizan.feature.transactions.domain.usecase

import dev.esbi.mizan.feature.transactions.domain.model.Transaction
import dev.esbi.mizan.feature.transactions.domain.model.TransactionFilter
import dev.esbi.mizan.feature.transactions.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe transactions with filtering and search
 */
class ObserveTransactionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    operator fun invoke(
        filter: TransactionFilter = TransactionFilter.ALL,
        searchQuery: String = ""
    ): Flow<List<Transaction>> {
        return repository.observeTransactions(filter, searchQuery)
    }
}
