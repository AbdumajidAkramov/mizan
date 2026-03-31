package dev.esbi.mizan.presentation.feature.accountmanagement.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Currency

/**
 * MVI Store for Account Management Screen
 */
interface AccountManagementStore : Store<
        AccountManagementStore.Intent,
        AccountManagementStore.State,
        AccountManagementStore.Label> {

    /**
     * UI State
     */
    data class State(
        val accounts: List<AccountItem> = emptyList(),
        val groups: List<AccountGroupItem> = emptyList(),
        val totalBalance: Double = 0.0,
        val isLoading: Boolean = true,
        val error: String? = null,
        val isAddEditSheetVisible: Boolean = false,
        val editingAccount: AccountItem? = null,
        val searchQuery: String = "",
        val baseCurrency: Currency? = Currency.UZS,
        val monthlyChange: Double = 0.0,
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
        val balance: Double,
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
        data class SaveAccount(val account: AccountItem) : Intent
        data class DeleteAccount(val id: Long) : Intent
        data class ConfirmDeleteAccount(val id: Long) : Intent
        data class ArchiveAccount(val id: Long) : Intent
        data object OpenAddAccountSheet : Intent
        data class OpenEditAccountSheet(val account: AccountItem) : Intent
        data object CloseAddEditSheet : Intent
        data class SearchAccounts(val query: String) : Intent
        data object BackClicked : Intent
    }

    /**
     * Internal messages for reducer
     */
    sealed interface Message {
        data class AccountsLoaded(
            val accounts: List<AccountItem>,
            val groups: List<AccountGroupItem>,
            val totalBalance: Double
        ) : Message

        data class AccountSaved(val account: AccountItem) : Message
        data class AccountDeleted(val id: Long) : Message
        data class AccountArchived(val id: Long) : Message
        data class EditSheetShown(val account: AccountItem?) : Message
        data object EditSheetHidden : Message
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
        data class ShowDeleteConfirmation(val account: AccountItem) : Label
        data class ShowError(val message: String) : Label
    }
}
