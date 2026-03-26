package dev.esbi.mizan.feature.accountselector.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.domain.model.Account

object AccountSelectorReducer :
    Reducer<AccountSelectorStore.State, AccountSelectorReducer.Message> {

    sealed interface Message {
        class LoadingChanged(val isLoading: Boolean) : Message
        class AccountsLoaded(val accounts: List<Account>) : Message
        class AccountSelected(val accountId: Long) : Message
        class ErrorChanged(val error: String?) : Message
    }

    override fun AccountSelectorStore.State.reduce(msg: Message): AccountSelectorStore.State {
        return when (msg) {
            is Message.LoadingChanged -> copy(
                isLoading = msg.isLoading,
                error = if (msg.isLoading) null else error
            )

            is Message.AccountsLoaded -> copy(
                isLoading = false,
                accounts = msg.accounts,
                error = null
            )

            is Message.AccountSelected -> copy(
                selectedAccountId = msg.accountId
            )

            is Message.ErrorChanged -> copy(
                isLoading = false,
                error = msg.error
            )
        }
    }
}
