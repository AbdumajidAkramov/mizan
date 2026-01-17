package dev.esbi.mizan.feature.profile.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

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
