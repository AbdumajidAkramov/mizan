package dev.esbi.mizan.presentation.feature.accountmanagement.store

import com.arkivanov.mvikotlin.core.store.Reducer

/**
 * Reducer for AccountManagement - handles state updates
 */
internal class AccountManagementReducer :
    Reducer<AccountManagementStore.State, AccountManagementStore.Message> {

    override fun AccountManagementStore.State.reduce(
        msg: AccountManagementStore.Message
    ): AccountManagementStore.State {
        return when (msg) {
            is AccountManagementStore.Message.MainCurrencyChanged -> copy(
                baseCurrency = msg.value
            )

            is AccountManagementStore.Message.AccountsLoaded -> {
                copy(
                    accounts = msg.accounts,
                    groups = msg.groups,
                    totalBalance = msg.totalBalance,
                    error = null
                )
            }

            is AccountManagementStore.Message.AccountSaved -> {
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

            is AccountManagementStore.Message.AccountDeleted -> {
                val updatedAccounts = accounts.filter { it.id != msg.id }
                val newTotal = updatedAccounts
                    .filter { !it.excludeFromTotal }
                    .sumOf { it.balance }
                copy(
                    accounts = updatedAccounts,
                    totalBalance = newTotal
                )
            }

            is AccountManagementStore.Message.AccountArchived -> {
                val updatedAccounts = accounts.filter { it.id != msg.id }
                val newTotal = updatedAccounts
                    .filter { !it.excludeFromTotal }
                    .sumOf { it.balance }
                copy(
                    accounts = updatedAccounts,
                    totalBalance = newTotal
                )
            }

            is AccountManagementStore.Message.EditSheetShown -> {
                copy(
                    isAddEditSheetVisible = true,
                    editingAccount = msg.account
                )
            }

            is AccountManagementStore.Message.EditSheetHidden -> {
                copy(
                    isAddEditSheetVisible = false,
                    editingAccount = null
                )
            }

            is AccountManagementStore.Message.SearchQueryChanged -> {
                copy(searchQuery = msg.query)
            }

            is AccountManagementStore.Message.LoadingChanged -> {
                copy(isLoading = msg.isLoading)
            }

            is AccountManagementStore.Message.ErrorOccurred -> {
                copy(error = msg.error)
            }
        }
    }
}
