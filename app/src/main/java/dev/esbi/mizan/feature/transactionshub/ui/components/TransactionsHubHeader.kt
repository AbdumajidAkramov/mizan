package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Header component for TransactionsHub screen
 * Contains back button, title, month subtitle, and add transaction FAB
 */
@Composable
fun TransactionsHubHeader(
    currentMonth: YearMonth,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MizanTheme.premium.background.secondary)
            .padding(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.md
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface2)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Back",
                tint = MizanTheme.premium.text.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.md))

        // Title and Subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Transactions",
                style = MizanTheme.typography.headingLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentMonth.format(monthFormatter),
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }

        // Add Transaction FAB
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.emerald)
                .clickable { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
