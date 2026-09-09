package dev.esbi.mizan.features.addtransaction

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AmountInputViewModel @Inject constructor(
    private val addNewTransactionStoreFactory: AddNewTransactionStoreFactory,
    private val transactionId: Long? = null
) : ViewModel() {

    private val addNewTransactionStore: AddNewTransactionStore by lazy {
        addNewTransactionStoreFactory.create(transactionId)
    }

    val addNewTransactionState: Flow<AddNewTransactionStore.State> = addNewTransactionStore.states
    val addNewTransactionLabels: Flow<AddNewTransactionStore.Label> = addNewTransactionStore.labels

    init {
        addNewTransactionStore.init()
    }

    fun onNewTransactionStoreIntent(intent: AddNewTransactionStore.Intent) {
        addNewTransactionStore.accept(intent)
    }
}