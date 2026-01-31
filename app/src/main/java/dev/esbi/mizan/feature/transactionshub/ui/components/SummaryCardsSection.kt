package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import dev.esbi.mizan.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Summary cards section showing Income, Expense, and Total balance
 */
@Composable
fun SummaryCardsSection(
    summary: TransactionsHubStore.MonthlySummary,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Income Card
        SummaryCard(
            label = "INCOME",
            amount = summary.totalIncome,
            color = MizanTheme.premium.colors.emerald,
            iconResId = R.drawable.ic_trending_up,
            modifier = Modifier.weight(1f)
        )

        // Expense Card
        SummaryCard(
            label = "EXPENSE",
            amount = summary.totalExpense,
            color = Color(0xFFF5576C), // Red/Pink
            iconResId = R.drawable.ic_trending_down,
            modifier = Modifier.weight(1f)
        )

        // Total Card
        SummaryCard(
            label = "TOTAL",
            amount = summary.balance,
            color = if (summary.balance >= 0) MizanTheme.premium.colors.emerald else Color(0xFFF5576C),
            iconResId = R.drawable.ic_wallet,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    label: String,
    amount: Double,
    color: Color,
    iconResId: Int,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(color.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(MizanTheme.premium.radius.lg)
            )
            .padding(MizanTheme.premium.spacing.md)
    ) {
        // Icon and Label Row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MizanTheme.typography.labelSm,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Amount
        Text(
            text = numberFormat.format(amount),
            style = MizanTheme.typography.headingMd,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
