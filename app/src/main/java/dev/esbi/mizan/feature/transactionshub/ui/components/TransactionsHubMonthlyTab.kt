package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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

/**
 * Monthly Tab for TransactionsHub
 * Shows transactions grouped by week with expandable sections
 */
@Composable
fun TransactionsHubMonthlyTab(
    weeklySummaries: List<TransactionsHubStore.WeeklySummary>,
    accounts: List<Account>,
    categories: List<Category>,
    onToggleWeek: (Int) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    // Weekly Sections
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MizanTheme.premium.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            item { Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md)) }

            if (weeklySummaries.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MizanTheme.premium.spacing.xl),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No transactions this month",
                                style = MizanTheme.typography.headingMd,
                                color = MizanTheme.premium.text.tertiary
                            )
                            Text(
                                text = "Add your first transaction to get started",
                                style = MizanTheme.typography.bodySm,
                                color = MizanTheme.premium.text.muted
                            )
                        }
                    }
                }
            } else {
                items(
                    items = weeklySummaries,
                    key = { it.weekNumber }
                ) { week ->
                    WeekSection(
                        week = week,
                        accounts = accounts,
                        categories = categories,
                        onToggle = { onToggleWeek(week.weekNumber) },
                        onTransactionClick = onTransactionClick
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg)) }
        }
    }
}

@Composable
private fun WeekSection(
    week: TransactionsHubStore.WeeklySummary,
    accounts: List<Account>,
    categories: List<Category>,
    onToggle: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(MizanTheme.premium.colors.surface2)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.lg)
            )
    ) {
        // Week Header - Tappable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(MizanTheme.premium.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side: Expand icon + Date range
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        id = if (week.isExpanded) Icons.ic_chevron_down else Icons.ic_chevron_right
                    ),
                    contentDescription = if (week.isExpanded) "Collapse" else "Expand",
                    tint = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

                Column {
                    Text(
                        text = week.dateRangeFormatted,
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${week.transactions.size} transactions",
                        style = MizanTheme.typography.labelSm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            // Right side: Totals
            Column(horizontalAlignment = Alignment.End) {
                Row(horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)) {
                    if (week.income > java.math.BigDecimal.ZERO) {
                        Text(
                            text = "+$${formatCompactAmountMonthly(week.income.toDouble())}",
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.colors.emerald
                        )
                    }
                    if (week.expense > java.math.BigDecimal.ZERO) {
                        Text(
                            text = "-$${formatCompactAmountMonthly(week.expense.toDouble())}",
                            style = MizanTheme.typography.bodySm,
                            color = Color(0xFFF5576C)
                        )
                    }
                }
                Text(
                    text = "${if (week.balance >= java.math.BigDecimal.ZERO) "+" else ""}$${formatCompactAmountMonthly(week.balance.toDouble())}",
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Expandable Transaction List
        AnimatedVisibility(
            visible = week.isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(
                    color = MizanTheme.premium.glass.border,
                    thickness = 1.dp
                )

                week.transactions.forEachIndexed { index, transaction ->
                    WeekTransactionItem(
                        transaction = transaction,
                        account = accounts.find { it.id == transaction.accountId },
                        category = categories.find { it.id == transaction.categoryId },
                        onClick = { onTransactionClick(transaction) }
                    )

                    if (index < week.transactions.lastIndex) {
                        HorizontalDivider(
                            color = MizanTheme.premium.glass.border,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekTransactionItem(
    transaction: Transaction,
    account: Account?,
    category: Category?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIncome = transaction.type == Transaction.Type.INCOME
    val amountColor = if (isIncome) MizanTheme.premium.colors.emerald else Color(0xFFF5576C)

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
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(categoryColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = getCategoryIconMonthly(category?.iconName)),
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

        // Transaction Info
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = category?.name ?: "Uncategorized",
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = buildString {
                    append(account?.name ?: "Unknown")
                    transaction.description?.let {
                        if (it.isNotBlank()) {
                            append(" • $it")
                        }
                    }
                },
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Amount
        Text(
            text = "${if (isIncome) "+" else "-"}$${String.format("%.2f", transaction.amount)}",
            style = MizanTheme.typography.bodyMd,
            color = amountColor,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatCompactAmountMonthly(amount: Double): String {
    return when {
        amount >= 1000000 -> "${
            (amount / 1000000).let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else String.format(
                    "%.1f",
                    it
                )
            }
        }M"

        amount >= 1000 -> "${
            (amount / 1000).let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else String.format(
                    "%.1f",
                    it
                )
            }
        }k"

        else -> amount.toLong().toString()
    }
}

private fun getCategoryIconMonthly(iconName: String?): Int {
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
