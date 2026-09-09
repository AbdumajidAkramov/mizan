package dev.esbi.mizan.presentation.feature.addtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


class AddNewTransactionLoadExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val transactionId: Long? = null
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainDispatcher) {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.LoadTransaction -> {
                loadTransaction(transactionId)
            }

            else -> Unit
        }
    }

    private fun loadTransaction(transactionId: Long? = null) {
//        val transactionId = state().editingTransactionId ?: return
        transactionId ?: return
        scope.launch {
            dispatch(Message.UpdateLoading(true))
            dispatch(Message.UpdateError(null))

            try {
                // Fetch transaction from repository
                val transaction = transactionRepository.getTransactionById(transactionId)

                if (transaction != null) {
                    // Fetch related entities
                    val account = transaction.accountId?.let { accountRepository.getAccount(it) }
                    val targetAccount = transaction.targetAccountId?.let {
                        accountRepository.getAccount(it)
                    }

                    // Find category from state (categories will be loaded by InitCategories action)
                    val category = transaction.categoryId?.let { categoryId ->
                        state().allCategories.find { it.id == categoryId }
                    }

                    // Dispatch message to pre-fill state
                    dispatch(
                        Message.TransactionLoaded(
                            transaction = transaction,
                            account = account,
                            category = category,
                            targetAccount = targetAccount
                        )
                    )
                } else {
                    dispatch(Message.UpdateError("Transaction not found"))
                }
            } catch (e: Exception) {
                dispatch(Message.UpdateError("Failed to load transaction: ${e.message}"))
            } finally {
                dispatch(Message.UpdateLoading(false))
            }
        }
    }
}
