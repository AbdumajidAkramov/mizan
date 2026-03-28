package dev.esbi.mizan.feature.accounts.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.accounts.presentation.AccountsViewModel
import dev.esbi.mizan.feature.accounts.presentation.store.AccountsStore
import dev.esbi.mizan.ui.components.account.AccountGroupHeader
import dev.esbi.mizan.ui.components.account.AccountRow
import dev.esbi.mizan.ui.components.account.TotalBalanceCard

private val BackgroundColor = Color(0xFF1A1A2E)
private val PremiumEmerald = Color(0xFF10B981)

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddAccount: () -> Unit,
    onNavigateToEditAccount: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Handle Labels side-effects in real app with LaunchedEffect
    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is AccountsStore.Label.NavigateBack -> onNavigateBack()
                is AccountsStore.Label.NavigateToAddAccount -> onNavigateToAddAccount()
                is AccountsStore.Label.NavigateToEditAccount -> onNavigateToEditAccount(label.accountId)
                is AccountsStore.Label.ShowError -> { /* Show error toast */ }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Header
        HeaderSection(
            onBackClick = { viewModel.onIntent(AccountsStore.Intent.NavigateBackClicked) },
            onAddClick = { viewModel.onIntent(AccountsStore.Intent.AddAccountClicked) }
        )

        // Search
        SearchSection(
            query = state.searchQuery,
            onQueryChanged = { viewModel.onIntent(AccountsStore.Intent.Search(it)) }
        )

        if (state.isLoading && state.groupedAccounts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PremiumEmerald)
            }
        } else {
            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TotalBalanceCard(
                        totalBalance = state.totalBalance,
                        baseCurrencyCode = state.baseCurrencyCode
                    )
                }

                state.groupedAccounts.forEach { group ->
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        AccountGroupHeader(
                            title = group.label,
                            count = group.accounts.size
                        )
                    }

                    items(group.accounts) { account ->
                        AccountRow(
                            id = account.id,
                            name = account.name,
                            balance = account.balance,
                            currencyCode = account.currency.code,
                            colorHex = account.color,
                            iconName = account.iconName,
                            onClick = { viewModel.onIntent(AccountsStore.Intent.EditAccountClicked(account.id)) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Add Account Dotted Button
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    AddAccountDashedButton(
                        onClick = { viewModel.onIntent(AccountsStore.Intent.AddAccountClicked) }
                    )
                }

                // Empty State
                if (state.groupedAccounts.isEmpty() && state.searchQuery.isNotBlank()) {
                    item {
                        EmptySearchState()
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Accounts",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Manage your financial accounts",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }

        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(PremiumEmerald)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun SearchSection(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.White.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Search accounts...",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Normal),
                cursorBrush = SolidColor(PremiumEmerald),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (query.isNotEmpty()) {
            IconButton(
                onClick = { onQueryChanged("") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun AddAccountDashedButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(16.dp), // Placeholder for drawn dashed border logic
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(PremiumEmerald.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = PremiumEmerald
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Add New Account",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Cash, Bank, Card, or Savings",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun EmptySearchState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "No result",
            tint = Color.White.copy(alpha = 0.2f),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No accounts found",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Try adjusting your search",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 14.sp
        )
    }
}
