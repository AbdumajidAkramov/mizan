package dev.esbi.mizan.feature.profile.data.repository

import dev.esbi.mizan.data.settings.AppSettingsManager
import dev.esbi.mizan.feature.profile.domain.model.AppSettings
import dev.esbi.mizan.feature.profile.domain.model.UserProfile
import dev.esbi.mizan.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ProfileRepository
 * Manages user profile and app settings
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val settingsManager: AppSettingsManager
) : ProfileRepository {

    private val _profile = MutableStateFlow(generateMockProfile())
    private val _settings = MutableStateFlow(AppSettings())

    override fun observeProfile(): Flow<UserProfile> = _profile.asStateFlow()

    override fun observeSettings(): Flow<AppSettings> = _settings.asStateFlow()

    override suspend fun updateProfile(profile: UserProfile) {
        _profile.value = profile
    }

    override suspend fun updateSettings(settings: AppSettings) {
        _settings.value = settings
    }

    override suspend fun toggleDarkMode() {
        settingsManager.toggleDarkMode()
        _settings.value = _settings.value.copy(
            isDarkMode = !_settings.value.isDarkMode
        )
    }

    override suspend fun logout() {
        // In a real app, this would clear auth tokens and navigate to login
    }

    private fun generateMockProfile(): UserProfile {
        return UserProfile(
            id = "user_001",
            name = "John Doe",
            email = "john.doe@email.com",
            phoneNumber = "+1 (555) 123-4567",
            memberSince = "Jan 2024",
            totalTransactions = 156,
            totalIncome = 12500.0,
            totalExpense = 8200.0,
            totalSaved = 4300.0,
            avatarInitials = "JD"
        )
    }
}
