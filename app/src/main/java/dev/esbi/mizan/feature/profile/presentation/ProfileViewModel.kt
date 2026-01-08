package dev.esbi.mizan.feature.profile.presentation

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.profile.presentation.store.ProfileStore
import dev.esbi.mizan.feature.profile.presentation.store.ProfileStoreFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel for Profile Screen
 * Wraps MVIKotlin store for Compose integration
 */
class ProfileViewModel @Inject constructor(
    private val storeFactory: ProfileStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    val state: Flow<ProfileStore.State> = store.states
    val labels: Flow<ProfileStore.Label> = store.labels

    fun onIntent(intent: ProfileStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
