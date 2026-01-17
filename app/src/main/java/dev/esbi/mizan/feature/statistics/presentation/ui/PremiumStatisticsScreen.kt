package dev.esbi.mizan.feature.statistics.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.feature.dashboard.presentation.widgets.LoadingContent
import dev.esbi.mizan.feature.statistics.domain.model.CategoryData
import dev.esbi.mizan.feature.statistics.domain.model.ChartDataPoint
import dev.esbi.mizan.feature.statistics.domain.model.StatisticsSummary
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModel
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModelFactory
import dev.esbi.mizan.feature.statistics.presentation.store.StatisticsStore
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.CategoryBreakdownCard
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.ChartData
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.ComparisonCard
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.InsightsCard
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.MonthlyTrendChart
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.PeriodSelector
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.StatsHeader
import dev.esbi.mizan.feature.statistics.presentation.ui.widgets.TopCategoriesCard
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.utils.Icons

@Composable
fun PremiumStatisticsScreen(
    statisticsViewModelFactory: StatisticsViewModelFactory,
    modifier: Modifier = Modifier
) {
    val viewModel: StatisticsViewModel = viewModel(factory = statisticsViewModelFactory)
    val state by viewModel.state.collectAsState(initial = StatisticsStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is StatisticsStore.Label.ShowError -> {
                    // Handle error display
                }
            }
        }
    }
    when {
        state.isLoading && state.summary == null -> LoadingContent()
        state.error != null && state.summary == null -> ErrorState(
            message = state.error!!,
            onRetry = { viewModel.onIntent(StatisticsStore.Intent.Retry) },
            modifier = Modifier.fillMaxSize()
        )

        state.summary != null -> StatisticsContent(
            modifier = modifier,
            data = state.summary!!,
            onIntent = { }
        )
    }
}


@Composable
internal fun StatisticsContent(
    data: StatisticsSummary,
    onIntent: (StatisticsStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPeriod by remember { mutableStateOf(TimePeriod.MONTH) }

    val monthlyData = listOf(
        ChartDataPoint("Jan", 1450f),
        ChartDataPoint("Feb", 1820f),
        ChartDataPoint("Mar", 1650f),
        ChartDataPoint("Apr", 2100f),
        ChartDataPoint("May", 1880f),
        ChartDataPoint("Jun", 2350f)
    )

    val categoryData = listOf(
        CategoryData("Bills & Utilities", 205f, 30f, Color(0xFF00D2FF)),
        CategoryData("Shopping", 300f, 26f, Color(0xFFFFA34D)),
        CategoryData("Food & Dining", 186f, 20f, Color(0xFFFF6B9D)),
        CategoryData("Transport", 98f, 12f, Color(0xFF4FACFE)),
        CategoryData("Health & Fitness", 107f, 12f, Color(0xFFFF6B6B))
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            StatsHeader()
        }

        item {
            PeriodSelector(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ComparisonCard(
                    modifier = Modifier.weight(1f),
                    label = "Income",
                    amount = "$3,850",
                    change = "+12.5%",
                    isPositive = true,
                    gradientColors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
                    icon = IconValue(Icons.ic_trend_up)
                )
                ComparisonCard(
                    modifier = Modifier.weight(1f),
                    label = "Expenses",
                    amount = "$1,750",
                    change = "-8.3%",
                    isPositive = false,
                    gradientColors = listOf(Color(0xFFFF6B6B), Color(0xFFF5576C)),
                    icon = IconValue(Icons.ic_down_trend)
                )
            }
        }

        item {
            val data = listOf(
                ChartData("Jan", 1400f),
                ChartData("Feb", 1820f),
                ChartData("Mar", 1600f),
                ChartData("Apr", 2100f),
                ChartData("May", 1900f),
                ChartData("Jun", 2400f)
            )
            MonthlyTrendChart(data = data)
        }

        item {
            CategoryBreakdownCard(categories = categoryData)
        }

        item {
            TopCategoriesCard(categoryData = categoryData)
        }

        item {
            InsightsCard()
        }
    }
}
