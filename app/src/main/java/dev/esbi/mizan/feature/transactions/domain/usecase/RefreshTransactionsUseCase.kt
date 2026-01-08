package dev.esbi.mizan.feature.transactions.domain.usecase

import dev.esbi.mizan.feature.transactions.domain.repository.TransactionsRepository
import javax.inject.Inject

/**
 * Use case to refresh transactions data
 */
class RefreshTransactionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke() {
        repository.refreshTransactions()
    }
}
