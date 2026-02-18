package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import javax.inject.Inject

internal class AddNewTransactionAmountInputExecutor @Inject constructor(

) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            else -> Unit
        }
    }
}
