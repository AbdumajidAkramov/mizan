package dev.esbi.mizan.feature.transactionshub.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
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
import dev.esbi.mizan.design.theme.colors.MizanTheme

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
    // 1. Karta ko'rinishi uchun holat
    var isSummaryVisible by remember { mutableStateOf(true) }

    // 2. Scroll eventlarini ushlab olish uchun NestedScrollConnection
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                // Ro'yxat pastga tushganda (barmoq tepaga surilganda)
                if (delta < -15f) {
                    isSummaryVisible = false
                }
                // Ro'yxat tepaga chiqqanda (barmoq pastga surilganda)
                else if (delta > 15f) {
                    isSummaryVisible = true
                }
                return Offset.Zero
            }
        }
    }
    Scaffold(
        topBar = {
            TransactionsHubHeader(
                currentMonth = state.currentMonth,
                onBackClick = { onIntent(TransactionsHubStore.Intent.BackClicked) },
                onAddClick = {
                    onIntent(TransactionsHubStore.Intent.AddTransactionClicked)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MizanTheme.premium.background.primary)
                .nestedScroll(nestedScrollConnection) // Scrollni butun ekranga ulaymiz
        ) {
            // ── Fixed header sections ────────────────────────────
            // 3. Animatsiya bilan yashirinadigan Summary Cards
            AnimatedVisibility(
                visible = isSummaryVisible,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(
                    animationSpec = tween(
                        300
                    )
                ),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(
                    animationSpec = tween(
                        300
                    )
                )
            ) {
                // Summary Cards (Income / Expense / Total)
                SummaryCardsSection(
                    summary = state.summary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MizanTheme.premium.spacing.md)

                )
            }

            // Global Time Selector (← Month Year →)
            GlobalTimeSelector(
                currentMonth = state.currentMonth,
                daysWithTransactions = state.daysWithTransactions,
                onPreviousMonth = { onIntent(TransactionsHubStore.Intent.PreviousMonth) },
                onNextMonth = { onIntent(TransactionsHubStore.Intent.NextMonth) },
                onMonthSelected = {
                    onIntent(TransactionsHubStore.Intent.OnChangeTransactionMonth(it))
                }
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
                    .padding(vertical = MizanTheme.premium.spacing.md)
            )

            HorizontalDivider(
                color = MizanTheme.premium.glass.border,
                thickness = 1.dp
            )

            // ── Scrollable tab content ───────────────────────────
            // 1. Swipe masofasini saqlash uchun state
            var swipeOffset by remember { mutableFloatStateOf(0f) }
            val swipeThreshold = 150f // Swipe sezgirligi (qancha masofaga surilganda ishlashi)
            // 2. Barcha tab contentlarini o'rab turadigan Box va unga ulanadigan Swipe Modifier
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ekranning qolgan barcha qismini egallaydi
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                when {
                                    swipeOffset > swipeThreshold -> {
                                        // O'ngga swipe -> Oldingi oy (Previous Month)
                                        onIntent(TransactionsHubStore.Intent.PreviousMonth)
                                    }

                                    swipeOffset < -swipeThreshold -> {
                                        // Chapga swipe -> Keyingi oy (Next Month)
                                        onIntent(TransactionsHubStore.Intent.NextMonth)
                                    }
                                }
                                swipeOffset = 0f // Har swipe tugaganda qiymatni tozalaymiz
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                // Barmog'ing bilan qancha surilganini yig'ib boramiz
                                swipeOffset += dragAmount
                            }
                        )
                    }
            ) {
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
                            modifier = Modifier
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
                            modifier = Modifier
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
                            modifier = Modifier
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
                            modifier = Modifier
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
                            modifier = Modifier
                        )
                    }
                }

                /*
                                when {
                                    state.isLoading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
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
                                                    modifier = Modifier
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
                                                    modifier = Modifier
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
                                                    modifier = Modifier
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
                                                    modifier = Modifier
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
                                                    modifier = Modifier
                                                )
                                            }
                                        }
                                    }
                                }
                */
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
