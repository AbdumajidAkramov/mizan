package dev.esbi.mizan.feature.accounts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dev.esbi.mizan.feature.accounts.presentation.store.AccountsStore
import dev.esbi.mizan.feature.accounts.presentation.store.AccountsStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountsViewModel @Inject constructor(
    storeFactory: AccountsStoreFactory
) : ViewModel() {

    private val store: AccountsStore = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<AccountsStore.State> = store.stateFlow

    private val _labels = MutableSharedFlow<AccountsStore.Label>()
    val labels = _labels.asSharedFlow()

    init {
        viewModelScope.launch {
            store.labels.collect { label ->
                _labels.emit(label)
            }
        }
    }

    fun onIntent(intent: AccountsStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        store.dispose()
        super.onCleared()
    }
}
