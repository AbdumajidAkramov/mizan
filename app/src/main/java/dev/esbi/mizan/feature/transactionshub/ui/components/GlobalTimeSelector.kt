package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Global Time Selector Component
 * Centered month/year selector with navigation arrows
 * Shows contextual information about the selected period
 */
@Composable
fun GlobalTimeSelector(
    currentMonth: YearMonth,
    daysWithTransactions: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFormatter = remember {
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    }

    val contextText = when {
        daysWithTransactions > 0 -> "$daysWithTransactions day${if (daysWithTransactions != 1) "s" else ""} with transactions"
        else -> "No transactions"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(0.dp)
            )
            .padding(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.md
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))

                .border(
                    width = 1.dp,
                    color = MizanTheme.premium.glass.border,
                    shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
                )
                .padding(horizontal = MizanTheme.premium.spacing.md, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Previous Month Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface2)
                    .border(1.dp, MizanTheme.premium.glass.border, CircleShape)
                    .clickable { onPreviousMonth() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = "Previous month",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Month/Year Display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = MizanTheme.premium.spacing.md)
            ) {
                Text(
                    text = currentMonth.format(monthFormatter),
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = contextText,
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            // Next Month Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface2)
                    .border(1.dp, MizanTheme.premium.glass.border, CircleShape)
                    .clickable { onNextMonth() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = "Next month",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
