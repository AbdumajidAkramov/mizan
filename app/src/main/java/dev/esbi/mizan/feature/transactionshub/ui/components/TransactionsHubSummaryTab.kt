package dev.esbi.mizan.feature.transactionshub.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Summary Tab for TransactionsHub
 * Shows financial analysis with donut chart and category breakdown
 */
@Composable
fun TransactionsHubSummaryTab(
    currentMonth: YearMonth,
    summary: TransactionsHubStore.MonthlySummary,
    expenseCategorySummaries: List<TransactionsHubStore.CategorySummary>,
    incomeCategorySummaries: List<TransactionsHubStore.CategorySummary>,
    weeklySummaries: List<TransactionsHubStore.WeeklySummary>,
    expenseAccountSummaries: List<TransactionsHubStore.AccountSummary>,
    incomeAccountSummaries: List<TransactionsHubStore.AccountSummary>,
    savingsRate: Float,
    transactionCount: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    var analyticsMode by remember { mutableStateOf(AnalyticsMode.Expense) }

    val currentSummaries = when (analyticsMode) {
        AnalyticsMode.Expense -> expenseCategorySummaries
        AnalyticsMode.Income -> incomeCategorySummaries
    }

    val currentTotal = when (analyticsMode) {
        AnalyticsMode.Expense -> summary.totalExpense
        AnalyticsMode.Income -> summary.totalIncome
    }

    val centerColor = when (analyticsMode) {
        AnalyticsMode.Expense -> Color(0xFFF5576C)
        AnalyticsMode.Income -> MizanTheme.premium.colors.emerald
    }

    val currentAccountSummaries = when (analyticsMode) {
        AnalyticsMode.Expense -> expenseAccountSummaries
        AnalyticsMode.Income -> incomeAccountSummaries
    }

    val topCategories = currentSummaries.take(5)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Month Selector Header
        item {
            SummaryMonthHeader(
                currentMonth = currentMonth,
                transactionCount = transactionCount,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth
            )
        }

        // Key Metrics Cards
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md),
                verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
            ) {
                // Income Card
                MetricCard(
                    title = "INCOME",
                    subtitle = "This month",
                    amount = summary.totalIncome,
                    color = MizanTheme.premium.colors.emerald,
                    iconResId = R.drawable.ic_trending_up
                )

                // Expense Card
                MetricCard(
                    title = "EXPENSES",
                    subtitle = "This month",
                    amount = summary.totalExpense,
                    color = Color(0xFFF5576C),
                    iconResId = R.drawable.ic_trending_down
                )

                // Net Savings Card
                NetSavingsCard(
                    netSavings = summary.balance,
                    savingsRate = savingsRate
                )
            }
        }

        // Category Breakdown Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .background(MizanTheme.premium.colors.surface2)
                    .border(
                        width = 1.dp,
                        color = MizanTheme.premium.glass.border,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
                    )
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                // Header with Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Category\nBreakdown",
                        style = MizanTheme.typography.headingLg,
                        color = MizanTheme.premium.text.primary
                    )

                    AnalyticsModeToggle(
                        selectedMode = analyticsMode,
                        onModeSelected = { analyticsMode = it }
                    )
                }

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

                // Donut Chart
                if (currentSummaries.isNotEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        DonutChart(
                            categorySummaries = currentSummaries,
                            totalAmount = currentTotal,
                            centerLabel = "$${formatCompactAmountSummary(currentTotal)}",
                            centerColor = centerColor,
                            chartSize = 200.dp,
                            strokeWidth = 28.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

                    // Category Legend List
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
                    ) {
                        currentSummaries.forEach { categorySummary ->
                            CategoryLegendItem(categorySummary = categorySummary)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MizanTheme.premium.spacing.xl),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No ${analyticsMode.name.lowercase()} data for this month",
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
        }

        // Weekly Trends Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .background(MizanTheme.premium.colors.surface2)
                    .border(
                        width = 1.dp,
                        color = MizanTheme.premium.glass.border,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
                    )
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                Text(
                    text = "Weekly Trends",
                    style = MizanTheme.typography.headingLg,
                    color = MizanTheme.premium.text.primary
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

                WeeklyTrendsChart(
                    weeklySummaries = weeklySummaries,
                    chartHeight = 200.dp
                )
            }
        }

        // Top 5 Categories Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .background(MizanTheme.premium.colors.surface2)
                    .border(
                        width = 1.dp,
                        color = MizanTheme.premium.glass.border,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
                    )
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                Text(
                    text = "Top 5 Categories",
                    style = MizanTheme.typography.headingLg,
                    color = MizanTheme.premium.text.primary
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

                if (topCategories.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
                    ) {
                        topCategories.forEachIndexed { index, category ->
                            TopCategoryItem(
                                rank = index + 1,
                                categorySummary = category
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MizanTheme.premium.spacing.lg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No category data",
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
        }

        // Account Usage Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .background(MizanTheme.premium.colors.surface2)
                    .border(
                        width = 1.dp,
                        color = MizanTheme.premium.glass.border,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
                    )
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                Text(
                    text = "Account Usage",
                    style = MizanTheme.typography.headingLg,
                    color = MizanTheme.premium.text.primary
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

                if (currentAccountSummaries.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
                    ) {
                        currentAccountSummaries.forEach { account ->
                            AccountUsageItem(accountSummary = account)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MizanTheme.premium.spacing.lg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No account data",
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
        }
    }
}

private enum class AnalyticsMode {
    Expense,
    Income
}

@Composable
private fun SummaryMonthHeader(
    currentMonth: YearMonth,
    transactionCount: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MizanTheme.premium.background.primary)
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface2)
                .clickable { onPreviousMonth() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Previous Month",
                tint = MizanTheme.premium.text.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = currentMonth.format(monthFormatter),
                style = MizanTheme.typography.headingMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$transactionCount transactions",
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.tertiary
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface2)
                .clickable { onNextMonth() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Next Month",
                tint = MizanTheme.premium.text.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    subtitle: String,
    amount: Double,
    color: Color,
    iconResId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(color.copy(alpha = 0.1f))
            .border(
                width = 2.dp,
                color = color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .padding(MizanTheme.premium.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

            Column {
                Text(
                    text = title,
                    style = MizanTheme.typography.bodyMd,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MizanTheme.typography.labelSm,
                    color = color.copy(alpha = 0.7f)
                )
            }
        }

        Text(
            text = "$${formatCompactAmountSummary(amount)}",
            style = MizanTheme.typography.headingLg,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun NetSavingsCard(
    netSavings: Double,
    savingsRate: Float,
    modifier: Modifier = Modifier
) {
    val isPositive = netSavings >= 0
    val color = if (isPositive) MizanTheme.premium.colors.emerald else Color(0xFFF5576C)
    val animatedProgress by animateFloatAsState(
        targetValue = (savingsRate.coerceIn(0f, 100f) / 100f),
        animationSpec = tween(700),
        label = "savingsProgress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(color.copy(alpha = 0.1f))
            .border(
                width = 2.dp,
                color = color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wallet),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

                Column {
                    Text(
                        text = "NET SAVINGS",
                        style = MizanTheme.typography.bodyMd,
                        color = color,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Savings rate: ${String.format("%.1f", savingsRate)}%",
                        style = MizanTheme.typography.labelSm,
                        color = color.copy(alpha = 0.7f)
                    )
                }
            }

            Text(
                text = "${if (isPositive) "+" else "-"}$${
                    formatCompactAmountSummary(
                        kotlin.math.abs(
                            netSavings
                        )
                    )
                }",
                style = MizanTheme.typography.headingLg,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        // Progress Bar
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MizanTheme.premium.colors.surface3,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun AnalyticsModeToggle(
    selectedMode: AnalyticsMode,
    onModeSelected: (AnalyticsMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MizanTheme.premium.colors.surface3)
            .padding(4.dp)
    ) {
        AnalyticsMode.entries.forEach { mode ->
            val isSelected = mode == selectedMode
            val backgroundColor = when {
                isSelected && mode == AnalyticsMode.Expense -> Color(0xFFF5576C)
                isSelected && mode == AnalyticsMode.Income -> MizanTheme.premium.colors.emerald
                else -> Color.Transparent
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(backgroundColor)
                    .clickable { onModeSelected(mode) }
                    .padding(
                        horizontal = MizanTheme.premium.spacing.md,
                        vertical = MizanTheme.premium.spacing.xs
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode.name,
                    style = MizanTheme.typography.bodySm,
                    color = if (isSelected) Color.White else MizanTheme.premium.text.secondary,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun CategoryLegendItem(
    categorySummary: TransactionsHubStore.CategorySummary,
    modifier: Modifier = Modifier
) {
    val categoryColor = remember(categorySummary.categoryColor) {
        try {
            Color(android.graphics.Color.parseColor(categorySummary.categoryColor))
        } catch (e: Exception) {
            Color(0xFFFF6B9D)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
            .background(MizanTheme.premium.colors.surface3)
            .padding(MizanTheme.premium.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color Indicator
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(categoryColor)
        )

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

        // Category Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = categorySummary.categoryName,
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${categorySummary.transactionCount} transaction${if (categorySummary.transactionCount != 1) "s" else ""}",
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.tertiary
            )
        }

        // Amount and Percentage
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${formatCompactAmountSummary(categorySummary.totalAmount)}",
                style = MizanTheme.typography.bodyLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${String.format("%.1f", categorySummary.percentage)}%",
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.tertiary
            )
        }

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.xs))

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MizanTheme.premium.text.tertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@SuppressLint("DefaultLocale")
internal fun formatCompactAmountSummary(amount: Double): String {
    val absAmount = kotlin.math.abs(amount)
    val result = when {
        absAmount >= 1000000 -> "${(absAmount / 1000000).let { if (it == it.toLong().toDouble()) it.toLong().toString() else String.format("%.1f", it) }}M"
        absAmount >= 10000 -> "${(absAmount / 1000).let { if (it == it.toLong().toDouble()) it.toLong().toString() else String.format("%.1f", it) }}k"
        absAmount >= 1000 -> "${String.format("%.2f", absAmount / 1000)}k"
        else -> absAmount.toLong().toString()
    }
    return if (amount >= 0) result else "-$result"
}

@Composable
private fun TopCategoryItem(
    rank: Int,
    categorySummary: TransactionsHubStore.CategorySummary,
    modifier: Modifier = Modifier
) {
    val categoryColor = remember(categorySummary.categoryColor) {
        try {
            Color(android.graphics.Color.parseColor(categorySummary.categoryColor))
        } catch (e: Exception) {
            Color(0xFFFF6B9D)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(MizanTheme.premium.colors.surface3)
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank Badge
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(categoryColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                style = MizanTheme.typography.bodyLg,
                color = categoryColor,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

        // Category Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = categorySummary.categoryName,
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${
                    String.format(
                        "%.1f",
                        categorySummary.percentage
                    )
                }% of total • ${categorySummary.transactionCount} txn${if (categorySummary.transactionCount != 1) "s" else ""}",
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.tertiary
            )
        }

        // Amount
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$${formatCompactAmountSummary(categorySummary.totalAmount)}",
                style = MizanTheme.typography.bodyLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.xs))
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = MizanTheme.premium.text.tertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun AccountUsageItem(
    accountSummary: TransactionsHubStore.AccountSummary,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (accountSummary.percentage.coerceIn(0f, 100f) / 100f),
        animationSpec = tween(700),
        label = "accountProgress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(MizanTheme.premium.colors.surface3)
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Account Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wallet),
                        contentDescription = null,
                        tint = MizanTheme.premium.colors.emerald,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

                Column {
                    Text(
                        text = accountSummary.accountName,
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${accountSummary.transactionCount} transaction${if (accountSummary.transactionCount != 1) "s" else ""}",
                        style = MizanTheme.typography.labelSm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            // Amount and Percentage
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${formatCompactAmountSummary(accountSummary.totalAmount)}",
                        style = MizanTheme.typography.bodyLg,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${String.format("%.1f", accountSummary.percentage)}%",
                        style = MizanTheme.typography.labelSm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
                Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.xs))
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        // Progress Bar
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MizanTheme.premium.colors.emerald,
            trackColor = MizanTheme.premium.colors.surface4,
            strokeCap = StrokeCap.Round
        )
    }
}
