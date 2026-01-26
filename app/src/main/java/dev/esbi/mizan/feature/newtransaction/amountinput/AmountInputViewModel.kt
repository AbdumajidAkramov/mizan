package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.newtransaction.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.Intent
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.Label
import dev.esbi.mizan.feature.newtransaction.store.AmountInputStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AmountInputViewModel @Inject constructor(
    storeFactory: AmountInputStoreFactory
) : ViewModel() {

    private val store: NewTransactionStore = storeFactory.create()

    val state: Flow<AmountInputState> = store.states
    val labels: Flow<Label> = store.labels

    fun onIntent(intent: Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
