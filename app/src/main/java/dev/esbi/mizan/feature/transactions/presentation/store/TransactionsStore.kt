package dev.esbi.mizan.feature.transactions.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.transactions.domain.model.TransactionFilter
import dev.esbi.mizan.feature.transactions.domain.model.TransactionSummary

/**
 * MVIKotlin Store for Transactions Screen
 * Manages filtering, search, and transaction display
 */
interface TransactionsStore : Store<TransactionsStore.Intent, TransactionsStore.State, TransactionsStore.Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data object Refresh : Intent
        data object Retry : Intent
        data class SetFilter(val filter: TransactionFilter) : Intent
        data class SetSearchQuery(val query: String) : Intent
        data class SelectTransaction(val transactionId: String) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val summary: TransactionSummary? = null,
        val error: String? = null,
        val selectedFilter: TransactionFilter = TransactionFilter.ALL,
        val searchQuery: String = ""
    )

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data class NavigateToDetail(val transactionId: String) : Label
    }
}
