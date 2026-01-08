package dev.esbi.mizan.feature.budget.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.budget.presentation.store.BudgetStore
import dev.esbi.mizan.feature.budget.presentation.store.BudgetStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel for Budget Screen
 * Wraps MVIKotlin store for Compose integration
 */
class BudgetViewModel @Inject constructor(
    private val storeFactory: BudgetStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<BudgetStore.State> = store.states
    val labels: Flow<BudgetStore.Label> = store.labels

    fun onIntent(intent: BudgetStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
