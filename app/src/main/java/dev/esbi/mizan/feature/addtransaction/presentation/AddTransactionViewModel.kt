package dev.esbi.mizan.feature.addtransaction.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AddTransactionViewModel @Inject constructor(
    storeFactory: AddTransactionStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<AddTransactionStore.State> = store.states
    val labels: Flow<AddTransactionStore.Label> = store.labels

    fun onIntent(intent: AddTransactionStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
