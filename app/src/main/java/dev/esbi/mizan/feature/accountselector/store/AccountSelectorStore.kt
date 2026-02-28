package dev.esbi.mizan.feature.accountselector.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account

interface AccountSelectorStore :
    Store<AccountSelectorStore.Intent, AccountSelectorStore.State, AccountSelectorStore.Label> {

    data class State(
        val isLoading: Boolean = false,
        val accounts: List<Account> = emptyList(),
        val selectedAccountId: Long? = null,
        val error: String? = null
    ) {
        val selectedAccount: Account?
            get() = accounts.find { it.id == selectedAccountId }
    }

    sealed interface Intent {
        object LoadAccounts : Intent
        class SelectAccount(val account: Account) : Intent
        data object RetryLoad : Intent
    }

    sealed interface Label {
        class AccountSelected(val account: Account) : Label
        class ShowError(val message: String) : Label
    }
}
