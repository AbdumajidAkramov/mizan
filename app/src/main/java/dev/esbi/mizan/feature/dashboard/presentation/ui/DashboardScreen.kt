package dev.esbi.mizan.feature.dashboard.presentation.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModelFactory
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore
import dev.esbi.mizan.feature.dashboard.presentation.widgets.AnimSection
import dev.esbi.mizan.feature.dashboard.presentation.widgets.CashFlowCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.CategoriesSection
import dev.esbi.mizan.feature.dashboard.presentation.widgets.ChartCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.EmergencyCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.HealthCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.InsightsSection
import dev.esbi.mizan.feature.dashboard.presentation.widgets.LoadingContent
import dev.esbi.mizan.feature.dashboard.presentation.widgets.NetWorthCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.StatsRow
import dev.esbi.mizan.feature.dashboard.presentation.widgets.TransactionsSection
import dev.esbi.mizan.feature.dashboard.presentation.widgets.balancecard.BalanceCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.header.HeaderSection
import dev.esbi.mizan.ui.components.ErrorState

//import dev.esbi.mizan.ui.theme.utils.primitiveColors

const val SLIDE_UP_DELAY_MS = 20

@Composable
fun DashboardScreen(
    viewModelFactory: DashboardViewModelFactory,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is DashboardStore.Label.NavigateToCategory -> onNavigateToCategory(label.categoryId)
                is DashboardStore.Label.ShowError -> {}
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
            onCategoryClick = { viewModel.onIntent(DashboardStore.Intent.CategoryClicked(it)) }
        )
    }
}

@Composable
private fun DashboardScrollContent(
    data: DashboardSummary,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
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
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                BalanceCard(
                    data.totalBalance,
                    data.totalBalance - data.monthlyExpenses + data.monthlySavings,
                    data.monthlyExpenses
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { HealthCard(78, 5) } }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { NetWorthCard(22450.0, 1245.0, 5.9) } }
        item {
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                CashFlowCard(
                    3850.0,
                    data.monthlyExpenses
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { EmergencyCard(8500.0, 12000.0, 6) } }
        item {
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                StatsRow(
                    data.budgetPercentageUsed,
                    data.monthlyExpenses,
                    data.budgetLimit,
                    data.monthlySavings
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { ChartCard(data.weeklySpending) } }
        item {
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                CategoriesSection(
                    data.topCategories,
                    onCategoryClick
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { InsightsSection() } }
        item {
            AnimSection(
                visible,
                SLIDE_UP_DELAY_MS
            ) { TransactionsSection(data.recentTransactions) }
        }

    }
}
