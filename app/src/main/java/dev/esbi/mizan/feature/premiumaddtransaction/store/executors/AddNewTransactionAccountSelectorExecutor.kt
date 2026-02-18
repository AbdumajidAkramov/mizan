package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.model.Account.Type.CASH
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class AddNewTransactionAccountSelectorExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository,
) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeAction(action: AddNewTransactionStore.Action) {
        when (action) {
            is AddNewTransactionStore.Action.InitAccounts -> {
                fetchAccounts()
            }

            else -> Unit
        }
    }

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {

            is AddNewTransactionStore.Intent.UpdateSelectedAccount -> {
                dispatch(AddNewTransactionStore.Message.UpdateSelectedAccount(intent.account))
                forward(AddNewTransactionStore.Action.CheckAndConfirm)
            }

            is AddNewTransactionStore.Intent.UpdateTargetAccount -> {
                dispatch(AddNewTransactionStore.Message.UpdateTargetAccount(intent.account))
                forward(AddNewTransactionStore.Action.CheckAndConfirm)
            }

            is AddNewTransactionStore.Intent.OpenAccountManageScreen -> {
                publish(
                    AddNewTransactionStore.Label.NavigateToAccountManage
                )
            }

            else -> Unit
        }
    }

    private fun fetchAccounts() {
        accountRepository.observeAccounts()
            .onEach { accounts ->
                dispatch(AddNewTransactionStore.Message.UpdateAccounts(accounts))
                // Auto-select first account if none selected
                if (state().selectedAccount == null && accounts.isNotEmpty()) {
                    // Prefer CASH type, otherwise first account
                    val defaultAccount =
                        accounts.find { it.type == CASH } ?: accounts.firstOrNull()
                    dispatch(AddNewTransactionStore.Message.UpdateSelectedAccount(defaultAccount))
                }
            }
            .launchIn(scope)
    }
}
