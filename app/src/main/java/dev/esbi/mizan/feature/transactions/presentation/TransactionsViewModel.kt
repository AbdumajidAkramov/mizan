package dev.esbi.mizan.feature.transactions.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.transactions.presentation.store.TransactionsStore
import dev.esbi.mizan.feature.transactions.presentation.store.TransactionsStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel for Transactions Screen
 * Wraps MVIKotlin store for Compose integration
 */
class TransactionsViewModel @Inject constructor(
    private val storeFactory: TransactionsStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<TransactionsStore.State> = store.states
    val labels: Flow<TransactionsStore.Label> = store.labels

    fun onIntent(intent: TransactionsStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
