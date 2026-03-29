package dev.esbi.mizan.feature.budget.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.budget.presentation.BudgetViewModel
import dev.esbi.mizan.feature.budget.presentation.store.BudgetStore
import dev.esbi.mizan.feature.budget.presentation.ui.components.CategoryBudgetItem
import dev.esbi.mizan.feature.budget.presentation.ui.components.OverallBudgetCard
import dev.esbi.mizan.ui.animation.FadeInUpAnimation
import dev.esbi.mizan.ui.animation.StaggeredFadeInUp
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.utils.Strings

@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState(initial = BudgetStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is BudgetStore.Label.ShowError -> {
                    // Handle error display (e.g., Snackbar)
                }

                is BudgetStore.Label.ShowSuccess -> {
                    // Handle success display (e.g., Snackbar)
                }
            }
        }
    }

    BudgetContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun BudgetContent(
    state: BudgetStore.State,
    onIntent: (BudgetStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            if (state.budgetSummary != null) {
                FloatingActionButton(
                    onClick = { onIntent(BudgetStore.Intent.ShowAddDialog) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                            )
                        )
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_input_add),
                        contentDescription = stringResource(Strings.budget_add_category),
                        tint = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading && state.budgetSummary == null -> {
                LoadingContent(modifier = modifier.padding(paddingValues))
            }

            state.error != null && state.budgetSummary == null -> {
                FadeInUpAnimation {
                    ErrorState(
                        message = state.error,
                        onRetry = { onIntent(BudgetStore.Intent.Retry) },
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }

            state.budgetSummary != null -> {
                val summary = state.budgetSummary

                FadeInUpAnimation {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Header
                        StaggeredFadeInUp(index = 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = stringResource(Strings.budget_title),
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(Strings.budget_subtitle),
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Overall Budget Card
                        StaggeredFadeInUp(index = 1) {
                            OverallBudgetCard(
                                totalBudget = summary.totalBudget,
                                totalSpent = summary.totalSpent,
                                overallPercentage = summary.overallPercentage,
                                remaining = summary.remaining
                            )
                        }

                        // Category Budgets Section
                        StaggeredFadeInUp(index = 2) {
                            Text(
                                text = stringResource(Strings.budget_category_budgets),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Category Budget Items
                        summary.categoryBudgets.forEachIndexed { index, budget ->
                            StaggeredFadeInUp(index = 3 + index, delayMillis = 60) {
                                CategoryBudgetItem(
                                    budget = budget,
                                    categoryColor = getCategoryColor(budget.categoryId),
                                    onEditClick = {
                                        onIntent(BudgetStore.Intent.ShowEditDialog(budget.categoryId))
                                    }
                                )
                            }
                        }

                        // Add More Categories Card
                        StaggeredFadeInUp(
                            index = 3 + summary.categoryBudgets.size,
                            delayMillis = 60
                        ) {
                            PremiumCard(
                                variant = PremiumCardVariant.Glass,
                                onClick = { onIntent(BudgetStore.Intent.ShowAddDialog) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(android.R.drawable.ic_input_add),
                                        contentDescription = null,
                                        tint = Color(0xFF667EEA),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(Strings.budget_add_category),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF667EEA)
                                    )
                                }
                            }
                        }

                        // Budget Tips Card
                        StaggeredFadeInUp(
                            index = 4 + summary.categoryBudgets.size,
                            delayMillis = 60
                        ) {
                            PremiumCard(
                                variant = PremiumCardVariant.Glass,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = "💡",
                                        fontSize = 32.sp
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = stringResource(Strings.budget_tips_title),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = stringResource(Strings.budget_tip_1),
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = stringResource(Strings.budget_tip_2),
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = stringResource(Strings.budget_tip_3),
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom spacing for FAB
                        Spacer(modifier = Modifier.height(80.dp))
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            repeat(5) { index ->
                StaggeredFadeInUp(index = index) {
                    LoadingSkeleton(height = 150)
                }
            }
        }
    }
}

private fun getCategoryColor(categoryId: String): Color {
    return when (categoryId) {
        "food" -> Color(0xFFFF6B6B)
        "transport" -> Color(0xFF4ECDC4)
        "shopping" -> Color(0xFFFFBE0B)
        "bills" -> Color(0xFF667EEA)
        "entertainment" -> Color(0xFFFF006E)
        "health" -> Color(0xFF06FFA5)
        "travel" -> Color(0xFF4FACFE)
        "tech" -> Color(0xFF764BA2)
        else -> Color(0xFF667EEA)
    }
}
