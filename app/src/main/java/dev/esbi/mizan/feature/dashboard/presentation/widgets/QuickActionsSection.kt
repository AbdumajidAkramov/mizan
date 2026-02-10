package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * Quick Actions Section for Dashboard
 * Provides quick access to common actions like Add Transaction
 */
@Composable
fun QuickActionsSection(
    onAddTransactionClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onTransferClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Add Transaction - Primary Action
        QuickActionButton(
            icon = R.drawable.ic_add,
            label = "Add",
            isPrimary = true,
            onClick = onAddTransactionClick,
            modifier = Modifier.weight(1f)
        )

        // Transfer - Secondary Action
        QuickActionButton(
            icon = R.drawable.ic_swap,
            label = "Transfer",
            isPrimary = false,
            onClick = onTransferClick,
            modifier = Modifier.weight(1f)
        )

        // View History - Secondary Action
        QuickActionButton(
            icon = R.drawable.ic_list,
            label = "History",
            isPrimary = false,
            onClick = onViewHistoryClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: Int,
    label: String,
    isPrimary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isPrimary) {
        Brush.linearGradient(
            colors = listOf(
                MizanTheme.premium.colors.emerald,
                Color(0xFF059669)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MizanTheme.premium.colors.surface2,
                MizanTheme.premium.colors.surface2
            )
        )
    }

    val contentColor = if (isPrimary) Color.White else MizanTheme.premium.text.primary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(backgroundColor)
            .then(
                if (!isPrimary) {
                    Modifier.border(
                        width = 1.dp,
                        color = MizanTheme.premium.glass.border,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
                    )
                } else Modifier
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPrimary) Color.White.copy(alpha = 0.2f)
                        else MizanTheme.premium.colors.surface3
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

            Text(
                text = label,
                style = MizanTheme.typography.bodyMd,
                color = contentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Floating Action Button for Add Transaction
 * Can be used as an alternative to the QuickActionsSection
 */
@Composable
fun DashboardFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = MizanTheme.premium.colors.emerald.copy(alpha = 0.4f),
                spotColor = MizanTheme.premium.colors.emerald.copy(alpha = 0.4f)
            )
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.emerald,
                        Color(0xFF059669)
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Add Transaction",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}
