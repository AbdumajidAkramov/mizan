package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Calendar Tab for TransactionsHub
 * Shows a calendar grid with transaction indicators and selected day transactions list
 */
@Composable
fun TransactionsHubCalendarTab(
    currentMonth: YearMonth,
    calendarDays: List<TransactionsHubStore.CalendarDaySummary?>,
    daysWithTransactions: Int,
    selectedDate: LocalDate,
    transactions: List<Transaction>,
    accounts: List<Account>,
    categories: List<Category>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()) }

    // Filter transactions for selected date
    val selectedDateTransactions = remember(selectedDate, transactions) {
        transactions.filter { txn ->
            val txnDate = Instant.ofEpochMilli(txn.date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            txnDate == selectedDate
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MizanTheme.premium.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Month Selector Header
        item {
            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))
            CalendarMonthHeader(
                currentMonth = currentMonth,
                daysWithTransactions = daysWithTransactions,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth
            )
        }

        // Calendar Grid
        item {
            CalendarGrid(
                calendarDays = calendarDays,
                selectedDate = selectedDate,
                onDateSelected = onDateSelected
            )
        }

        // Monthly Summary
        item {
            CalendarMonthlySummary(calendarDays = calendarDays)
        }

        // Selected Day Transactions Header
        item {
            if (selectedDateTransactions.isNotEmpty()) {
                val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault())
                Text(
                    text = selectedDate.format(dateFormatter),
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = MizanTheme.premium.spacing.sm)
                )
            }
        }

        // Selected Day Transactions List
        if (selectedDateTransactions.isNotEmpty()) {
            items(selectedDateTransactions.size) { index ->
                val transaction = selectedDateTransactions[index]
                CalendarTransactionItem(
                    transaction = transaction,
                    account = accounts.find { it.id == transaction.accountId },
                    category = categories.find { it.id == transaction.categoryId },
                    onClick = { onTransactionClick(transaction) }
                )
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MizanTheme.premium.spacing.xl),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No transactions on this day",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
        }
    }
}

@Composable
private fun CalendarMonthHeader(
    currentMonth: YearMonth,
    daysWithTransactions: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(MizanTheme.premium.colors.surface2)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.lg)
            )
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Previous Month Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface3)
                .clickable { onPreviousMonth() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Previous Month",
                tint = MizanTheme.premium.text.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        // Month/Year Title
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = currentMonth.format(monthFormatter),
                style = MizanTheme.typography.headingMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$daysWithTransactions days with transactions",
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.tertiary
            )
        }

        // Next Month Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface3)
                .clickable { onNextMonth() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Next Month",
                tint = MizanTheme.premium.text.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    calendarDays: List<TransactionsHubStore.CalendarDaySummary?>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

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
            .padding(MizanTheme.premium.spacing.sm)
    ) {
        // Weekday Headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Calendar Days Grid
        val rows = calendarDays.chunked(7)
        rows.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Pad week to 7 days if needed
                val paddedWeek = week + List(7 - week.size) { null }
                paddedWeek.forEach { daySummary ->
                    CalendarDayCell(
                        daySummary = daySummary,
                        isSelected = daySummary?.date == selectedDate,
                        isToday = daySummary?.date == LocalDate.now(),
                        onClick = { daySummary?.date?.let { onDateSelected(it) } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    daySummary: TransactionsHubStore.CalendarDaySummary?,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasTransactions = daySummary?.transactionCount ?: 0 > 0

    val backgroundColor = when {
        isSelected -> MizanTheme.premium.colors.emerald.copy(alpha = 0.2f)
        isToday -> MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
        hasTransactions -> MizanTheme.premium.colors.surface3
        else -> MizanTheme.premium.colors.surface1
    }

    val borderColor = when {
        isSelected || isToday -> MizanTheme.premium.colors.emerald
        hasTransactions -> MizanTheme.premium.glass.border
        else -> Color.Transparent
    }

    val borderWidth = if (isSelected || isToday) 2.dp else 1.dp

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
            .background(backgroundColor)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(borderWidth, borderColor, RoundedCornerShape(MizanTheme.premium.radius.sm))
                } else {
                    Modifier
                }
            )
            .clickable(enabled = daySummary != null) { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (daySummary == null) {
            // Empty cell
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Day Number
                Text(
                    text = daySummary.date.dayOfMonth.toString(),
                    style = MizanTheme.typography.labelSm,
                    color = when {
                        isToday || isSelected -> MizanTheme.premium.colors.emerald
                        hasTransactions -> MizanTheme.premium.text.primary
                        else -> MizanTheme.premium.text.muted
                    },
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                )

                if (hasTransactions) {
                    // Income
                    if (daySummary.income > 0) {
                        Text(
                            text = "+${formatCompactAmount(daySummary.income)}",
                            style = MizanTheme.typography.labelSm,
                            color = MizanTheme.premium.colors.emerald,
                            fontSize = 8.sp,
                            maxLines = 1
                        )
                    }

                    // Expense
                    if (daySummary.expense > 0) {
                        Text(
                            text = "-${formatCompactAmount(daySummary.expense)}",
                            style = MizanTheme.typography.labelSm,
                            color = Color(0xFFF5576C),
                            fontSize = 8.sp,
                            maxLines = 1
                        )
                    }

                    // Balance
                    Text(
                        text = formatCompactAmount(kotlin.math.abs(daySummary.balance)),
                        style = MizanTheme.typography.labelSm,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp,
                        maxLines = 1
                    )
                } else {
                    // Empty day indicator
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(MizanTheme.premium.text.muted.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarMonthlySummary(
    calendarDays: List<TransactionsHubStore.CalendarDaySummary?>,
    modifier: Modifier = Modifier
) {
    val totalIncome = calendarDays.filterNotNull().sumOf { it.income }
    val totalExpense = calendarDays.filterNotNull().sumOf { it.expense }
    val balance = totalIncome - totalExpense

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
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Text(
            text = "MONTHLY SUMMARY",
            style = MizanTheme.typography.labelSm,
            color = MizanTheme.premium.text.tertiary,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Income
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Income",
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary
                )
                Text(
                    text = "$${formatCompactAmount(totalIncome)}",
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.colors.emerald,
                    fontWeight = FontWeight.Bold
                )
            }

            // Expense
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Expense",
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary
                )
                Text(
                    text = "$${formatCompactAmount(totalExpense)}",
                    style = MizanTheme.typography.bodyMd,
                    color = Color(0xFFF5576C),
                    fontWeight = FontWeight.Bold
                )
            }

            // Balance
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Balance",
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary
                )
                Text(
                    text = "${if (balance >= 0) "+" else "-"}$${formatCompactAmount(kotlin.math.abs(balance))}",
                    style = MizanTheme.typography.bodyMd,
                    color = if (balance >= 0) MizanTheme.premium.colors.emerald else Color(0xFFF5576C),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CalendarTransactionItem(
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
            try { Color(android.graphics.Color.parseColor(it)) }
            catch (e: Exception) { Color(0xFFFF6B9D) }
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
                .size(40.dp)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                .background(categoryColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = getCategoryIconForCalendar(category?.iconName)),
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier.size(20.dp)
            )
        }

        // Transaction Details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MizanTheme.premium.spacing.sm)
        ) {
            Text(
                text = transaction.description ?: category?.name ?: "Transaction",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${category?.name ?: "Uncategorized"} • ${account?.name ?: "Unknown"}",
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
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatCompactAmount(amount: Double): String {
    return when {
        amount >= 1000000 -> "${(amount / 1000000).let { if (it == it.toLong().toDouble()) it.toLong().toString() else String.format("%.1f", it) }}M"
        amount >= 1000 -> "${(amount / 1000).let { if (it == it.toLong().toDouble()) it.toLong().toString() else String.format("%.1f", it) }}k"
        else -> amount.toLong().toString()
    }
}

private fun getCategoryIconForCalendar(iconName: String?): Int {
    return when (iconName?.lowercase()) {
        "food", "food-dining", "restaurant", "utensils" -> R.drawable.ic_utensils
        "transport", "transportation", "car" -> R.drawable.ic_car
        "shopping", "shop", "bag" -> R.drawable.ic_shopping_bag
        "bills", "bills-utilities", "home" -> R.drawable.ic_home
        "entertainment", "coffee" -> R.drawable.ic_coffee
        "health", "healthcare" -> R.drawable.ic_heart
        "income", "salary", "trending-up" -> R.drawable.ic_trending_up
        else -> R.drawable.ic_wallet
    }
}
