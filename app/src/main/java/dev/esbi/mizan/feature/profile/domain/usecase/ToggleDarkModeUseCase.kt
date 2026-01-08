package dev.esbi.mizan.feature.profile.domain.usecase

import dev.esbi.mizan.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to toggle dark mode
 */
class ToggleDarkModeUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() {
        repository.toggleDarkMode()
    }
}
