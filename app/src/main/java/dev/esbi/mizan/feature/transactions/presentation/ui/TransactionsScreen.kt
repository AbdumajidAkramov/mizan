package dev.esbi.mizan.feature.transactions.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModel
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModelFactory
import dev.esbi.mizan.feature.transactions.presentation.store.TransactionsStore
import dev.esbi.mizan.feature.transactions.presentation.ui.components.FilterChips
import dev.esbi.mizan.feature.transactions.presentation.ui.components.MonthlySummaryCards
import dev.esbi.mizan.feature.transactions.presentation.ui.components.PremiumTransactionItem
import dev.esbi.mizan.ui.animation.FadeInUpAnimation
import dev.esbi.mizan.ui.animation.StaggeredFadeInUp
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.theme.PremiumColors

@Composable
fun TransactionsScreen(
    viewModelFactory: TransactionsViewModelFactory,
    modifier: Modifier = Modifier
) {
    val viewModel: TransactionsViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState(initial = TransactionsStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is TransactionsStore.Label.ShowError -> {
                    // Handle error display
                }

                is TransactionsStore.Label.NavigateToDetail -> {
                    // Handle navigation to detail
                }
            }
        }
    }

    TransactionsContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun TransactionsContent(
    state: TransactionsStore.State,
    onIntent: (TransactionsStore.Intent) -> Unit,
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
                        onRetry = { onIntent(TransactionsStore.Intent.Retry) },
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
                        // Header
                        StaggeredFadeInUp(index = 0) {
                            Text(
                                text = stringResource(R.string.transactions_title),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Search Bar
                        StaggeredFadeInUp(index = 1) {
                            PremiumCard(variant = PremiumCardVariant.Glass) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(android.R.drawable.ic_menu_search),
                                        contentDescription = null,
                                        tint =  PremiumColors.TextTertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    TextField(
                                        value = state.searchQuery,
                                        onValueChange = { query ->
                                            onIntent(TransactionsStore.Intent.SetSearchQuery(query))
                                        },
                                        placeholder = {
                                            Text(
                                                text = stringResource(R.string.transactions_search_placeholder),
                                                color = PremiumColors.TextMuted
                                            )
                                        },
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            disabledContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedTextColor = PremiumColors.TextPrimary,
                                            unfocusedTextColor = PremiumColors.TextPrimary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Filter Chips
                        StaggeredFadeInUp(index = 2) {
                            FilterChips(
                                selectedFilter = state.selectedFilter,
                                onFilterSelected = { filter ->
                                    onIntent(TransactionsStore.Intent.SetFilter(filter))
                                }
                            )
                        }

                        // Summary Cards
                        StaggeredFadeInUp(index = 3) {
                            MonthlySummaryCards(
                                totalIncome = summary.totalIncome,
                                totalExpense = summary.totalExpense
                            )
                        }

                        // Transaction Groups
                        if (summary.groupedTransactions.isEmpty()) {
                            StaggeredFadeInUp(index = 4) {
                                EmptyState()
                            }
                        } else {
                            summary.groupedTransactions.forEachIndexed { groupIndex, group ->
                                StaggeredFadeInUp(index = 4 + groupIndex, delayMillis = 60) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Date Header
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(android.R.drawable.ic_menu_my_calendar),
                                                contentDescription = null,
                                                tint = PremiumColors.TextMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = group.dateLabel,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = PremiumColors.TextTertiary
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(1.dp)
                                                    .background(PremiumColors.Surface2)
                                            )
                                        }

                                        // Transaction Items
                                        group.transactions.forEachIndexed { txnIndex, transaction ->
                                            StaggeredFadeInUp(
                                                index = 4 + groupIndex + txnIndex + 1,
                                                delayMillis = 40
                                            ) {
                                                PremiumTransactionItem(
                                                    transaction = transaction,
                                                    onClick = { txn ->
                                                        onIntent(
                                                            TransactionsStore.Intent.SelectTransaction(
                                                                txn.id
                                                            )
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
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
            repeat(8) { index ->
                StaggeredFadeInUp(index = index, delayMillis = 60) {
                    LoadingSkeleton(height = 80)
                }
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PremiumColors.Surface2),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "💳",
                fontSize = 40.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.transactions_empty_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = PremiumColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.transactions_empty_subtitle),
            fontSize = 14.sp,
            color = PremiumColors.TextTertiary
        )
    }
}
