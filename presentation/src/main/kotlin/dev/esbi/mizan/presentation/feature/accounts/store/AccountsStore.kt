package dev.esbi.mizan.presentation.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Currency
import java.math.BigDecimal

/**
 * MVI Store for Account Management Screen
 */
interface AccountsStore : Store<
        AccountsStore.Intent,
        AccountsStore.State,
        AccountsStore.Label> {

    /**
     * UI State
     */
    data class State(
        val accounts: List<AccountItem> = emptyList(),
        val groups: List<AccountGroupItem> = emptyList(),
        val totalBalance: BigDecimal = BigDecimal.ZERO,
        val isLoading: Boolean = true,
        val error: String? = null,
        val editingAccount: AccountItem? = null,
        val searchQuery: String = "",
        val baseCurrency: Currency? = Currency.UZS,
        val monthlyChange: BigDecimal = BigDecimal.ZERO,
        val monthlyChangePercent: Double = 0.0,
    )

    /**
     * Account item for UI display (no type/color/icon)
     */
    data class AccountItem(
        val id: Long,
        val groupId: Long,
        val groupName: String = "",
        val name: String,
        val balance: BigDecimal,
        val currencyCode: String,
        val isArchived: Boolean = false,
        val excludeFromTotal: Boolean = false,
        val description: String? = null
    )

    /**
     * Account group for categorization (from DB)
     */
    data class AccountGroupItem(
        val id: Long,
        val name: String,
        val isSystemGroup: Boolean = false,
        val accounts: List<AccountItem> = emptyList()
    )

    sealed interface Action {
        class FetchMainCurrency : Action
    }

    /**
     * User intents
     */
    sealed interface Intent {
        data object LoadAccounts : Intent
        class CloseToast : Intent
        data class SaveAccount(val account: AccountItem) : Intent
        data class ArchiveAccount(val id: Long) : Intent
        data class SearchAccounts(val query: String) : Intent
        data object BackClicked : Intent

        class OpenAddNewAccount : Intent
        class OpenEditAccount(val accountId: Long) : Intent
    }

    /**
     * Internal messages for reducer
     */
    sealed interface Message {
        data class AccountsLoaded(
            val accounts: List<AccountItem>,
            val groups: List<AccountGroupItem>,
            val totalBalance: BigDecimal
        ) : Message

        class UpdateErrorValue(val value: String? = null) : Message

        data class AccountSaved(val account: AccountItem) : Message
        data class AccountDeleted(val id: Long) : Message
        data class AccountArchived(val id: Long) : Message
        data class SearchQueryChanged(val query: String) : Message
        data class LoadingChanged(val isLoading: Boolean) : Message
        data class ErrorOccurred(val error: String?) : Message
        class MainCurrencyChanged(val value: Currency?) : Message
    }

    /**
     * Labels for side effects (navigation, etc.)
     */
    sealed interface Label {
        data object NavigateBack : Label
        class NavigateToAddNewAccount(val accountId: Long? = null) : Label
    }
}
