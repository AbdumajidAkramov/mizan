package dev.esbi.mizan.feature.profile.domain.model

/**
 * Setting item for display in profile screen
 */
data class SettingItem(
    val id: String,
    val icon: SettingIcon,
    val label: String,
    val description: String? = null,
    val value: String? = null,
    val showChevron: Boolean = true,
    val action: SettingAction
)

enum class SettingIcon {
    USER,
    MAIL,
    PHONE,
    BELL,
    PALETTE,
    GLOBE,
    LOCK,
    SHIELD,
    DOWNLOAD,
    FILE,
    HELP,
    SHARE,
    STAR,
    DOLLAR,
    TRENDING
}

enum class SettingAction {
    PERSONAL_INFO,
    EMAIL,
    PHONE,
    NOTIFICATIONS,
    APPEARANCE,
    LANGUAGE,
    CHANGE_PASSWORD,
    TWO_FACTOR,
    EXPORT_DATA,
    PRIVACY_POLICY,
    TERMS,
    HELP_CENTER,
    SHARE_APP,
    RATE_APP,
    BUDGET_MANAGEMENT,
    FINANCIAL_GOALS
}
