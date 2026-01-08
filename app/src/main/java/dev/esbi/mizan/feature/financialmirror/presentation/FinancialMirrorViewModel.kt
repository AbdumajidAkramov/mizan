package dev.esbi.mizan.feature.financialmirror.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStore
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Financial Mirror Screen
 * Wraps MVIKotlin store for Compose integration
 */
class FinancialMirrorViewModel @Inject constructor(
    private val storeFactory: FinancialMirrorStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<FinancialMirrorStore.State> = store.states
    val labels: Flow<FinancialMirrorStore.Label> = store.labels

    fun onIntent(intent: FinancialMirrorStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
