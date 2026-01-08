package dev.esbi.mizan.feature.statistics.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.statistics.presentation.store.StatisticsStore
import dev.esbi.mizan.feature.statistics.presentation.store.StatisticsStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel for Statistics Screen
 * Wraps MVIKotlin store for Compose integration
 */
class StatisticsViewModel @Inject constructor(
    private val storeFactory: StatisticsStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<StatisticsStore.State> = store.states
    val labels: Flow<StatisticsStore.Label> = store.labels

    fun onIntent(intent: StatisticsStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
