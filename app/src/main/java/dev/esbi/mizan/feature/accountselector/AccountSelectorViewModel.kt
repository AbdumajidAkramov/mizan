package dev.esbi.mizan.feature.accountselector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.accountselector.store.AccountSelectorStore
import dev.esbi.mizan.feature.accountselector.store.AccountSelectorStoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSelectorViewModel @Inject constructor(
    private val storeFactory: AccountSelectorStoreFactory
) : ViewModel() {

    private val store: AccountSelectorStore by lazy {
        storeFactory.create()
    }

    val state: Flow<AccountSelectorStore.State> = store.states

    val labels: Flow<AccountSelectorStore.Label> = store.labels

    init {
        // Load accounts immediately after initialization
        viewModelScope.launch {
            store.accept(AccountSelectorStore.Intent.LoadAccounts)
        }
    }

    fun onIntent(intent: AccountSelectorStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        if (store.isDisposed.not()) {
            store.dispose()
        }
    }
}
