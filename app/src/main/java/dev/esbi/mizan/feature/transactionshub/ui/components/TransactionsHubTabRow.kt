package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes

/**
 * Custom pill-shaped tab row for TransactionsHub
 * Matches the premium design with emerald green active state
 */
@Composable
fun TransactionsHubTabRow(
    selectedTab: TransactionsHubStore.Tab,
    onTabSelected: (TransactionsHubStore.Tab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
    ) {
        TransactionsHubStore.Tab.entries.forEach { tab ->
            TabItem(
                tab = tab,
                isSelected = selectedTab == tab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun TabItem(
    tab: TransactionsHubStore.Tab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MizanTheme.premium.colors.emerald else MizanTheme.premium.colors.surface2,
        animationSpec = tween(200),
        label = "tabBackground"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MizanTheme.premium.text.secondary,
        animationSpec = tween(200),
        label = "tabContent"
    )

    val iconResId = when (tab) {
        TransactionsHubStore.Tab.Daily -> IconRes.ic_list
        TransactionsHubStore.Tab.Calendar -> IconRes.ic_calendar
        TransactionsHubStore.Tab.Monthly -> IconRes.ic_chart_bar
        TransactionsHubStore.Tab.Summary -> IconRes.ic_chart_bar
        TransactionsHubStore.Tab.Description -> IconRes.ic_list
    }

    val label = when (tab) {
        TransactionsHubStore.Tab.Daily -> "Daily"
        TransactionsHubStore.Tab.Calendar -> "Calendar"
        TransactionsHubStore.Tab.Monthly -> "Monthly"
        TransactionsHubStore.Tab.Summary -> "Summary"
        TransactionsHubStore.Tab.Description -> "Description"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal = MizanTheme.premium.spacing.md,
                vertical = MizanTheme.premium.spacing.sm
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MizanTheme.typography.bodySm,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}
