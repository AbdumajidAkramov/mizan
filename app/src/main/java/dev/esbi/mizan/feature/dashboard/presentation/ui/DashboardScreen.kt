package dev.esbi.mizan.feature.dashboard.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.dashboard.DashboardSummary
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.widgets.BalanceCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.HeaderSection
import dev.esbi.mizan.feature.dashboard.presentation.widgets.HealthCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.InsightsSection
import dev.esbi.mizan.feature.dashboard.presentation.widgets.LoadingContent
import dev.esbi.mizan.feature.dashboard.presentation.widgets.StatsRow
import dev.esbi.mizan.feature.dashboard.presentation.widgets.TransactionsSection
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.CashFlowDataPoint
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.CategorySpending
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.ChartDataPoint
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.PremiumCashFlowCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.PremiumEmergencyFund
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.PremiumNetWorthCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.PremiumSpendingChart
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.PremiumTopCategories
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.SpendingPoint
import dev.esbi.mizan.presentation.feature.currencymanagement.CurrencyFormatter
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStore
import dev.esbi.mizan.ui.animation.AnimSection
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.account.PremiumTotalBalanceCard
import java.math.BigDecimal


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToNewTransaction: () -> Unit = {},
    onNavigateToTransactionsHub: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToSubscriptions: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    // --- Notification Permission Request (Android 13+) ---
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* granted or denied — no action needed for now */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(permission)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is DashboardStore.Label.NavigateToCategory -> onNavigateToCategory(label.categoryId)
                is DashboardStore.Label.ShowError -> {}
                is DashboardStore.Label.NavigateToNewTransaction -> onNavigateToNewTransaction()
                is DashboardStore.Label.NavigateToTransactionsHub -> onNavigateToTransactionsHub()
                is DashboardStore.Label.NavigateToProfile -> onNavigateToProfile()
            }
        }
    }
    when {
        state.isLoading && state.dashboardData == null -> LoadingContent()
        state.error != null && state.dashboardData == null -> ErrorState(
            message = state.error!!,
            onRetry = { viewModel.onIntent(DashboardStore.Intent.Retry) },
            modifier = Modifier.fillMaxSize()
        )

        state.dashboardData != null -> DashboardScrollContent(
            modifier = modifier,
            data = state.dashboardData!!,
            totalBalance = state.totalBalance,
            mainCurrency = state.mainCurrency,
            onCategoryClick = { viewModel.onIntent(DashboardStore.Intent.CategoryClicked(it)) },
            onAddTransactionClick = { viewModel.onIntent(DashboardStore.Intent.AddTransactionClicked) },
            onSeeAllTransactions = { viewModel.onIntent(DashboardStore.Intent.ViewAllTransactionsClicked) },
            onNavigateToGoals = onNavigateToGoals,
            onNavigateToSubscriptions = onNavigateToSubscriptions,
            onNavigateToTransfer = onNavigateToTransfer
        )
    }
}

@Composable
private fun DashboardScrollContent(
    data: DashboardSummary,
    totalBalance: BigDecimal,
    mainCurrency: Currency?,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onAddTransactionClick: () -> Unit = {},
    onSeeAllTransactions: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToSubscriptions: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { AnimSection(visible, 0) { HeaderSection() } }

        item {
            AnimSection(visible) {
                val displayCurrency = mainCurrency ?: Currency.UZS
                val formattedTotal = CurrencyFormatter.format(totalBalance, displayCurrency, isAbbreviated = false)
                val abbreviatedTotal = CurrencyFormatter.format(totalBalance, displayCurrency, isAbbreviated = true)
                BalanceCard(
                    total = data.totalBalance,
                    income = data.totalBalance - data.monthlyExpenses + data.monthlySavings,
                    expenses = data.monthlyExpenses,
                    mainCurrency = displayCurrency.code
                )
                PremiumTotalBalanceCard(
                    balance = totalBalance,
                    monthlyChange = data.monthlySavings.toDouble() - data.monthlyExpenses.toDouble(),
                    monthlyChangePercent = data.budgetPercentageUsed,
                    currency = displayCurrency.code,
                    formattedBalance = formattedTotal,
                    abbreviatedBalance = abbreviatedTotal,
                    enableLongPressPrecision = true
                )
            }
        }
        item { AnimSection(visible) { HealthCard(89, 10) } }
        item {
            AnimSection(visible) {
                PremiumNetWorthCard(
                    netWorth = 24500.00,
                    change = 1250.00,
                    changePercent = 5.4,
                    chartData = listOf(
                        ChartDataPoint(18500f),
                        ChartDataPoint(19200f),
                        ChartDataPoint(18800f),
                        ChartDataPoint(22450f)
                    )
                )
            }
        }
        item {
            AnimSection(visible) {
                PremiumCashFlowCard(
                    income = 4250.0,
                    expenses = 2800.0,
                    listOf(
                        CashFlowDataPoint("Week 1", 1950f, 520f),
                        CashFlowDataPoint("Week 2", 1900f, 680f),
                        CashFlowDataPoint("Week 3", 2900f, 720f),
                        CashFlowDataPoint("Week 4", 1000f, 685f),
                    )
                )
            }
        }
        item {
            AnimSection(visible) {
                PremiumEmergencyFund(
                    current = 16000.0,
                    goal = 16000.0,
                    targetMonths = 12
                )
            }
        }
        item {
            AnimSection(visible) {
                StatsRow(
                    data.budgetPercentageUsed,
                    data.monthlyExpenses,
                    data.budgetLimit,
                    data.monthlySavings
                )
            }
        }
        item {
            AnimSection(visible) {
                val mockData = listOf(
                    SpendingPoint(
                        "Mon",
                        185f
                    ),
                    SpendingPoint(
                        "Tue",
                        120f
                    ),
                    SpendingPoint(
                        "Wed",
                        120f
                    ), // Flat qism
                    SpendingPoint(
                        "Thu",
                        250f
                    ), // Cho'qqi
                    SpendingPoint(
                        "Fri",
                        20f
                    ),  // Past
                    SpendingPoint(
                        "Sat",
                        60f
                    ),
                    SpendingPoint(
                        "Sun",
                        170f
                    )
                )

                Box {
                    PremiumSpendingChart(data = mockData)
                }
//                ChartCard(data.weeklySpending)
            }
        }
        item {
            AnimSection(visible) {
                val mockCategories = listOf(
                    CategorySpending("Food", 850.0, Color(0xFFFF6B9D)),
                    CategorySpending("Transport", 420.0, Color(0xFF4FACFE)),
                    CategorySpending("Shopping", 340.0, Color(0xFFFFA34D)),
                    CategorySpending("Bills", 240.0, Color(0xFF00D2FF)),
                    CategorySpending("Others", 120.0, Color.Gray)
                )
                Box {
                    PremiumTopCategories(data = mockCategories)
                }
            }
        }
        item { AnimSection(visible) { InsightsSection() } }
        item {
            AnimSection(visible) {
                TransactionsSection(
                    data.recentTransactions,
                    onSeeAllClick = onSeeAllTransactions
                )
            }
        }
    }
}
