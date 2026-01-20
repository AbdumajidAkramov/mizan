package dev.esbi.mizan.feature.profile.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.profile.domain.model.AppSettings
import dev.esbi.mizan.feature.profile.domain.model.SettingAction
import dev.esbi.mizan.feature.profile.domain.model.SettingIcon
import dev.esbi.mizan.feature.profile.domain.model.SettingItem
import dev.esbi.mizan.feature.profile.domain.model.UserProfile
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModel
import dev.esbi.mizan.feature.profile.presentation.store.ProfileStore
import dev.esbi.mizan.feature.profile.presentation.ui.widgets.ProfileHeaderCard
import dev.esbi.mizan.feature.profile.presentation.ui.widgets.SettingsSection
import dev.esbi.mizan.feature.profile.presentation.ui.widgets.StatsGrid
import dev.esbi.mizan.ui.animation.FadeInUpAnimation
import dev.esbi.mizan.ui.animation.StaggeredFadeInUp
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.kit.glass.PressCard
import dev.esbi.mizan.ui.theme.PremiumColors
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState(initial = ProfileStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is ProfileStore.Label.ShowError -> {
                    // Handle error display
                }

                is ProfileStore.Label.NavigateToLogin -> {
                    // Handle navigation to login
                }

                is ProfileStore.Label.NavigateToSetting -> {
                    // Handle navigation to settings
                }
            }
        }
    }

    ProfileContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
internal fun ProfileContent(
    state: ProfileStore.State,
    onIntent: (ProfileStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold { paddingValues ->
        when {
            state.isLoading && state.profile == null -> {
                LoadingContent(modifier = modifier.padding(paddingValues))
            }

            state.error != null && state.profile == null -> {
                FadeInUpAnimation {
                    ErrorState(
                        message = state.error,
                        onRetry = { },
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }

            state.profile != null -> {
                val profile = state.profile
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Header
                    Text(
                        text = stringResource(R.string.profile_title),
                        style = MizanTheme.typography.headingXl,
                        color = MizanTheme.premium.text.primary,
                    )
                    ProfileHeaderCard(profile)

                    StatsGrid(profile)

                    // Settings Sections
                    val sections = getSettingsSections(profile, state.settings)
                    sections.forEachIndexed { sectionIndex, section ->
                        SettingsSection(
                            title = section.first,
                            items = section.second,
                            isDarkMode = state.settings.isDarkMode,
                            onToggleDarkMode = { onIntent(ProfileStore.Intent.ToggleDarkMode) },
                            onItemClick = { action ->
                                onIntent(ProfileStore.Intent.OnSettingClick(action))
                            }
                        )
                    }

                    // Logout Button
                    PressCard(
                        onClick = { onIntent(ProfileStore.Intent.Logout) },
                    ) {
                        PremiumCard(
                            variant = PremiumCardVariant.Glass,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp,
                                    color = MizanTheme.premium.colors.primary,
                                    shape = RoundedCornerShape(MizanTheme.premium.radius.full)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(Icons.ic_logout),
                                    contentDescription = null,
                                    tint = MizanTheme.premium.colors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.profile_logout),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MizanTheme.premium.colors.primary
                                )
                            }
                        }

                    }

                    // App Version
                    StaggeredFadeInUp(index = 4 + sections.size) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.profile_app_version),
                                fontSize = 12.sp,
                                color = PremiumColors.TextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.profile_copyright),
                                fontSize = 11.sp,
                                color = PremiumColors.TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
internal fun LoadingContent(modifier: Modifier = Modifier) {
    FadeInUpAnimation {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            repeat(8) { index ->
                StaggeredFadeInUp(index = index, delayMillis = 60) {
                    LoadingSkeleton(height = if (index == 1) 160 else 80)
                }
            }
        }
    }
}

internal fun getIconResource(icon: SettingIcon): Int {
    return when (icon) {
        SettingIcon.BUDGET_MANAGEMENT -> R.drawable.ic_dollar
        SettingIcon.FINANCIAL_GOALS -> R.drawable.ic_trend_up
        SettingIcon.USER -> R.drawable.ic_profile
        SettingIcon.MAIL -> R.drawable.ic_mail
        SettingIcon.PHONE -> R.drawable.ic_phone
        SettingIcon.BELL -> R.drawable.ic_bell
        SettingIcon.PALETTE -> R.drawable.ic_palette
        SettingIcon.GLOBE -> R.drawable.ic_globe
        SettingIcon.LOCK -> R.drawable.ic_lock
        SettingIcon.SHIELD -> R.drawable.ic_shield
        SettingIcon.DOWNLOAD -> R.drawable.ic_download
        SettingIcon.FILE -> R.drawable.ic_file
        SettingIcon.HELP -> R.drawable.ic_help
        SettingIcon.SHARE -> R.drawable.ic_share
        SettingIcon.STAR -> R.drawable.ic_star
        SettingIcon.DOLLAR -> R.drawable.ic_dollar
        SettingIcon.TRENDING -> R.drawable.ic_trend_up
        SettingIcon.PRIVACY_POLICY -> R.drawable.ic_file
        SettingIcon.TERMS_AND_SERVICE -> R.drawable.ic_file
    }
}

internal fun getSettingsSections(
    profile: UserProfile,
    settings: AppSettings
): List<Pair<String, List<SettingItem>>> {
    return listOf(
        "Finance" to listOf(
            SettingItem(
                id = "budget_management",
                icon = SettingIcon.BUDGET_MANAGEMENT,
                label = "Budget Management",
                description = "Set and track category budgets",
                action = SettingAction.BUDGET_MANAGEMENT
            ),
            SettingItem(
                id = "financial_goals",
                icon = SettingIcon.FINANCIAL_GOALS,
                label = "Financial Goals",
                description = "Track your savings goals",
                action = SettingAction.FINANCIAL_GOALS
            ),
        ),
        "Account" to listOf(
            SettingItem(
                id = "personal_info",
                icon = SettingIcon.USER,
                label = "Personal Information",
                description = "Update your profile details",
                action = SettingAction.PERSONAL_INFO
            ),
            SettingItem(
                id = "email",
                icon = SettingIcon.MAIL,
                label = "Email",
                value = profile.email,
                action = SettingAction.EMAIL
            ),
            SettingItem(
                id = "phone",
                icon = SettingIcon.PHONE,
                label = "Phone Number",
                value = profile.phoneNumber,
                action = SettingAction.PHONE
            )
        ),
        "Preferences" to listOf(
            SettingItem(
                id = "notifications",
                icon = SettingIcon.BELL,
                label = "Notifications",
                description = "Manage notification settings",
                action = SettingAction.NOTIFICATIONS
            ),
            SettingItem(
                id = "appearance",
                icon = SettingIcon.PALETTE,
                label = "Appearance",
                value = if (settings.isDarkMode) "Dark Mode" else "Light Mode",
                action = SettingAction.APPEARANCE
            ),
            SettingItem(
                id = "language",
                icon = SettingIcon.GLOBE,
                label = "Language",
                value = settings.language,
                action = SettingAction.LANGUAGE
            )
        ),
        "Security" to listOf(
            SettingItem(
                id = "password",
                icon = SettingIcon.LOCK,
                label = "Change Password",
                description = "Update your password",
                action = SettingAction.CHANGE_PASSWORD
            ),
            SettingItem(
                id = "2fa",
                icon = SettingIcon.SHIELD,
                label = "Two-Factor Authentication",
                value = if (settings.twoFactorEnabled) "Enabled" else "Disabled",
                action = SettingAction.TWO_FACTOR
            )
        ),
        "Data & Privacy" to listOf(
            SettingItem(
                id = "export_data",
                icon = SettingIcon.LOCK,
                label = "Export Data",
                description = "Download your transaction history",
                action = SettingAction.EXPORT_DATA
            ),
            SettingItem(
                id = "privacy_policy",
                icon = SettingIcon.PRIVACY_POLICY,
                label = "Privacy Policy",
                action = SettingAction.PRIVACY_POLICY
            ),
            SettingItem(
                id = "terms_of_service",
                icon = SettingIcon.TERMS_AND_SERVICE,
                label = "Terms & Service",
                action = SettingAction.TERMS_AND_SERVICE
            )
        ),
        "Support" to listOf(
            SettingItem(
                id = "help",
                icon = SettingIcon.HELP,
                label = "Help Center",
                description = "FAQs and guides",
                action = SettingAction.HELP_CENTER
            ),
            SettingItem(
                id = "privacy",
                icon = SettingIcon.FILE,
                label = "Privacy Policy",
                action = SettingAction.PRIVACY_POLICY
            ),
            SettingItem(
                id = "terms",
                icon = SettingIcon.FILE,
                label = "Terms of Service",
                action = SettingAction.TERMS
            )
        )
    )
}
