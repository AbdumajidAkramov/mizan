package dev.esbi.mizan.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for Account Management Screen
 */
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

    fun onIntent(intent: AccountsStore.Intent) {
        store.accept(intent = intent)
    }

    override fun onCleared() {
        store.dispose()
        super.onCleared()
    }
}
