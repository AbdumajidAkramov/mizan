package dev.esbi.mizan.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.Reducer

internal class AccountsReducer : Reducer<AccountsStore.State, AccountsStore.Message> {

    override fun AccountsStore.State.reduce(msg: AccountsStore.Message): AccountsStore.State =
        when (msg) {
            is AccountsStore.Message.AccountsLoaded -> copy(
                groupedAccounts = msg.groupedAccounts,
                totalBalance = msg.totalBalance,
                isLoading = false,
                error = null
            )
            is AccountsStore.Message.LoadingChanged -> copy(isLoading = msg.isLoading)
            is AccountsStore.Message.ErrorChanged -> copy(error = msg.error, isLoading = false)
            is AccountsStore.Message.SearchQueryChanged -> copy(searchQuery = msg.query)
        }
}
