package dev.esbi.mizan.feature.subscriptions.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.subscriptions.presentation.store.SubscriptionsStore
import dev.esbi.mizan.feature.subscriptions.presentation.store.SubscriptionsStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscriptionsViewModel @Inject constructor(
    private val storeFactory: SubscriptionsStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<SubscriptionsStore.State> = store.states
    val labels: Flow<SubscriptionsStore.Label> = store.labels

    fun onIntent(intent: SubscriptionsStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
