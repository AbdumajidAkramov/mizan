package dev.esbi.mizan.feature.profile.presentation.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.profile.SettingAction
import dev.esbi.mizan.domain.model.profile.SettingItem
import dev.esbi.mizan.feature.profile.presentation.ui.getIconResource
import dev.esbi.mizan.design.components.PremiumCard
import dev.esbi.mizan.design.components.PremiumCardVariant
import dev.esbi.mizan.design.components.PremiumThemeToggle
import dev.esbi.mizan.design.kit.glass.PressCard
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.theme.shadows.premiumShadow
import dev.esbi.mizan.design.utils.IconRes

@Composable
internal fun SettingsSection(
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
            style = MizanTheme.typography.headingSm,
            color = MizanTheme.premium.text.secondary,
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
internal fun SettingItemRow(
    item: SettingItem,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAppearance = item.action == SettingAction.APPEARANCE
    PressCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        PremiumCard(
            variant = PremiumCardVariant.Glass,
            modifier = modifier.premiumShadow(MizanTheme.premium.shadows.sm),
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
                        .background(
                            MizanTheme.premium.colors.surface2,
                            RoundedCornerShape(MizanTheme.premium.radius.md)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(getIconResource(item.icon)),
                        contentDescription = null,
                        tint = MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Content
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                    Text(
                        text = item.label,
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.primary,
                    )
                    item.description?.let { desc ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = desc,
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary,
                        )
                    }
                    if (!isAppearance) {
                        item.value?.let { itemValue ->
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = itemValue,
                                style = MizanTheme.typography.bodySm,
                                color = MizanTheme.premium.text.tertiary,
                            )
                        }
                    }
                }

                // Action
                if (isAppearance) {
                    PremiumThemeToggle(
                        isDarkMode = isDarkMode,
                        onToggle = onToggleDarkMode,
                    )
                } else if (item.showChevron) {
                    Icon(
                        painter = painterResource(IconRes.ic_chevron_right),
                        contentDescription = null,
                        tint = MizanTheme.premium.text.muted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

