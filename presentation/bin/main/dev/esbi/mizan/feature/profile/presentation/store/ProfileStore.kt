package dev.esbi.mizan.feature.profile.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.profile.AppSettings
import dev.esbi.mizan.domain.model.profile.SettingAction
import dev.esbi.mizan.domain.model.profile.UserProfile

/**
 * MVIKotlin Store for Profile Screen
 * Manages user profile and settings
 */
interface ProfileStore : Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data object ToggleDarkMode : Intent
        data object Logout : Intent
        data class OnSettingClick(val action: SettingAction) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val profile: UserProfile? = null,
        val settings: AppSettings = AppSettings(),
        val error: String? = null
    )

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data object NavigateToLogin : Label
        data class NavigateToSetting(val action: SettingAction) : Label
    }
}
