package dev.esbi.mizan.feature.transactions.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.transactions.domain.model.Transaction
import dev.esbi.mizan.feature.transactions.domain.model.TransactionType
import dev.esbi.mizan.feature.transactions.domain.model.TransactionFilter
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModel
import dev.esbi.mizan.feature.transactions.presentation.store.TransactionsStore
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsHubScreen(
    viewModel: TransactionsViewModel,
    navController: NavController? = null,
    onBack: () -> Unit = {},
    onAddTransaction: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState(initial = TransactionsStore.State())
    val context = LocalContext.current
    
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    
    LaunchedEffect(searchQuery) {
        viewModel.onIntent(TransactionsStore.Intent.SetSearchQuery(searchQuery))
    }
    
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Transactions",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        AnimatedVisibility(
                            visible = !showSearch,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Row {
                                IconButton(onClick = { showSearch = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                IconButton(onClick = { onAddTransaction() }) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(PremiumColors.Success),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Transaction",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search Bar
                AnimatedVisibility(
                    visible = showSearch,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            viewModel.onIntent(TransactionsStore.Intent.SetSearchQuery(it))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                        placeholder = {
                            Text(
                                text = "Search transactions...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { 
                                showSearch = false
                                searchQuery = ""
                                viewModel.onIntent(TransactionsStore.Intent.SetSearchQuery(""))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Close search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = PremiumColors.Success,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                // Filter Chips
                TransactionFilterChips(
                    selectedFilter = state.selectedFilter,
                    onFilterSelected = { 
                        viewModel.onIntent(TransactionsStore.Intent.SetFilter(it))
                    },
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
                
                // Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = PremiumColors.Success
                        )
                    } else if (state.error != null) {
                        state.error?.let { errorMessage ->
                            ErrorMessage(
                                error = errorMessage,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        TransactionsContent(
                            state = state,
                            onTransactionClick = { transactionId ->
                                viewModel.onIntent(TransactionsStore.Intent.SelectTransaction(transactionId))
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionFilterChips(
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        TransactionFilter.ALL to "All",
        TransactionFilter.INCOME to "Income",
        TransactionFilter.EXPENSE to "Expense"
    )
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { (filter, label) ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = label,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) PremiumColors.Success 
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { onClick() }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TransactionsContent(
    state: TransactionsStore.State,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val summary = state.summary
    
    if (summary == null) {
        // Empty state
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "No transactions",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    } else if (summary.groupedTransactions.isEmpty()) {
        // Empty state with filter
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "No transactions found",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Filter: ${state.selectedFilter}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (state.searchQuery.isNotBlank()) {
                    Text(
                        text = "Search: ${state.searchQuery}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    } else {
        // Transaction list
        val listState = rememberLazyListState()
        
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Monthly Summary Card
            item {
                MonthlySummaryCard(
                    totalIncome = summary.totalIncome,
                    totalExpense = summary.totalExpense,
                    totalTransactions = summary.totalTransactions
                )
            }
            
            summary.groupedTransactions.forEach { group ->
                stickyHeader {
                    TransactionDateHeader(
                        dateLabel = group.dateLabel,
                        transactions = group.transactions,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(vertical = 8.dp)
                    )
                }
                
                items(group.transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onClick = { onTransactionClick(transaction.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    error: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TransactionDateHeader(
    dateLabel: String,
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    val dayTotal = transactions.fold(0.0) { sum, txn ->
        sum + when (txn.type) {
            TransactionType.INCOME -> txn.amount
            TransactionType.EXPENSE -> -txn.amount
            TransactionType.TRANSFER -> 0.0 // Transfers don't affect total
        }
    }
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dateLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = formatAmount(dayTotal, if (dayTotal >= 0) "+" else ""),
            style = MaterialTheme.typography.bodySmall,
            color = if (dayTotal >= 0) PremiumColors.Success else Color(0xFFF5576C),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        getCategoryColor(transaction.category).copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(transaction.category),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = getCategoryColor(transaction.category)
                )
            }
            
            // Transaction Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.categoryName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Amount
            Text(
                text = formatAmount(
                    when (transaction.type) {
                        TransactionType.INCOME -> transaction.amount
                        TransactionType.EXPENSE -> -transaction.amount
                        TransactionType.TRANSFER -> transaction.amount
                    },
                    if (transaction.type == TransactionType.INCOME) "+" else ""
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = when (transaction.type) {
                    TransactionType.INCOME -> PremiumColors.Success
                    TransactionType.EXPENSE -> Color(0xFFF5576C)
                    TransactionType.TRANSFER -> MaterialTheme.colorScheme.onSurface
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Helper functions
private fun formatAmount(amount: Double, prefix: String = ""): String {
    return "$prefix${String.format("%.2f", amount).replace(",", " ")} ${getCurrencySymbol()}"
}

private fun getCurrencySymbol(): String {
    // For now, return UZS. In real app, this should come from user settings
    return "UZS"
}

private fun getCategoryColor(category: String): Color {
    // Return different colors based on category
    return when (category.lowercase()) {
        "food" -> Color(0xFF4CAF50) // Food - Green
        "transport" -> Color(0xFF2196F3) // Transport - Blue
        "shopping" -> Color(0xFFFF9800) // Shopping - Orange
        "bills" -> Color(0xFFF44336) // Bills - Red
        "entertainment" -> Color(0xFF9C27B0) // Entertainment - Purple
        else -> PremiumColors.Success
    }
}

private fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "food" -> Icons.Default.List
        "transport" -> Icons.Default.List
        "shopping" -> Icons.Default.List
        "bills" -> Icons.Default.List
        "entertainment" -> Icons.Default.List
        else -> Icons.Default.List
    }
}

@Composable
private fun MonthlySummaryCard(
    totalIncome: Double,
    totalExpense: Double,
    totalTransactions: Int,
    modifier: Modifier = Modifier
) {
    val balance = totalIncome - totalExpense
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Monthly Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$totalTransactions transactions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Income",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatAmount(totalIncome, "+"),
                        style = MaterialTheme.typography.titleMedium,
                        color = PremiumColors.Success,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Expense",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatAmount(totalExpense, "-"),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFF5576C),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Balance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatAmount(balance, if (balance >= 0) "+" else ""),
                    style = MaterialTheme.typography.titleLarge,
                    color = if (balance >= 0) PremiumColors.Success else Color(0xFFF5576C),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
