package dev.esbi.mizan.presentation.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.Reducer

/**
 * Reducer for AccountManagement - handles state updates
 */
internal class AccountsReducer : Reducer<AccountsStore.State, AccountsStore.Message> {

    override fun AccountsStore.State.reduce(
        msg: AccountsStore.Message
    ): AccountsStore.State {
        return when (msg) {
            is AccountsStore.Message.MainCurrencyChanged -> copy(
                baseCurrency = msg.value
            )

            is AccountsStore.Message.UpdateErrorValue -> copy(error = msg.value)
            is AccountsStore.Message.AccountsLoaded -> {
                copy(
                    accounts = msg.accounts,
                    groups = msg.groups,
                    totalBalance = msg.totalBalance,
                    error = null
                )
            }

            is AccountsStore.Message.AccountSaved -> {
                val existingIndex = accounts.indexOfFirst { it.id == msg.account.id }
                val updatedAccounts = if (existingIndex >= 0) {
                    accounts.toMutableList().apply {
                        this[existingIndex] = msg.account
                    }
                } else {
                    accounts + msg.account
                }
                val newTotal = updatedAccounts
                    .filter { !it.excludeFromTotal }
                    .sumOf { it.balance }
                copy(
                    accounts = updatedAccounts,
                    totalBalance = newTotal
                )
            }

            is AccountsStore.Message.AccountDeleted -> {
                val updatedAccounts = accounts.filter { it.id != msg.id }
                val newTotal = updatedAccounts
                    .filter { !it.excludeFromTotal }
                    .sumOf { it.balance }
                copy(
                    accounts = updatedAccounts,
                    totalBalance = newTotal
                )
            }

            is AccountsStore.Message.AccountArchived -> {
                val updatedAccounts = accounts.filter { it.id != msg.id }
                val newTotal = updatedAccounts
                    .filter { !it.excludeFromTotal }
                    .sumOf { it.balance }
                copy(
                    accounts = updatedAccounts,
                    totalBalance = newTotal
                )
            }

            is AccountsStore.Message.SearchQueryChanged -> {
                copy(searchQuery = msg.query)
            }

            is AccountsStore.Message.LoadingChanged -> {
                copy(isLoading = msg.isLoading)
            }

            is AccountsStore.Message.ErrorOccurred -> {
                copy(error = msg.error)
            }
        }
    }
}
