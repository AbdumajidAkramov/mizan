package dev.esbi.mizan.feature.accounts.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.feature.accounts.presentation.store.AccountsStore.Intent
import dev.esbi.mizan.feature.accounts.presentation.store.AccountsStore.Label
import dev.esbi.mizan.feature.accounts.presentation.store.AccountsStore.State

interface AccountsStore : Store<Intent, State, Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data class Search(val query: String) : Intent
        data object AddAccountClicked : Intent
        data class EditAccountClicked(val id: Long) : Intent
        data class ArchiveAccountClicked(val id: Long) : Intent
        data object NavigateBackClicked : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val totalBalance: Double = 0.0,
        val baseCurrencyCode: String = "UZS",
        val groupedAccounts: List<AccountGroupUIModel> = emptyList(),
        val searchQuery: String = "",
        val error: String? = null
    )

    data class AccountGroupUIModel(
        val id: String,
        val label: String,
        val accounts: List<Account>
    )

    sealed interface Label {
        data object NavigateBack : Label
        data object NavigateToAddAccount : Label
        data class NavigateToEditAccount(val accountId: Long) : Label
        data class ShowError(val message: String) : Label
    }
}
