package dev.esbi.mizan.presentation.feature.accountselector.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.presentation.feature.accountselector.store.AccountSelectorReducer.Message
import dev.esbi.mizan.presentation.feature.accountselector.store.AccountSelectorStore.Intent
import dev.esbi.mizan.presentation.feature.accountselector.store.AccountSelectorStore.Label
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSelectorExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository
) : CoroutineExecutor<Intent, Unit, AccountSelectorStore.State, Message, Label>(
    mainContext = mainDispatcher
) {

    interface Factory {
        fun create(): AccountSelectorExecutor
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadAccounts -> {
                loadAccounts()
            }
            
            is Intent.SelectAccount -> {
                selectAccount(intent.account)
            }
            
            is Intent.RetryLoad -> {
                loadAccounts()
            }
        }
    }

    private fun loadAccounts() {
        dispatch(Message.LoadingChanged(true))
        
        scope.launch {
            accountRepository.observeAccounts()
                .catch { exception ->
                    dispatch(Message.ErrorChanged(exception.message ?: "Failed to load accounts"))
                    publish(Label.ShowError(exception.message ?: "Failed to load accounts"))
                }
                .collect { accounts ->
                    dispatch(Message.AccountsLoaded(accounts))
                }
        }
    }

    private fun selectAccount(account: dev.esbi.mizan.domain.model.Account) {
        dispatch(Message.AccountSelected(account.id))
        publish(Label.AccountSelected(account))
    }
}
