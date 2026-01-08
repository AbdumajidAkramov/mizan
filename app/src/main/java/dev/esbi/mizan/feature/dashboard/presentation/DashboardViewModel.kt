package dev.esbi.mizan.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val storeFactory: DashboardStoreFactory
) : ViewModel() {

    private val store: DashboardStore = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<DashboardStore.State> = store.stateFlow

    private val _labels = MutableSharedFlow<DashboardStore.Label>()
    val labels = _labels.asSharedFlow()

    init {
        viewModelScope.launch {
            store.labels.collect { label ->
                _labels.emit(label)
            }
        }
    }

    fun onIntent(intent: DashboardStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        store.dispose()
        super.onCleared()
    }
}
