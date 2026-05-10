package dev.esbi.mizan.presentation.feature.transactionshub.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Action
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Intent
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Label
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Message
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.State
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class DailyTransactionsExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when(intent){
            else -> Unit
        }
    }
}
