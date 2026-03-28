package dev.esbi.mizan.feature.profile.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.model.profile.AppSettings
import dev.esbi.mizan.domain.model.profile.SettingAction
import dev.esbi.mizan.domain.model.profile.UserProfile
import dev.esbi.mizan.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Factory for creating ProfileStore instances
 * Implements profile and settings management
 */
class ProfileStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val profileRepository: ProfileRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): ProfileStore =
        object : ProfileStore, Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> by storeFactory.create(
            name = "ProfileStore",
            initialState = ProfileStore.State(isLoading = true),
            bootstrapper = SimpleBootstrapper(ProfileStore.Action.Init),
            executorFactory = {
                ExecutorImpl(
                    profileRepository,
                    mainDispatcher
                )
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class ProfileLoaded(val profile: UserProfile) : Msg
        data class SettingsLoaded(val settings: AppSettings) : Msg
        data class Error(val message: String) : Msg
    }

    private class ExecutorImpl(
        private val profileRepository: ProfileRepository,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<ProfileStore.Intent, ProfileStore.Action, ProfileStore.State, Msg, ProfileStore.Label>(
        mainContext = mainDispatcher
    ) {

        override fun executeAction(action: ProfileStore.Action) {
            when (action) {
                ProfileStore.Action.Init -> {
                    observeProfile()
                    observeSettings()
                }
            }
        }

        override fun executeIntent(intent: ProfileStore.Intent) {
            when (intent) {
                is ProfileStore.Intent.ToggleDarkMode -> toggleDarkMode()
                is ProfileStore.Intent.Logout -> logout()
                is ProfileStore.Intent.OnSettingClick -> {
                    when (intent.action) {
                        SettingAction.ACCOUNT_MANAGEMENT -> {
                            publish(ProfileStore.Label.NavigateToAccounts)
                        }
                        else -> {
                            publish(ProfileStore.Label.NavigateToSetting(intent.action))
                        }
                    }
                }
            }
        }

        private fun observeProfile() {
            profileRepository.observeProfile()
                .onEach { profile ->
                    dispatch(Msg.ProfileLoaded(profile))
                }
                .catch { e ->
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                }
                .launchIn(scope)
        }

        private fun observeSettings() {
            profileRepository.observeSettings()
                .onEach { settings ->
                    dispatch(Msg.SettingsLoaded(settings))
                }
                .catch { e ->
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                }
                .launchIn(scope)
        }

        private fun toggleDarkMode() {
            scope.launch {
                try {
                    profileRepository.toggleDarkMode()
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Failed to toggle dark mode"))
                    publish(ProfileStore.Label.ShowError(e.message ?: "Failed to toggle dark mode"))
                }
            }
        }

        private fun logout() {
            scope.launch {
                try {
                    profileRepository.logout()
                    publish(ProfileStore.Label.NavigateToLogin)
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Failed to logout"))
                    publish(ProfileStore.Label.ShowError(e.message ?: "Failed to logout"))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<ProfileStore.State, Msg> {
        override fun ProfileStore.State.reduce(msg: Msg): ProfileStore.State =
            when (msg) {
                is Msg.Loading -> copy(isLoading = true, error = null)
                is Msg.ProfileLoaded -> copy(
                    isLoading = false,
                    profile = msg.profile,
                    error = null
                )
                is Msg.SettingsLoaded -> copy(
                    settings = msg.settings
                )
                is Msg.Error -> copy(isLoading = false, error = msg.message)
            }
    }
}
