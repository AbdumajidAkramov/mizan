package dev.esbi.mizan.feature.goals.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.presentation.feature.goals.presentation.store.GoalsStore
import dev.esbi.mizan.presentation.feature.goals.presentation.store.GoalsStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoalsViewModel @Inject constructor(
    private val storeFactory: GoalsStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<GoalsStore.State> = store.states
    val labels: Flow<GoalsStore.Label> = store.labels

    fun onIntent(intent: GoalsStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
