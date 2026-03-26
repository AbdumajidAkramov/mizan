package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.newtransaction.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.store.AmountInputStoreFactory
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.Intent
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.Label
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AmountInputViewModel @Inject constructor(
    storeFactory: AmountInputStoreFactory,
    private val addNewTransactionStoreFactory: AddNewTransactionStoreFactory,
    private val transactionId: Long? = null
) : ViewModel() {

    private val addNewTransactionStore: AddNewTransactionStore by lazy {
        addNewTransactionStoreFactory.create(transactionId)
    }
    private val store: NewTransactionStore = storeFactory.create()

    val state: Flow<AmountInputState> = store.states
    val labels: Flow<Label> = store.labels

    val addNewTransactionState: Flow<AddNewTransactionStore.State> = addNewTransactionStore.states
    val addNewTransactionLabels: Flow<AddNewTransactionStore.Label> = addNewTransactionStore.labels

    init {
        addNewTransactionStore.init()
    }

    fun onIntent(intent: Intent) {
        store.accept(intent)
    }

    fun onNewTransactionStoreIntent(intent: AddNewTransactionStore.Intent) {
        addNewTransactionStore.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
