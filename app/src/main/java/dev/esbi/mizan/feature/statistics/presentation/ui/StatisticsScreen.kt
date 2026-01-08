package dev.esbi.mizan.feature.statistics.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModel
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModelFactory
import dev.esbi.mizan.feature.statistics.presentation.store.StatisticsStore
import dev.esbi.mizan.feature.statistics.presentation.ui.components.CategoryBreakdownList
import dev.esbi.mizan.feature.statistics.presentation.ui.components.InsightsCard
import dev.esbi.mizan.feature.statistics.presentation.ui.components.PeriodSelector
import dev.esbi.mizan.feature.statistics.presentation.ui.components.SimpleLineChart
import dev.esbi.mizan.feature.statistics.presentation.ui.components.SummaryStatsCards
import dev.esbi.mizan.ui.animation.FadeInUpAnimation
import dev.esbi.mizan.ui.animation.StaggeredFadeInUp
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.theme.PremiumColors

@Composable
fun StatisticsScreen(
    viewModelFactory: StatisticsViewModelFactory,
    modifier: Modifier = Modifier
) {
    val viewModel: StatisticsViewModel = viewModel(factory = viewModelFactory)
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

    StatisticsContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun StatisticsContent(
    state: StatisticsStore.State,
    onIntent: (StatisticsStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { paddingValues ->
        when {
            state.isLoading && state.summary == null -> {
                LoadingContent(modifier = modifier.padding(paddingValues))
            }

            state.error != null && state.summary == null -> {
                FadeInUpAnimation {
                    ErrorState(
                        message = state.error,
                        onRetry = { onIntent(StatisticsStore.Intent.Retry) },
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }

            state.summary != null -> {
                val summary = state.summary

                FadeInUpAnimation {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Header with Period Label
                        StaggeredFadeInUp(index = 0) {
                            Column {
                                Text(
                                    text = stringResource(R.string.statistics_title),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFF667EEA),
                                                    Color(0xFF764BA2)
                                                )
                                            )
                                        )
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = summary.periodLabel,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Period Selector
                        StaggeredFadeInUp(index = 1) {
                            PeriodSelector(
                                selectedPeriod = state.selectedPeriod,
                                onPeriodSelected = { period ->
                                    onIntent(StatisticsStore.Intent.SelectPeriod(period))
                                }
                            )
                        }

                        // Summary Stats
                        StaggeredFadeInUp(index = 2) {
                            SummaryStatsCards(comparison = summary.comparison)
                        }

                        // Spending Trend Chart
                        StaggeredFadeInUp(index = 3) {
                            PremiumCard(
                                variant = PremiumCardVariant.Glass,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.statistics_spending_trend),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PremiumColors.TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(R.string.statistics_spending_pattern),
                                        fontSize = 12.sp,
                                        color = PremiumColors.TextTertiary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    SimpleLineChart(data = summary.spendingTrend)
                                }
                            }
                        }

                        // Category Breakdown
                        StaggeredFadeInUp(index = 4) {
                            PremiumCard(
                                variant = PremiumCardVariant.Glass,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.statistics_category_breakdown),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PremiumColors.TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(R.string.statistics_spending_by_category),
                                        fontSize = 12.sp,
                                        color = PremiumColors.TextTertiary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    CategoryBreakdownList(categories = summary.categoryBreakdown)
                                }
                            }
                        }

                        // Insights
                        StaggeredFadeInUp(index = 5) {
                            InsightsCard(insights = summary.insights)
                        }

                        // Bottom spacing
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    FadeInUpAnimation {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            repeat(6) { index ->
                StaggeredFadeInUp(index = index, delayMillis = 60) {
                    LoadingSkeleton(height = if (index == 3) 220 else 120)
                }
            }
        }
    }
}
