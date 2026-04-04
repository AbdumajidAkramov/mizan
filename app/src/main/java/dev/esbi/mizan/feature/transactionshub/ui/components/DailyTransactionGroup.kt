package dev.esbi.mizan.feature.transactionshub.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.text.NumberFormat
import java.util.Locale

/**
 * Daily transaction group with date header and transaction items
 */
@Composable
fun DailyTransactionGroup(
    group: TransactionsHubStore.DailyGroup,
    accounts: List<Account>,
    categories: List<Category>,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val numberFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MizanTheme.premium.spacing.lg)
    ) {
        // Date Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MizanTheme.premium.spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = group.dateFormatted,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatAmount(group.dayTotal.toDouble(), numberFormat),
                style = MizanTheme.typography.bodySm,
                color = if (group.dayTotal >= java.math.BigDecimal.ZERO) MizanTheme.premium.colors.emerald else Color(
                    0xFFF5576C
                ),
                fontWeight = FontWeight.SemiBold
            )
        }

        // Transaction Items
        Column(
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
        ) {
            group.transactions.forEach { transaction ->
                TransactionItem(
                    transaction = transaction,
                    account = accounts.find { it.id == transaction.accountId },
                    category = categories.find { it.id == transaction.categoryId },
                    onClick = { onTransactionClick(transaction) }
                )
            }
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))
    }
}

@Composable
private fun TransactionItem(
    transaction: Transaction,
    account: Account?,
    category: Category?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val numberFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val isIncome = transaction.type == Transaction.Type.INCOME
    val amountColor = if (isIncome) MizanTheme.premium.colors.emerald else Color(0xFFF5576C)

    // Category color - use a default pink if not found
    val categoryColor = remember(category) {
        category?.color?.let {
            try {
                Color(android.graphics.Color.parseColor(it))
            } catch (e: Exception) {
                Color(0xFFFF6B9D)
            }
        } ?: Color(0xFFFF6B9D)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(MizanTheme.premium.colors.surface2)
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                .background(categoryColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = getCategoryIcon(category?.iconName)),
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.md))

        // Transaction Details
        Column(modifier = Modifier.weight(1f)) {
            // Title (description or category name)
            Text(
                text = transaction.description ?: category?.name ?: "Transaction",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Subtitle (Category • Account)
            Text(
                text = buildString {
                    append(category?.name ?: "Uncategorized")
                    account?.let {
                        append(" • ")
                        append(it.name)
                    }
                },
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Note if present
            transaction.note?.let { note ->
                if (note.isNotBlank()) {
                    Text(
                        text = note,
                        style = MizanTheme.typography.labelSm,
                        color = MizanTheme.premium.text.muted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Amount
        Text(
            text = "${if (isIncome) "+" else "-"}${numberFormat.format(transaction.amount)}",
            style = MizanTheme.typography.bodyLg,
            color = amountColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatAmount(amount: Double, numberFormat: NumberFormat): String {
    val prefix = if (amount >= 0) "+" else ""
    return "$prefix${numberFormat.format(amount)}"
}

private fun getCategoryIcon(iconName: String?): Int {
    return when (iconName?.lowercase()) {
        "food", "food-dining", "restaurant", "utensils" -> Icons.ic_utensils
        "transport", "transportation", "car" -> Icons.ic_car
        "shopping", "shop", "bag" -> Icons.ic_shopping_bag
        "bills", "bills-utilities", "home" -> Icons.ic_home
        "entertainment", "coffee" -> Icons.ic_coffee
        "health", "healthcare" -> Icons.ic_heart
        "income", "salary", "trending-up" -> Icons.ic_trending_up
        else -> Icons.ic_wallet
    }
}
