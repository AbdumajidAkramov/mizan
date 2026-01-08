package dev.esbi.mizan.feature.transactions.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.transactions.domain.model.Transaction
import dev.esbi.mizan.feature.transactions.domain.model.TransactionFilter
import dev.esbi.mizan.feature.transactions.domain.model.TransactionGroup
import dev.esbi.mizan.feature.transactions.domain.model.TransactionSummary
import dev.esbi.mizan.feature.transactions.domain.model.TransactionType
import dev.esbi.mizan.feature.transactions.domain.usecase.ObserveTransactionsUseCase
import dev.esbi.mizan.feature.transactions.domain.usecase.RefreshTransactionsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Factory for creating TransactionsStore instances
 * Implements filtering, search, and date grouping logic
 */
class TransactionsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeTransactionsUseCase: ObserveTransactionsUseCase,
    private val refreshTransactionsUseCase: RefreshTransactionsUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): TransactionsStore =
        object : TransactionsStore, Store<TransactionsStore.Intent, TransactionsStore.State, TransactionsStore.Label> by storeFactory.create(
            name = "TransactionsStore",
            initialState = TransactionsStore.State(isLoading = true),
            bootstrapper = SimpleBootstrapper(TransactionsStore.Action.Init),
            executorFactory = {
                ExecutorImpl(
                    observeTransactionsUseCase,
                    refreshTransactionsUseCase,
                    mainDispatcher
                )
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class DataLoaded(val summary: TransactionSummary) : Msg
        data class Error(val message: String) : Msg
        data class FilterChanged(val filter: TransactionFilter) : Msg
        data class SearchQueryChanged(val query: String) : Msg
    }

    private class ExecutorImpl(
        private val observeTransactionsUseCase: ObserveTransactionsUseCase,
        private val refreshTransactionsUseCase: RefreshTransactionsUseCase,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<TransactionsStore.Intent, TransactionsStore.Action, TransactionsStore.State, Msg, TransactionsStore.Label>(
        mainContext = mainDispatcher
    ) {

        override fun executeAction(action: TransactionsStore.Action) {
            when (action) {
                TransactionsStore.Action.Init -> {
                    observeTransactions()
                    refresh()
                }
            }
        }

        override fun executeIntent(intent: TransactionsStore.Intent) {
            when (intent) {
                is TransactionsStore.Intent.Refresh -> refresh()
                is TransactionsStore.Intent.Retry -> retry()
                is TransactionsStore.Intent.SetFilter -> {
                    dispatch(Msg.FilterChanged(intent.filter))
                    observeTransactions()
                }
                is TransactionsStore.Intent.SetSearchQuery -> {
                    dispatch(Msg.SearchQueryChanged(intent.query))
                    observeTransactions()
                }
                is TransactionsStore.Intent.SelectTransaction -> {
                    publish(TransactionsStore.Label.NavigateToDetail(intent.transactionId))
                }
            }
        }

        private fun observeTransactions() {
            val currentState = state()
            observeTransactionsUseCase(
                filter = currentState.selectedFilter,
                searchQuery = currentState.searchQuery
            )
                .onEach { transactions ->
                    val summary = processTransactions(transactions)
                    dispatch(Msg.DataLoaded(summary))
                }
                .catch { e ->
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                }
                .launchIn(scope)
        }

        private fun refresh() {
            dispatch(Msg.Loading)
            scope.launch {
                try {
                    refreshTransactionsUseCase()
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                    publish(TransactionsStore.Label.ShowError(e.message ?: "Failed to refresh"))
                }
            }
        }

        private fun retry() {
            observeTransactions()
            refresh()
        }

        private fun processTransactions(transactions: List<Transaction>): TransactionSummary {
            val totalIncome = transactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }

            val totalExpense = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            val grouped = groupTransactionsByDate(transactions)

            return TransactionSummary(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                groupedTransactions = grouped,
                totalTransactions = transactions.size
            )
        }

        private fun groupTransactionsByDate(transactions: List<Transaction>): List<TransactionGroup> {
            val groups = mutableMapOf<String, MutableList<Transaction>>()
            val calendar = Calendar.getInstance()
            val today = calendar.apply { 
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val yesterday = calendar.apply { 
                add(Calendar.DAY_OF_YEAR, -1)
            }.timeInMillis

            transactions.forEach { transaction ->
                val dateLabel = when {
                    transaction.timestamp >= today -> "Today"
                    transaction.timestamp >= yesterday -> "Yesterday"
                    else -> {
                        val date = Date(transaction.timestamp)
                        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(date)
                    }
                }

                groups.getOrPut(dateLabel) { mutableListOf() }.add(transaction)
            }

            return groups.map { (label, txns) ->
                TransactionGroup(
                    dateLabel = label,
                    transactions = txns.sortedByDescending { it.timestamp }
                )
            }.sortedBy { group ->
                when (group.dateLabel) {
                    "Today" -> 0
                    "Yesterday" -> 1
                    else -> 2
                }
            }
        }
    }

    private object ReducerImpl : Reducer<TransactionsStore.State, Msg> {
        override fun TransactionsStore.State.reduce(msg: Msg): TransactionsStore.State =
            when (msg) {
                is Msg.Loading -> copy(isLoading = true, error = null)
                is Msg.DataLoaded -> copy(
                    isLoading = false,
                    summary = msg.summary,
                    error = null
                )
                is Msg.Error -> copy(isLoading = false, error = msg.message)
                is Msg.FilterChanged -> copy(selectedFilter = msg.filter)
                is Msg.SearchQueryChanged -> copy(searchQuery = msg.query)
            }
    }
}
