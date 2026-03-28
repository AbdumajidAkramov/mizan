package dev.esbi.mizan.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dev.esbi.mizan.feature.accounts.store.AccountsStore
import dev.esbi.mizan.feature.accounts.store.AccountsStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class AccountsViewModel @Inject constructor(
    storeFactory: AccountsStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<AccountsStore.State> = store.stateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AccountsStore.State()
        )

    val labels = store.labels

    fun onRefresh() = store.accept(AccountsStore.Intent.Refresh)
    fun onSearchQueryChanged(query: String) = store.accept(AccountsStore.Intent.SearchQueryChanged(query))
    fun onAddAccount() = store.accept(AccountsStore.Intent.AddAccount)
    fun onAccountClicked(accountId: Long) = store.accept(AccountsStore.Intent.AccountClicked(accountId))

    override fun onCleared() {
        store.dispose()
        super.onCleared()
    }
}
