package dev.esbi.mizan.feature.transfer.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.transfer.presentation.store.TransferStore
import dev.esbi.mizan.feature.transfer.presentation.store.TransferStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransferViewModel @Inject constructor(
    private val storeFactory: TransferStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<TransferStore.State> = store.states
    val labels: Flow<TransferStore.Label> = store.labels

    fun onIntent(intent: TransferStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
