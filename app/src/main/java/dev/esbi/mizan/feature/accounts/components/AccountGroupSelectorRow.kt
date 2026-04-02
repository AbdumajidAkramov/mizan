package dev.esbi.mizan.feature.accounts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons as MizanIcons

@Composable
fun AccountGroupSelectorRow(
    selectedGroup: AccountGroup?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.colors.surface2)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            // Group Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                MizanIcon(
                    icon = IconValue(MizanIcons.ic_category),
                    contentDescription = null,
                    tint = MizanTheme.premium.colors.emerald,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Group Name
            Column {
                Text(
                    text = selectedGroup?.name ?: "Select Group",
                    style = MizanTheme.typography.bodyMd,
                    color = if (selectedGroup != null) {
                        MizanTheme.premium.text.primary
                    } else {
                        MizanTheme.premium.text.tertiary
                    },
                    fontWeight = FontWeight.Medium
                )

                if (selectedGroup?.isSystemGroup == true) {
                    Text(
                        text = "System group",
                        style = MizanTheme.typography.bodyXs,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

        // Dropdown Arrow
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Select group",
            tint = MizanTheme.premium.text.tertiary,
            modifier = Modifier.size(24.dp)
        )
    }
}
