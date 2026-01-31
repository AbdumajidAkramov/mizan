package dev.esbi.mizan.feature.transactionshub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.feature.transactionshub.ui.components.DailyTransactionGroup
import dev.esbi.mizan.feature.transactionshub.ui.components.SummaryCardsSection
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubCalendarTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubDescriptionTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubHeader
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubMonthlyTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubSummaryTab
import dev.esbi.mizan.feature.transactionshub.ui.components.TransactionsHubTabRow
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import kotlin.math.roundToInt

/**
 * Main content composable for TransactionsHub screen
 */
@Composable
fun TransactionsHubContent(
    state: TransactionsHubStore.State,
    onIntent: (TransactionsHubStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    
    // Track the height of the summary cards section
    var summaryHeightPx by remember { mutableFloatStateOf(0f) }
    
    // Track the current offset (how much the summary is collapsed)
    var headerOffset by remember { mutableFloatStateOf(0f) }
    
    // NestedScrollConnection to handle collapsing behavior
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = headerOffset + delta
                // Clamp the offset between -summaryHeight (fully collapsed) and 0 (fully expanded)
                headerOffset = newOffset.coerceIn(-summaryHeightPx, 0f)
                // Consume the scroll if we're still collapsing/expanding
                return if (headerOffset > -summaryHeightPx && headerOffset < 0f) {
                    Offset(0f, delta)
                } else {
                    Offset.Zero
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TransactionsHubHeader(
                currentMonth = state.currentMonth,
                onBackClick = { onIntent(TransactionsHubStore.Intent.BackClicked) },
                onAddClick = { onIntent(TransactionsHubStore.Intent.AddTransactionClicked) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MizanTheme.premium.background.primary)
                .nestedScroll(nestedScrollConnection)
        ) {
            // Collapsible Summary Cards Section
            SummaryCardsSection(
                summary = state.summary,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, headerOffset.roundToInt()) }
                    .onSizeChanged { size ->
                        summaryHeightPx = size.height.toFloat()
                    }
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .padding(vertical = MizanTheme.premium.spacing.md)
                    .zIndex(0f)
            )

            // Sticky Tab Row + Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        // Tab row follows the summary cards but stops at top
                        val tabOffset = (summaryHeightPx + headerOffset).coerceAtLeast(0f)
                        IntOffset(0, tabOffset.roundToInt())
                    }
            ) {
                // Sticky Tab Row
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MizanTheme.premium.background.primary)
                        .zIndex(1f)
                ) {
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
                }

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
                                    onDateSelected = { date ->
                                        onIntent(TransactionsHubStore.Intent.SelectDate(date))
                                    },
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
                                    weeklySummaries = state.weeklySummaries,
                                    expenseAccountSummaries = state.expenseAccountSummaries,
                                    incomeAccountSummaries = state.incomeAccountSummaries,
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
