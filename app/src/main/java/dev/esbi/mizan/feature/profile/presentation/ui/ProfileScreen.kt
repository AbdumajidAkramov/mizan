package dev.esbi.mizan.feature.profile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.profile.domain.model.SettingAction
import dev.esbi.mizan.feature.profile.domain.model.SettingIcon
import dev.esbi.mizan.feature.profile.domain.model.SettingItem
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModel
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModelFactory
import dev.esbi.mizan.feature.profile.presentation.store.ProfileStore
import dev.esbi.mizan.ui.animation.FadeInUpAnimation
import dev.esbi.mizan.ui.animation.StaggeredFadeInUp
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModelFactory: ProfileViewModelFactory,
    modifier: Modifier = Modifier
) {
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
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
private fun ProfileContent(
    state: ProfileStore.State,
    onIntent: (ProfileStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

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

                FadeInUpAnimation {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Header
                        StaggeredFadeInUp(index = 0) {
                            Text(
                                text = stringResource(R.string.profile_title),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Profile Header Card
                        StaggeredFadeInUp(index = 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF667EEA),
                                                Color(0xFF764BA2),
                                                Color(0xFFF5576C)
                                            )
                                        )
                                    )
                                    .padding(24.dp)
                            ) {
                                // Background orbs
                                Box(
                                    modifier = Modifier
                                        .size(150.dp)
                                        .align(Alignment.TopEnd)
                                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                                        .blur(50.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .align(Alignment.BottomStart)
                                        .background(Color.Black.copy(alpha = 0.1f), CircleShape)
                                        .blur(40.dp)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Avatar
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.2f))
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.3f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = profile.avatarInitials,
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                    // Info
                                    Column {
                                        Text(
                                            text = profile.name,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = stringResource(R.string.profile_premium_member),
                                            fontSize = 14.sp,
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            InfoBadge(
                                                label = stringResource(R.string.profile_member_since),
                                                value = profile.memberSince
                                            )
                                            InfoBadge(
                                                label = stringResource(R.string.profile_transactions),
                                                value = profile.totalTransactions.toString()
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Stats Grid
                        StaggeredFadeInUp(index = 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                StatCard(
                                    label = stringResource(R.string.profile_income),
                                    value = numberFormat.format(profile.totalIncome),
                                    gradient = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
                                    icon = android.R.drawable.arrow_down_float,
                                    modifier = Modifier.weight(1f)
                                )
                                StatCard(
                                    label = stringResource(R.string.profile_expenses),
                                    value = numberFormat.format(profile.totalExpense),
                                    gradient = listOf(Color(0xFFFF6B6B), Color(0xFFF5576C)),
                                    icon = android.R.drawable.arrow_up_float,
                                    modifier = Modifier.weight(1f)
                                )
                                StatCard(
                                    label = stringResource(R.string.profile_saved),
                                    value = numberFormat.format(profile.totalSaved),
                                    gradient = listOf(Color(0xFF667EEA), Color(0xFFF5576C)),
                                    icon = android.R.drawable.star_on,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Settings Sections
                        val sections = getSettingsSections(profile, state.settings)
                        sections.forEachIndexed { sectionIndex, section ->
                            StaggeredFadeInUp(index = 3 + sectionIndex) {
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
                        }

                        // Logout Button
                        StaggeredFadeInUp(index = 3 + sections.size) {
                            PremiumCard(
                                variant = PremiumCardVariant.Glass,
                                onClick = { onIntent(ProfileStore.Intent.Logout) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                                        contentDescription = null,
                                        tint = Color(0xFFFF6B6B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.profile_logout),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFFF6B6B)
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
}

@Composable
private fun InfoBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    gradient: List<Color>,
    icon: Int,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Brush.linearGradient(gradient),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = PremiumColors.TextMuted
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PremiumColors.TextPrimary
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    items: List<SettingItem>,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onItemClick: (SettingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = PremiumColors.TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                SettingItemRow(
                    item = item,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onClick = { onItemClick(item.action) }
                )
            }
        }
    }
}

@Composable
private fun SettingItemRow(
    item: SettingItem,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAppearance = item.action == SettingAction.APPEARANCE

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        onClick = if (!isAppearance) onClick else null,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(PremiumColors.Surface2, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(getIconResource(item.icon)),
                    contentDescription = null,
                    tint = PremiumColors.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PremiumColors.TextPrimary
                )
                if (item.description != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = PremiumColors.TextTertiary
                    )
                }
                if (item.value != null && !isAppearance) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.value,
                        fontSize = 12.sp,
                        color = PremiumColors.TextTertiary
                    )
                }
            }

            // Action
            if (isAppearance) {
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onToggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF667EEA),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = PremiumColors.Surface3
                    )
                )
            } else if (item.showChevron) {
                Icon(
                    painter = painterResource(android.R.drawable.arrow_down_float),
                    contentDescription = null,
                    tint = PremiumColors.TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
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

private fun getIconResource(icon: SettingIcon): Int {
    return when (icon) {
        SettingIcon.USER -> R.drawable.outline_5g_24
        SettingIcon.MAIL -> R.drawable.outline_5g_24
        SettingIcon.PHONE -> R.drawable.outline_5g_24
        SettingIcon.BELL -> R.drawable.outline_5g_24
        SettingIcon.PALETTE -> R.drawable.outline_5g_24
        SettingIcon.GLOBE -> R.drawable.outline_5g_24
        SettingIcon.LOCK -> R.drawable.outline_5g_24
        SettingIcon.SHIELD -> R.drawable.outline_5g_24
        SettingIcon.DOWNLOAD -> R.drawable.outline_5g_24
        SettingIcon.FILE -> R.drawable.outline_5g_24
        SettingIcon.HELP -> R.drawable.outline_5g_24
        SettingIcon.SHARE -> R.drawable.outline_5g_24
        SettingIcon.STAR -> R.drawable.outline_5g_24
        SettingIcon.DOLLAR -> R.drawable.outline_5g_24
        SettingIcon.TRENDING -> R.drawable.outline_5g_24
    }
}

private fun getSettingsSections(
    profile: dev.esbi.mizan.feature.profile.domain.model.UserProfile,
    settings: dev.esbi.mizan.feature.profile.domain.model.AppSettings
): List<Pair<String, List<SettingItem>>> {
    return listOf(
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
