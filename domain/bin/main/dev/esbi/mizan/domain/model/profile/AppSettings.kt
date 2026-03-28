package dev.esbi.mizan.domain.model.profile

import kotlinx.serialization.Serializable

/**
 * Application settings
 */
@Serializable
data class AppSettings(
    val isDarkMode: Boolean = false,
    val language: String = "English (US)",
    val currency: String = "USD",
    val notificationsEnabled: Boolean = true,
    val twoFactorEnabled: Boolean = false
)
