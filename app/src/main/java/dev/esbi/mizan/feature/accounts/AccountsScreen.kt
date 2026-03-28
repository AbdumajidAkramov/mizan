package dev.esbi.mizan.feature.accounts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.feature.accounts.store.AccountsStore
import dev.esbi.mizan.ui.kit.accounts.AccountGroupHeader
import dev.esbi.mizan.ui.kit.accounts.AccountRow
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel,
    onBack: () -> Unit,
    onNavigateToAddAccount: () -> Unit,
    onNavigateToAccountDetail: (Long) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.labels.collectLatest { label ->
            when (label) {
                is AccountsStore.Label.NavigateToAddAccount -> onNavigateToAddAccount()
                is AccountsStore.Label.NavigateToAccountDetail -> onNavigateToAccountDetail(label.accountId)
                is AccountsStore.Label.ShowError -> {}
            }
        }
    }

    AccountsContent(
        state = state,
        onBack = onBack,
        onAddAccount = { viewModel.onAddAccount() },
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
        onAccountClicked = { viewModel.onAccountClicked(it) }
    )
}

@Composable
private fun AccountsContent(
    state: AccountsStore.State,
    onBack: () -> Unit,
    onAddAccount: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onAccountClicked: (Long) -> Unit
) {
    val bgColor = Color(0xFF0D1B2A)
    val surfaceColor = Color(0xFF152232)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                AccountsTopBar(
                    onBack = onBack,
                    onAddAccount = onAddAccount
                )
            }

            item {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChanged = onSearchQueryChanged,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item {
                TotalBalanceCard(
                    totalBalance = state.totalBalance,
                    primaryCurrency = state.primaryCurrency,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF00D4AA),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            } else {
                val displayGroups = state.filteredGroupedAccounts
                    .entries
                    .sortedBy { it.key.ordinal }

                displayGroups.forEach { (type, accounts) ->
                    item(key = "header_${type.name}") {
                        AccountGroupHeader(
                            title = type.displayName(),
                            accountCount = accounts.size,
                            modifier = Modifier.padding(
                                start = 20.dp, end = 20.dp, top = 20.dp, bottom = 4.dp
                            )
                        )
                    }

                    item(key = "group_${type.name}") {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(surfaceColor),
                            verticalArrangement = Arrangement.spacedBy(1.dp)
                        ) {
                            accounts.forEachIndexed { index, account ->
                                AccountRow(
                                    name = account.name,
                                    subtitle = account.subtitle,
                                    balance = account.balance,
                                    currencyCode = account.currencyCode,
                                    iconName = account.iconName,
                                    color = account.color,
                                    modifier = Modifier.background(
                                        if (index == 0) Color.Transparent
                                        else Color(0xFF0A0F1A).copy(alpha = 0.3f)
                                    ),
                                    onClick = { onAccountClicked(account.id) }
                                )
                            }
                        }
                    }
                }

                if (displayGroups.isEmpty()) {
                    item {
                        EmptyAccountsHint(
                            modifier = Modifier.padding(vertical = 48.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountsTopBar(
    onBack: () -> Unit,
    onAddAccount: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Accounts",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Manage your financial accounts",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF00D4AA)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onAddAccount) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Account",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChanged,
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF152232))
                    .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(28.dp))
                    .padding(horizontal = 16.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search accounts...",
                            color = Color.White.copy(alpha = 0.3f),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            color = Color.White,
            fontSize = 14.sp
        ),
        modifier = modifier
    )
}

@Composable
private fun TotalBalanceCard(
    totalBalance: Double,
    primaryCurrency: String,
    modifier: Modifier = Modifier
) {
    val (whole, fraction) = formatBalanceParts(totalBalance)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1A2E42), Color(0xFF0F2030))
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Column {
            Text(
                text = "Total Balance",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = whole,
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = ".$fraction",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 3.dp, start = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = primaryCurrency,
                    color = Color.White.copy(alpha = 0.55f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyAccountsHint(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No accounts yet",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tap + to add your first account",
            color = Color.White.copy(alpha = 0.25f),
            fontSize = 13.sp
        )
    }
}

private fun Account.Type.displayName(): String = when (this) {
    Account.Type.CASH -> "Cash"
    Account.Type.CARD -> "Cards"
    Account.Type.SAVINGS -> "Savings"
    Account.Type.DEBT -> "Debt"
    Account.Type.INVESTMENT -> "Investments"
}

private fun formatBalanceParts(amount: Double): Pair<String, String> {
    val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
        isGroupingUsed = true
    }
    val formatted = formatter.format(amount)
    val dotIndex = formatted.lastIndexOf('.')
    return if (dotIndex >= 0) {
        formatted.substring(0, dotIndex).replace(",", " ") to formatted.substring(dotIndex + 1)
    } else {
        formatted.replace(",", " ") to "00"
    }
}
