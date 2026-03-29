package dev.esbi.mizan.feature.transactionshub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.transactionshub.ui.components.DailyTransactionGroup
import dev.esbi.mizan.feature.transactionshub.ui.components.GlobalTimeSelector
import dev.esbi.mizan.feature.transactionshub.ui.components.SummaryCardsSection
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubCalendarTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubDescriptionTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubHeader
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubMonthlyTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubSummaryTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubTabRow
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * Main content composable for TransactionsHub screen
 *
 * Layout hierarchy (matches PremiumTransactionsHubScreen.tsx):
 * 1. Top App Bar (fixed)
 * 2. Summary Cards – Income / Expense / Total (fixed)
 * 3. GlobalTimeSelector – ← Month Year → (fixed)
 * 4. Tab Row (fixed)
 * 5. Tab Content (scrollable, fills remaining space)
 */
@Composable
fun TransactionsHubContent(
    state: TransactionsHubStore.State,
    onIntent: (TransactionsHubStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TransactionsHubHeader(
                currentMonth = state.currentMonth,
                onBackClick = { onIntent(TransactionsHubStore.Intent.BackClicked) },
                onAddClick = { onIntent(TransactionsHubStore.Intent.AddTransactionClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MizanTheme.premium.background.primary)
        ) {
            // ── Fixed header sections ────────────────────────────

            // Summary Cards (Income / Expense / Total)
            SummaryCardsSection(
                summary = state.summary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .padding(vertical = MizanTheme.premium.spacing.md)
            )

            // Global Time Selector (← Month Year →)
            GlobalTimeSelector(
                currentMonth = state.currentMonth,
                daysWithTransactions = state.daysWithTransactions,
                onPreviousMonth = { onIntent(TransactionsHubStore.Intent.PreviousMonth) },
                onNextMonth = { onIntent(TransactionsHubStore.Intent.NextMonth) }
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
                    .padding(vertical = MizanTheme.premium.spacing.sm)
            )

            HorizontalDivider(
                color = MizanTheme.premium.glass.border,
                thickness = 1.dp
            )

            // ── Scrollable tab content ───────────────────────────

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
                                    onIntent(
                                        TransactionsHubStore.Intent.TransactionClicked(
                                            transaction
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        TransactionsHubStore.Tab.Calendar -> {
                            TransactionsHubCalendarTab(
                                calendarDays = state.calendarDays,
                                selectedDate = state.selectedDate,
                                transactions = state.transactions,
                                accounts = state.accounts,
                                categories = state.categories,
                                onDateSelected = { date ->
                                    onIntent(TransactionsHubStore.Intent.SelectDate(date))
                                },
                                onTransactionClick = { transaction ->
                                    onIntent(
                                        TransactionsHubStore.Intent.TransactionClicked(
                                            transaction
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        TransactionsHubStore.Tab.Monthly -> {
                            TransactionsHubMonthlyTab(
                                weeklySummaries = state.weeklySummaries,
                                accounts = state.accounts,
                                categories = state.categories,
                                onToggleWeek = { weekNumber ->
                                    onIntent(
                                        TransactionsHubStore.Intent.ToggleWeekExpansion(
                                            weekNumber
                                        )
                                    )
                                },
                                onTransactionClick = { transaction ->
                                    onIntent(
                                        TransactionsHubStore.Intent.TransactionClicked(
                                            transaction
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        TransactionsHubStore.Tab.Summary -> {
                            TransactionsHubSummaryTab(
                                summary = state.summary,
                                expenseCategorySummaries = state.expenseCategorySummaries,
                                incomeCategorySummaries = state.incomeCategorySummaries,
                                weeklySummaries = state.weeklySummaries,
                                expenseAccountSummaries = state.expenseAccountSummaries,
                                incomeAccountSummaries = state.incomeAccountSummaries,
                                savingsRate = state.savingsRate,
                                transactionCount = state.transactions.size,
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
                                    onIntent(
                                        TransactionsHubStore.Intent.ToggleDescriptionGroup(
                                            description
                                        )
                                    )
                                },
                                onToggleExpandAll = {
                                    onIntent(TransactionsHubStore.Intent.ToggleExpandAllDescriptions)
                                },
                                onTransactionClick = { transaction ->
                                    onIntent(
                                        TransactionsHubStore.Intent.TransactionClicked(
                                            transaction
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
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
