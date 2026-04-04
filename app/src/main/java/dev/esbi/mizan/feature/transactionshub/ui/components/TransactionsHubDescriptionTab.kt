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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Description Tab for TransactionsHub
 * Groups transactions by their description/note field
 */
@Composable
fun TransactionsHubDescriptionTab(
    descriptionGroups: List<TransactionsHubStore.DescriptionGroup>,
    searchQuery: String,
    isAllExpanded: Boolean,
    totalTransactionCount: Int,
    accounts: List<Account>,
    categories: List<Category>,
    onSearchQueryChanged: (String) -> Unit,
    onToggleGroup: (String) -> Unit,
    onToggleExpandAll: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Sticky Header - Search & Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MizanTheme.premium.background.primary)
                .padding(MizanTheme.premium.spacing.md)
        ) {
            // Search Bar
            DescriptionSearchBar(
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged
            )

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

            // Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Results Count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
                ) {
                    Text(
                        text = "${descriptionGroups.size} group${if (descriptionGroups.size != 1) "s" else ""}",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.secondary
                    )
                    Text(
                        text = "•",
                        style = MizanTheme.typography.labelSm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Text(
                        text = "$totalTransactionCount total",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.secondary
                    )
                }

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
                ) {
                    // Expand/Collapse All
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                            .background(MizanTheme.premium.colors.surface2)
                            .clickable { onToggleExpandAll() }
                            .padding(
                                horizontal = MizanTheme.premium.spacing.sm,
                                vertical = MizanTheme.premium.spacing.xs
                            )
                    ) {
                        Text(
                            text = if (isAllExpanded) "Collapse All" else "Expand All",
                            style = MizanTheme.typography.labelSm,
                            color = MizanTheme.premium.text.secondary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Sort Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                            .background(MizanTheme.premium.colors.surface2)
                            .padding(
                                horizontal = MizanTheme.premium.spacing.sm,
                                vertical = MizanTheme.premium.spacing.xs
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = Icons.ic_list),
                            contentDescription = null,
                            tint = MizanTheme.premium.text.secondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Sort",
                            style = MizanTheme.typography.labelSm,
                            color = MizanTheme.premium.text.secondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Description Groups List
        if (descriptionGroups.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MizanTheme.premium.spacing.xl),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = Icons.ic_search),
                        contentDescription = null,
                        tint = MizanTheme.premium.text.tertiary.copy(alpha = 0.4f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))
                    Text(
                        text = if (searchQuery.isNotBlank()) "No descriptions found" else "No transactions",
                        style = MizanTheme.typography.bodyLg,
                        color = MizanTheme.premium.text.secondary,
                        fontWeight = FontWeight.Medium
                    )
                    if (searchQuery.isNotBlank()) {
                        Text(
                            text = "Try a different search term",
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MizanTheme.premium.spacing.md),
                verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
            ) {
                item { Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xs)) }

                items(descriptionGroups, key = { it.description }) { group ->
                    DescriptionGroupCard(
                        group = group,
                        accounts = accounts,
                        categories = categories,
                        onToggle = { onToggleGroup(group.description) },
                        onTransactionClick = onTransactionClick
                    )
                }

                item { Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg)) }
            }
        }
    }
}

@Composable
private fun DescriptionSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
            .padding(
                horizontal = MizanTheme.premium.spacing.md,
                vertical = MizanTheme.premium.spacing.sm
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = Icons.ic_search),
            contentDescription = null,
            tint = MizanTheme.premium.text.tertiary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

        BasicTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            textStyle = MizanTheme.typography.bodyMd.copy(
                color = MizanTheme.premium.text.primary
            ),
            cursorBrush = SolidColor(MizanTheme.premium.colors.emerald),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search in descriptions...",
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (query.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface3)
                    .clickable { onQueryChanged("") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = Icons.ic_close),
                    contentDescription = "Clear",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun DescriptionGroupCard(
    group: TransactionsHubStore.DescriptionGroup,
    accounts: List<Account>,
    categories: List<Category>,
    onToggle: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val isPositive = group.netAmount >= java.math.BigDecimal.ZERO

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.colors.surface2)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
    ) {
        // Group Header - Tappable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(MizanTheme.premium.spacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Chevron + Description Info
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
            ) {
                // Chevron
                Icon(
                    painter = painterResource(
                        id = if (group.isExpanded) Icons.ic_chevron_down else Icons.ic_chevron_right
                    ),
                    contentDescription = null,
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(20.dp)
                )

                // Description Details
                Column(modifier = Modifier.weight(1f)) {
                    // Description Title
                    Text(
                        text = group.description.ifEmpty { "(No description)" },
                        style = MizanTheme.typography.headingMd,
                        color = MizanTheme.premium.text.primary,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Meta Info Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
                    ) {
                        // Transaction Count
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(MizanTheme.premium.text.tertiary)
                            )
                            Text(
                                text = "${group.transactionCount} transaction${if (group.transactionCount != 1) "s" else ""}",
                                style = MizanTheme.typography.labelSm,
                                color = MizanTheme.premium.text.tertiary
                            )
                        }

                        Text(
                            text = "•",
                            style = MizanTheme.typography.labelSm,
                            color = MizanTheme.premium.text.tertiary
                        )

                        // Date Range
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = Icons.ic_calendar),
                                contentDescription = null,
                                tint = MizanTheme.premium.text.tertiary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = group.dateRange,
                                style = MizanTheme.typography.labelSm,
                                color = MizanTheme.premium.text.tertiary
                            )
                        }
                    }

                    // Income/Expense Breakdown (if both exist)
                    if (group.incomeAmount > java.math.BigDecimal.ZERO && group.expenseAmount > java.math.BigDecimal.ZERO) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = Icons.ic_trending_up),
                                    contentDescription = null,
                                    tint = MizanTheme.premium.colors.emerald,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "+${formatCompactAmountDescription(group.incomeAmount.toDouble())}",
                                    style = MizanTheme.typography.labelSm,
                                    color = MizanTheme.premium.colors.emerald
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = Icons.ic_trending_down),
                                    contentDescription = null,
                                    tint = Color(0xFFF5576C),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "-${formatCompactAmountDescription(group.expenseAmount.toDouble())}",
                                    style = MizanTheme.typography.labelSm,
                                    color = Color(0xFFF5576C)
                                )
                            }
                        }
                    }
                }
            }

            // Right: Total Amount
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (group.netAmount >= java.math.BigDecimal.ZERO) "+" else ""}${formatCompactAmountDescription(group.netAmount.abs().toDouble())}",
                    style = MizanTheme.typography.headingLg,
                    color = if (isPositive) MizanTheme.premium.colors.emerald else Color(0xFFF5576C),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (group.incomeAmount > java.math.BigDecimal.ZERO && group.expenseAmount > java.math.BigDecimal.ZERO) "Total net" else "Total",
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        }

        // Expanded Transactions List
        AnimatedVisibility(
            visible = group.isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MizanTheme.premium.colors.surface1)
            ) {
                HorizontalDivider(
                    color = MizanTheme.premium.glass.border,
                    thickness = 1.dp
                )

                Column(modifier = Modifier.padding(MizanTheme.premium.spacing.sm)) {
                    group.transactions.forEachIndexed { index, transaction ->
                        // TODO: Implement DescriptionTransactionRow component
                        // DescriptionTransactionRow(
                        //     transaction = transaction,
                        //     accounts = accounts,
                        //     categories = categories,
                        //     onClick = { onTransactionClick(transaction) }
                        // )
                        if (index < group.transactions.size - 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun formatCompactAmountDescription(amount: Double): String {
    return formatDescriptionAmount(amount)
}

private fun formatDescriptionAmount(amount: Double): String {
    return when {
        amount >= 1000000 -> "${
            (amount / 1000000).let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else String.format(
                    "%.1f",
                    it
                )
            }
        }M"

        amount >= 10000 -> "${
            (amount / 1000).let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else String.format(
                    "%.1f",
                    it
                )
            }
        }k"

        amount >= 1000 -> String.format("%,.0f", amount)
        else -> String.format("%.2f", amount)
    }
}

private fun getCategoryIconDescription(iconName: String?): Int {
    return when (iconName?.lowercase()) {
        "utensils", "food" -> Icons.ic_utensils
        "car", "transport" -> Icons.ic_car
        "shopping", "bag" -> Icons.ic_shopping_bag
        "coffee" -> Icons.ic_coffee
        "heart", "health" -> Icons.ic_heart
        else -> Icons.ic_list
    }
}
