package dev.esbi.mizan.feature.accountmanagement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import dev.esbi.mizan.ui.utils.Icons as MizanIcons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.feature.accountmanagement.AccountManagementViewModel
import dev.esbi.mizan.feature.accountmanagement.store.AccountManagementStore
import dev.esbi.mizan.feature.accountmanagement.ui.components.AccountCard
import dev.esbi.mizan.feature.accountmanagement.ui.components.AddAccountButton
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Account Management Screen
 *
 * Full CRUD operations for accounts with grouped display
 */
@Composable
fun AccountManagementScreen(
    viewModel: AccountManagementViewModel,
    onBack: () -> Unit,
    onNavigateToEditAccount: (Long?) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is AccountManagementStore.Label.NavigateBack -> onBack()
                is AccountManagementStore.Label.ShowDeleteConfirmation -> {
                    // Handle delete confirmation dialog
                }
                is AccountManagementStore.Label.ShowError -> {
                    // Handle error display
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AccountManagementHeader(
                onBack = onBack,
                onAddNew = {
                    viewModel.onOpenAddAccountSheet()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MizanTheme.premium.background.primary)
                .padding(paddingValues)
        ) {
            // Content
            Box(modifier = Modifier.weight(1f)) {
                if (state.isLoading && state.accounts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.width(200.dp),
                            color = MizanTheme.premium.colors.emerald
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = MizanTheme.premium.spacing.lg,
                            vertical = MizanTheme.premium.spacing.lg
                        ),
                        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
                    ) {
                        // Search Bar
                        item {
                            SearchBar(
                                query = state.searchQuery,
                                onQueryChange = { viewModel.onSearchAccounts(it) }
                            )
                        }

                        // Total Balance Card
                        item {
                            TotalBalanceCard(
                                totalBalance = state.totalBalance,
                                accountCount = state.accounts.size
                            )
                        }

                        // Group accounts by type
                        val filteredAccounts = if (state.searchQuery.isBlank()) {
                            state.accounts
                        } else {
                            state.accounts.filter {
                                it.name.contains(state.searchQuery, ignoreCase = true)
                            }
                        }

                        val groupedAccounts = groupAccountsByType(filteredAccounts)

                        groupedAccounts.forEach { group ->
                            // Group Header
                            item {
                                AccountGroupHeader(
                                    label = group.label,
                                    accountCount = group.accounts.size
                                )
                            }

                            // Account Cards
                            items(
                                items = group.accounts,
                                key = { it.id }
                            ) { account ->
                                AccountCard(
                                    account = account,
                                    onTap = {
                                        viewModel.onOpenEditAccountSheet(account)
                                    },
                                    onArchive = {
                                        viewModel.onArchiveAccount(account.id)
                                    }
                                )
                            }
                        }

                        // Add New Account Button
                        item {
                            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))
                            AddAccountButton(
                                onClick = { viewModel.onOpenAddAccountSheet() }
                            )
                        }

                        // Empty State
                        if (filteredAccounts.isEmpty()) {
                            item {
                                EmptyState(
                                    onAddNew = { viewModel.onOpenAddAccountSheet() }
                                )
                            }
                        }

                        // Bottom spacing
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }

    // Add/Edit Bottom Sheet
    if (state.isAddEditSheetVisible) {
        AddEditAccountSheet(
            account = state.editingAccount,
            onSave = { viewModel.onSaveAccount(it) },
            onDismiss = { viewModel.onCloseAddEditSheet() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountManagementHeader(
    onBack: () -> Unit,
    onAddNew: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Accounts",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.text.primary
                )
                Text(
                    text = "Manage your financial accounts",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                MizanIcon(
                    icon = IconValue(MizanIcons.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.primary
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald)
                    .clickable { onAddNew() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Account",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MizanTheme.premium.background.primary
        )
    )
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search accounts...",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.tertiary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MizanTheme.premium.text.tertiary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MizanTheme.premium.colors.surface2,
            unfocusedContainerColor = MizanTheme.premium.colors.surface2,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MizanTheme.premium.colors.emerald
        ),
        singleLine = true
    )
}

@Composable
private fun TotalBalanceCard(
    totalBalance: Double,
    accountCount: Int
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("uz", "UZ")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.surface2,
                        MizanTheme.premium.colors.surface3
                    )
                )
            )
            .padding(MizanTheme.premium.spacing.xl)
    ) {
        // Emerald Glow Effect
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(200.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MizanTheme.premium.colors.emerald.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column {
            Text(
                text = "Total Balance",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = currencyFormat.format(totalBalance),
                    style = MizanTheme.premium.typography.headingLg.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MizanTheme.premium.text.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "UZS",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            Text(
                text = "$accountCount accounts",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }
    }
}

@Composable
private fun AccountGroupHeader(
    label: String,
    accountCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MizanTheme.premium.spacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MizanTheme.premium.typography.headingSm,
            color = MizanTheme.premium.text.secondary
        )
        Text(
            text = "$accountCount ${if (accountCount == 1) "account" else "accounts"}",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )
    }
}

@Composable
private fun EmptyState(
    onAddNew: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MizanTheme.premium.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface2),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MizanTheme.premium.text.tertiary,
                modifier = Modifier.size(40.dp)
            )
        }

        Text(
            text = "No accounts yet",
            style = MizanTheme.premium.typography.headingSm,
            color = MizanTheme.premium.text.primary
        )

        Text(
            text = "Add your first account to start tracking",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )
    }
}

private fun groupAccountsByType(accounts: List<AccountManagementStore.AccountItem>): List<AccountManagementStore.AccountGroup> {
    val liquidAssets = accounts.filter {
        it.type == Account.Type.CASH || it.type == Account.Type.CARD
    }
    val savings = accounts.filter {
        it.type == Account.Type.SAVINGS || it.type == Account.Type.INVESTMENT
    }
    val debts = accounts.filter {
        it.type == Account.Type.DEBT
    }

    return listOf(
        AccountManagementStore.AccountGroup(
            id = "liquid",
            label = "Liquid Assets",
            accounts = liquidAssets
        ),
        AccountManagementStore.AccountGroup(
            id = "savings",
            label = "Savings & Investments",
            accounts = savings
        ),
        AccountManagementStore.AccountGroup(
            id = "debts",
            label = "Debts",
            accounts = debts
        )
    ).filter { it.accounts.isNotEmpty() }
}
