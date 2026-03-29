package dev.esbi.mizan.feature.accountmanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dev.esbi.mizan.feature.accountmanagement.store.AccountManagementStore
import dev.esbi.mizan.feature.accountmanagement.store.AccountManagementStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for Account Management Screen
 */
class AccountManagementViewModel @Inject constructor(
    storeFactory: AccountManagementStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<AccountManagementStore.State> = store.stateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AccountManagementStore.State()
        )

    val labels = store.labels

    fun onLoadAccounts() {
        store.accept(AccountManagementStore.Intent.LoadAccounts)
    }

    fun onSaveAccount(account: AccountManagementStore.AccountItem) {
        store.accept(AccountManagementStore.Intent.SaveAccount(account))
    }

    fun onDeleteAccount(id: Long) {
        store.accept(AccountManagementStore.Intent.DeleteAccount(id))
    }

    fun onConfirmDeleteAccount(id: Long) {
        store.accept(AccountManagementStore.Intent.ConfirmDeleteAccount(id))
    }

    fun onArchiveAccount(id: Long) {
        store.accept(AccountManagementStore.Intent.ArchiveAccount(id))
    }

    fun onOpenAddAccountSheet() {
        store.accept(AccountManagementStore.Intent.OpenAddAccountSheet)
    }

    fun onOpenEditAccountSheet(account: AccountManagementStore.AccountItem) {
        store.accept(AccountManagementStore.Intent.OpenEditAccountSheet(account))
    }

    fun onCloseAddEditSheet() {
        store.accept(AccountManagementStore.Intent.CloseAddEditSheet)
    }

    fun onSearchAccounts(query: String) {
        store.accept(AccountManagementStore.Intent.SearchAccounts(query))
    }

    fun onBackClicked() {
        store.accept(AccountManagementStore.Intent.BackClicked)
    }

    override fun onCleared() {
        store.dispose()
        super.onCleared()
    }
}
