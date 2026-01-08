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
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant

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
                is DashboardStore.Label.NavigateToCategory -> {
                    onNavigateToCategory(label.categoryId)
                }
                is DashboardStore.Label.ShowError -> {
                }
            }
        }
    }

    DashboardContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun DashboardContent(
    state: DashboardStore.State,
    onIntent: (DashboardStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.dashboardData == null -> {
            LoadingContent(modifier = modifier)
        }
        state.error != null && state.dashboardData == null -> {
            ErrorState(
                message = state.error,
                onRetry = { onIntent(DashboardStore.Intent.Retry) },
                modifier = modifier.fillMaxSize()
            )
        }
        state.dashboardData != null -> {
            val data = state.dashboardData
            
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
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

                PremiumBalanceCard(
                    totalBalance = data.totalBalance,
                    monthlyIncome = data.totalBalance - data.monthlyExpenses + data.monthlySavings,
                    monthlyExpenses = data.monthlyExpenses
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BudgetStatusCard(
                        spentAmount = data.monthlyExpenses,
                        budgetLimit = data.budgetLimit,
                        percentageUsed = data.budgetPercentageUsed,
                        modifier = Modifier.weight(1f)
                    )
                    
                    PremiumCard(
                        variant = PremiumCardVariant.Glass,
                        modifier = Modifier.weight(1f)
                    ) {
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
                    
                    data.recentTransactions.forEach { transaction ->
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

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        LoadingSkeleton(height = 200)
        LoadingSkeleton(height = 120)
        LoadingSkeleton(height = 300)
    }
}
