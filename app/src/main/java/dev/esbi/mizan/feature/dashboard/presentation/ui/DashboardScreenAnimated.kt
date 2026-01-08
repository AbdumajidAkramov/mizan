package dev.esbi.mizan.feature.dashboard.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModelFactory
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore
import dev.esbi.mizan.feature.dashboard.presentation.ui.components.BudgetStatusCard
import dev.esbi.mizan.feature.dashboard.presentation.ui.components.CategoryItem
import dev.esbi.mizan.feature.dashboard.presentation.ui.components.PremiumBalanceCard
import dev.esbi.mizan.ui.animation.FadeInUpAnimation
import dev.esbi.mizan.ui.animation.StaggeredFadeInUp
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant

/**
 * Animated Dashboard Screen with entrance animations matching design/src/styles/premium-theme.css
 * - Main container: animate-fade-in-up (500ms ease-out)
 * - Cards: Staggered entrance with 80ms delay per card
 * - Progress bars: 500ms animated width changes
 * - Interactive elements: press scale (0.95) on touch
 */
@Composable
fun DashboardScreenAnimated(
    viewModelFactory: DashboardViewModelFactory,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is DashboardStore.Label.NavigateToCategory -> {
                    onNavigateToCategory(label.categoryId)
                }
                is DashboardStore.Label.ShowError -> {
                }
            }
        }
    }

    DashboardContentAnimated(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun DashboardContentAnimated(
    state: DashboardStore.State,
    onIntent: (DashboardStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.dashboardData == null -> {
            LoadingContentAnimated(modifier = modifier)
        }
        state.error != null && state.dashboardData == null -> {
            FadeInUpAnimation {
                ErrorState(
                    message = state.error,
                    onRetry = { onIntent(DashboardStore.Intent.Retry) },
                    modifier = modifier.fillMaxSize()
                )
            }
        }
        state.dashboardData != null -> {
            val data = state.dashboardData
            
            FadeInUpAnimation {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    StaggeredFadeInUp(index = 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.dashboard_welcome_back),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "User",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = " ${stringResource(R.string.dashboard_greeting_emoji)}",
                                        fontSize = 28.sp
                                    )
                                }
                            }
                            
                            Surface(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape),
                                color = Color.Transparent
                            ) {
                                androidx.compose.foundation.Canvas(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    drawCircle(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF667EEA),
                                                Color(0xFF764BA2)
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }

                    StaggeredFadeInUp(index = 1) {
                        PremiumBalanceCard(
                            totalBalance = data.totalBalance,
                            monthlyIncome = data.totalBalance - data.monthlyExpenses + data.monthlySavings,
                            monthlyExpenses = data.monthlyExpenses
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StaggeredFadeInUp(index = 2, modifier = Modifier.weight(1f)) {
                            BudgetStatusCard(
                                spentAmount = data.monthlyExpenses,
                                budgetLimit = data.budgetLimit,
                                percentageUsed = data.budgetPercentageUsed
                            )
                        }
                        
                        StaggeredFadeInUp(index = 3, modifier = Modifier.weight(1f)) {
                            PremiumCard(variant = PremiumCardVariant.Glass) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.dashboard_savings_this_month),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "$${data.monthlySavings.toInt()}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = stringResource(R.string.dashboard_savings_growth, 12.5),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF00F2FE)
                                    )
                                }
                            }
                        }
                    }

                    StaggeredFadeInUp(index = 4) {
                        PremiumCard(variant = PremiumCardVariant.Glass) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.dashboard_spending_overview),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = stringResource(R.string.dashboard_details),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                LoadingSkeleton(height = 180)
                            }
                        }
                    }

                    StaggeredFadeInUp(index = 5) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.dashboard_top_categories),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.dashboard_see_all),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            PremiumCard(variant = PremiumCardVariant.Glass) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    data.topCategories.take(4).forEach { category ->
                                        CategoryItem(
                                            categoryLabel = category.categoryLabel,
                                            amount = category.totalAmount,
                                            colorHex = category.colorToken
                                        )
                                    }
                                }
                            }
                        }
                    }

                    StaggeredFadeInUp(index = 6) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.dashboard_recent_transactions),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.dashboard_see_all),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            data.recentTransactions.forEachIndexed { index, transaction ->
                                StaggeredFadeInUp(
                                    index = 7 + index,
                                    delayMillis = 60
                                ) {
                                    PremiumCard(
                                        variant = PremiumCardVariant.Glass,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = transaction.description,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = transaction.categoryLabel,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            Text(
                                                text = "$${transaction.amount}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (transaction.type.name == "INCOME") 
                                                    Color(0xFF00F2FE) 
                                                else 
                                                    MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContentAnimated(modifier: Modifier = Modifier) {
    FadeInUpAnimation {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            StaggeredFadeInUp(index = 0) {
                LoadingSkeleton(height = 200)
            }
            StaggeredFadeInUp(index = 1) {
                LoadingSkeleton(height = 120)
            }
            StaggeredFadeInUp(index = 2) {
                LoadingSkeleton(height = 300)
            }
        }
    }
}
