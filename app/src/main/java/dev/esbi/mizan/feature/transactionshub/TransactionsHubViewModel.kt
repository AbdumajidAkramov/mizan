package dev.esbi.mizan.feature.transactionshub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dev.esbi.mizan.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.feature.transactionshub.store.TransactionsHubStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for TransactionsHub screen
 */
class TransactionsHubViewModel @Inject constructor(
    storeFactory: TransactionsHubStoreFactory
) : ViewModel() {

    private val store: TransactionsHubStore = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<TransactionsHubStore.State> = store.stateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = TransactionsHubStore.State()
        )

    val labels: Flow<TransactionsHubStore.Label> = store.labels

    fun onIntent(intent: TransactionsHubStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        store.dispose()
        super.onCleared()
    }
}
