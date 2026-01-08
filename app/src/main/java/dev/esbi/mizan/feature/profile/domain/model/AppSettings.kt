package dev.esbi.mizan.feature.profile.domain.model

/**
 * Application settings
 */
data class AppSettings(
    val isDarkMode: Boolean = false,
    val language: String = "English (US)",
    val currency: String = "USD",
    val notificationsEnabled: Boolean = true,
    val twoFactorEnabled: Boolean = false
)
