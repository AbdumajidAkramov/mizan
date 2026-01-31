package dev.esbi.mizan.feature.transactionshub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.feature.transactionshub.ui.components.DailyTransactionGroup
import dev.esbi.mizan.feature.transactionshub.ui.components.SummaryCardsSection
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubCalendarTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubHeader
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubMonthlyTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubSummaryTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubTabRow
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubDescriptionTab
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * Main content composable for TransactionsHub screen
 */
@Composable
fun TransactionsHubContent(
    state: TransactionsHubStore.State,
    onIntent: (TransactionsHubStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
    ) {
        // Header with title and add button
        TransactionsHubHeader(
            currentMonth = state.currentMonth,
            onBackClick = { onIntent(TransactionsHubStore.Intent.BackClicked) },
            onAddClick = { onIntent(TransactionsHubStore.Intent.AddTransactionClicked) }
        )

        // Summary Cards (Income, Expense, Total)
        SummaryCardsSection(
            summary = state.summary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.lg)
                .padding(vertical = MizanTheme.premium.spacing.md)
        )

        // Tab Row
        TransactionsHubTabRow(
            selectedTab = state.selectedTab,
            onTabSelected = { tab ->
                onIntent(TransactionsHubStore.Intent.SelectTab(tab))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.lg)
                .padding(bottom = MizanTheme.premium.spacing.md)
        )

        HorizontalDivider(
            color = MizanTheme.premium.glass.border,
            thickness = 1.dp
        )

        // Content based on selected tab
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MizanTheme.premium.colors.emerald
                    )
                }
            }
            else -> {
                when (state.selectedTab) {
                    TransactionsHubStore.Tab.Daily -> {
                        DailyTransactionsList(
                            dailyGroups = state.dailyGroups,
                            accounts = state.accounts,
                            categories = state.categories,
                            onTransactionClick = { transaction ->
                                onIntent(TransactionsHubStore.Intent.TransactionClicked(transaction))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    TransactionsHubStore.Tab.Calendar -> {
                        TransactionsHubCalendarTab(
                            currentMonth = state.currentMonth,
                            calendarDays = state.calendarDays,
                            daysWithTransactions = state.daysWithTransactions,
                            selectedDate = state.selectedDate,
                            transactions = state.transactions,
                            accounts = state.accounts,
                            categories = state.categories,
                            onPreviousMonth = { onIntent(TransactionsHubStore.Intent.PreviousMonth) },
                            onNextMonth = { onIntent(TransactionsHubStore.Intent.NextMonth) },
                            onDateSelected = { date -> onIntent(TransactionsHubStore.Intent.SelectDate(date)) },
                            onTransactionClick = { transaction ->
                                onIntent(TransactionsHubStore.Intent.TransactionClicked(transaction))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    TransactionsHubStore.Tab.Monthly -> {
                        TransactionsHubMonthlyTab(
                            currentMonth = state.currentMonth,
                            weeklySummaries = state.weeklySummaries,
                            summary = state.summary,
                            transactionCount = state.transactions.size,
                            accounts = state.accounts,
                            categories = state.categories,
                            onPreviousMonth = { onIntent(TransactionsHubStore.Intent.PreviousMonth) },
                            onNextMonth = { onIntent(TransactionsHubStore.Intent.NextMonth) },
                            onToggleWeek = { weekNumber ->
                                onIntent(TransactionsHubStore.Intent.ToggleWeekExpansion(weekNumber))
                            },
                            onTransactionClick = { transaction ->
                                onIntent(TransactionsHubStore.Intent.TransactionClicked(transaction))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    TransactionsHubStore.Tab.Summary -> {
                        TransactionsHubSummaryTab(
                            currentMonth = state.currentMonth,
                            summary = state.summary,
                            expenseCategorySummaries = state.expenseCategorySummaries,
                            incomeCategorySummaries = state.incomeCategorySummaries,
                            savingsRate = state.savingsRate,
                            transactionCount = state.transactions.size,
                            onPreviousMonth = { onIntent(TransactionsHubStore.Intent.PreviousMonth) },
                            onNextMonth = { onIntent(TransactionsHubStore.Intent.NextMonth) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    TransactionsHubStore.Tab.Description -> {
                        TransactionsHubDescriptionTab(
                            descriptionGroups = state.descriptionGroups,
                            searchQuery = state.descriptionSearchQuery,
                            isAllExpanded = state.isAllDescriptionsExpanded,
                            totalTransactionCount = state.transactions.size,
                            accounts = state.accounts,
                            categories = state.categories,
                            onSearchQueryChanged = { query ->
                                onIntent(TransactionsHubStore.Intent.SearchDescription(query))
                            },
                            onToggleGroup = { description ->
                                onIntent(TransactionsHubStore.Intent.ToggleDescriptionGroup(description))
                            },
                            onToggleExpandAll = {
                                onIntent(TransactionsHubStore.Intent.ToggleExpandAllDescriptions)
                            },
                            onTransactionClick = { transaction ->
                                onIntent(TransactionsHubStore.Intent.TransactionClicked(transaction))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyTransactionsList(
    dailyGroups: List<TransactionsHubStore.DailyGroup>,
    accounts: List<dev.esbi.mizan.domain.model.Account>,
    categories: List<dev.esbi.mizan.domain.model.Category>,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (dailyGroups.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No transactions yet",
                    style = MizanTheme.typography.bodyLg,
                    color = MizanTheme.premium.text.secondary
                )
                Text(
                    text = "Add your first transaction to get started",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            dailyGroups.forEach { group ->
                item(key = "header_${group.date}") {
                    DailyTransactionGroup(
                        group = group,
                        accounts = accounts,
                        categories = categories,
                        onTransactionClick = onTransactionClick
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceholderContent(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MizanTheme.typography.headingMd,
            color = MizanTheme.premium.text.tertiary
        )
    }
}
