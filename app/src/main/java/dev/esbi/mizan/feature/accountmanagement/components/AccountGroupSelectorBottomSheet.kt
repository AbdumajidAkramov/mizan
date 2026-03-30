package dev.esbi.mizan.feature.accountmanagement.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
fun AccountGroupSelectorBottomSheet(
    groups: List<AccountGroup>,
    selectedGroupId: Long,
    onGroupSelected: (AccountGroup) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = MizanTheme.premium.radius.xl, topEnd = MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.background.primary)
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select Group",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.SemiBold
            )
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                    .background(MizanTheme.premium.colors.surface2)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
        
        // Groups List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
        ) {
            items(
                items = groups,
                key = { it.id }
            ) { group ->
                GroupItem(
                    group = group,
                    isSelected = group.id == selectedGroupId,
                    onClick = { onGroupSelected(group) }
                )
            }
        }
    }
}

@Composable
private fun GroupItem(
    group: AccountGroup,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
            .background(
                if (isSelected) {
                    MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
                } else {
                    MizanTheme.premium.colors.surface2
                }
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md),
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
        
        // Group Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = group.name,
                style = MizanTheme.typography.bodyMd,
                color = if (isSelected) {
                    MizanTheme.premium.colors.emerald
                } else {
                    MizanTheme.premium.text.primary
                },
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
            
            if (group.isSystemGroup) {
                Text(
                    text = "System group",
                    style = MizanTheme.typography.bodyXs,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        }
        
        // Selection Indicator
        if (isSelected) {
            MizanIcon(
                icon = IconValue(MizanIcons.ic_check),
                contentDescription = "Selected",
                tint = MizanTheme.premium.colors.emerald,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
