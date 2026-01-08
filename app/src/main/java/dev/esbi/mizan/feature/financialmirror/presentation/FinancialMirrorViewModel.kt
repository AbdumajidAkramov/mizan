package dev.esbi.mizan.feature.financialmirror.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStore
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStoreFactory
import kotlinx.coroutines.flow.Flow
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
