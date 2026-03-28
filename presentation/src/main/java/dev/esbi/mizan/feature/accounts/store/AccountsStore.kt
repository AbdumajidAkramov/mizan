package dev.esbi.mizan.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account

interface AccountsStore :
    Store<AccountsStore.Intent, AccountsStore.State, AccountsStore.Label> {

    data class AccountUIModel(
        val id: Long,
        val name: String,
        val subtitle: String?,
        val balance: Double,
        val currencyCode: String,
        val balanceInBase: Double,
        val iconName: String?,
        val color: String?
    )

    data class State(
        val isLoading: Boolean = false,
        val groupedAccounts: Map<Account.Type, List<AccountUIModel>> = emptyMap(),
        val totalBalance: Double = 0.0,
        val primaryCurrency: String = "UZS",
        val searchQuery: String = "",
        val error: String? = null
    ) {
        val filteredGroupedAccounts: Map<Account.Type, List<AccountUIModel>>
            get() = if (searchQuery.isBlank()) {
                groupedAccounts
            } else {
                groupedAccounts.mapValues { (_, accounts) ->
                    accounts.filter { it.name.contains(searchQuery, ignoreCase = true) }
                }.filter { (_, accounts) -> accounts.isNotEmpty() }
            }

        val totalAccountCount: Int
            get() = groupedAccounts.values.sumOf { it.size }
    }

    sealed interface Intent {
        data object InitialLoad : Intent
        data object Refresh : Intent
        data class SearchQueryChanged(val query: String) : Intent
        data object AddAccount : Intent
        data class AccountClicked(val accountId: Long) : Intent
    }

    sealed interface Label {
        data object NavigateToAddAccount : Label
        data class NavigateToAccountDetail(val accountId: Long) : Label
        data class ShowError(val message: String) : Label
    }

    sealed interface Message {
        data class AccountsLoaded(
            val groupedAccounts: Map<Account.Type, List<AccountUIModel>>,
            val totalBalance: Double
        ) : Message
        data class LoadingChanged(val isLoading: Boolean) : Message
        data class ErrorChanged(val error: String?) : Message
        data class SearchQueryChanged(val query: String) : Message
    }
}
