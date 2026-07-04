package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.profile.AppSettings
import dev.esbi.mizan.domain.model.profile.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Profile feature
 */
interface ProfileRepository {

    /**
     * Observe user profile
     */
    fun observeProfile(): Flow<UserProfile>

    /**
     * Observe app settings
     */
    fun observeSettings(): Flow<AppSettings>

    /**
     * Update user profile
     */
    suspend fun updateProfile(profile: UserProfile)

    /**
     * Update app settings
     */
    suspend fun updateSettings(settings: AppSettings)

    /**
     * Toggle dark mode
     */
    suspend fun toggleDarkMode()

    /**
     * Logout user
     */
    suspend fun logout()
}
