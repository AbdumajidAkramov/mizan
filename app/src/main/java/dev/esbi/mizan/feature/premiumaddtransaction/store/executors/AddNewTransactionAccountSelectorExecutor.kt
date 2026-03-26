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
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.State

internal class AddNewTransactionAccountSelectorExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository,
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.InitAccounts -> {
                fetchAccounts()
            }


            else -> Unit
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {

            is Intent.UpdateSelectedAccount -> {
                dispatch(Message.UpdateSelectedAccount(intent.account))
                forward(Action.CheckAndConfirm)
                dispatch(Message.UpdateSelectAccountsBottomSheet(false))
            }

            is Intent.UpdateTargetAccount -> {
                dispatch(Message.UpdateTargetAccount(intent.account))
                forward(Action.CheckAndConfirm)
                dispatch(Message.UpdateTargetAccountsBottomSheet(false))
            }

            is Intent.OpenAccountManageScreen -> {
                publish(Label.NavigateToAccountManage)
            }
            is Intent.OpenAccountsBottomSheet -> {
                dispatch(Message.UpdatePad(State.Pad.AccountSelector))
            }

            else -> Unit
        }
    }

    private fun fetchAccounts() {
        accountRepository.observeAccounts()
            .onEach { accounts ->
                dispatch(Message.UpdateAccounts(accounts))
                // Auto-select first account if none selected
                if (state().selectedAccount == null && accounts.isNotEmpty()) {
                    // Prefer CASH type, otherwise first account
                    val defaultAccount =
                        accounts.find { it.type == CASH } ?: accounts.firstOrNull()
                    dispatch(Message.UpdateSelectedAccount(defaultAccount))
                }
            }
            .launchIn(scope)
    }
}
