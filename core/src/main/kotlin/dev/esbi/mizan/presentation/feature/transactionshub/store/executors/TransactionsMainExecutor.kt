package dev.esbi.mizan.presentation.feature.transactionshub.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CategoryRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Action
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Intent
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Label
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.Message
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore.State
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class TransactionsMainExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeAction(action: Action) {
        when(action){
            is Action.LoadData -> {}
        }
    }
    override fun executeIntent(intent: Intent) {
        when(intent){
            else -> Unit
        }
    }

}
