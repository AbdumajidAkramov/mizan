package dev.esbi.mizan.feature.accountmanagement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.esbi.mizan.feature.accountmanagement.components.AddAccountButton
import dev.esbi.mizan.presentation.feature.accountmanagement.store.AccountManagementStore
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.components.account.AccountGroupHeader
import dev.esbi.mizan.ui.components.account.AccountRow
import dev.esbi.mizan.ui.components.account.PremiumTotalBalanceCard
import dev.esbi.mizan.ui.kit.dialogs.PremiumConfirmDialog
import dev.esbi.mizan.ui.kit.glass.PressCard
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow
import dev.esbi.mizan.ui.utils.Icons as MizanIcons

/**
 * Account Management Screen
 *
 * Full CRUD operations for accounts grouped by AccountGroup
 */
@Composable
fun AccountManagementScreen(
    viewModel: AccountManagementViewModel,
    onBack: () -> Unit,
    onNavigateToEditAccount: (Long?) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var accountToDelete by remember { mutableStateOf<AccountManagementStore.AccountItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is AccountManagementStore.Label.NavigateBack -> onBack()
                is AccountManagementStore.Label.ShowDeleteConfirmation -> {
                    println(label.account)
                    accountToDelete = label.account
                }

                is AccountManagementStore.Label.ShowError -> {
                    // Handle error display
                }
            }
        }
    }

    if (accountToDelete != null) {
        PremiumConfirmDialog(
            title = "Delete Account?",
            message = "Are you sure you want to delete this account? This action cannot be undone, but your past transaction history will be preserved.",
            confirmText = "Delete",
            dismissText = "Cancel",
            onConfirm = {
                viewModel.onConfirmDeleteAccount(accountToDelete!!.id)
                accountToDelete = null
            },
            onDismiss = {
                accountToDelete = null
            }
        )
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
                        contentPadding = PaddingValues(
                            horizontal = MizanTheme.premium.spacing.lg,
                            vertical = MizanTheme.premium.spacing.lg
                        ),
                        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
                    ) {
                        // Search Bar
                        item {
                            Box(modifier = Modifier.padding(vertical = 16.dp)) {
                                SearchBar(
                                    query = state.searchQuery,
                                    onQueryChange = { viewModel.onSearchAccounts(it) }
                                )
                            }
                        }

                        // Total Balance Card
                        item {
                            PremiumTotalBalanceCard(
                                balance = state.totalBalance,
                                monthlyChange = state.monthlyChange,
                                monthlyChangePercent = state.monthlyChangePercent,
                                currency = state.baseCurrency?.code.orEmpty()
                            )
                        }

                        // Group accounts by AccountGroup from DB
                        val searchQuery = state.searchQuery
                        val groupedAccounts = if (searchQuery.isBlank()) {
                            state.groups
                        } else {
                            state.groups.map { group ->
                                group.copy(
                                    accounts = group.accounts.filter {
                                        it.name.contains(searchQuery, ignoreCase = true)
                                    }
                                )
                            }.filter { it.accounts.isNotEmpty() }
                        }

                        groupedAccounts.forEach { group ->
                            // Group Header (from :ui-kit)
                            item(key = "group_${group.id}") {
                                AccountGroupHeader(
                                    title = group.name,
                                    count = group.accounts.size
                                )
                            }

                            // Account Cards
                            items(
                                items = group.accounts,
                                key = { it.id }
                            ) { account ->
                                PressCard(
                                    modifier = Modifier,
                                    onClick = { viewModel.onOpenEditAccountSheet(account) },
                                ) {
                                    PremiumCard(
                                        variant = PremiumCardVariant.Glass,
                                        modifier = Modifier.premiumShadow(MizanTheme.premium.shadows.sm),
                                        cornerShape = RoundedCornerShape(MizanTheme.premium.radius.xs)
                                    ) {
                                        AccountRow(
                                            id = account.id,
                                            name = account.name,
                                            balance = account.balance,
                                            currencyCode = account.currencyCode,
                                            colorHex = null,
                                        )
                                    }
                                }
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
                        val allEmpty = groupedAccounts.all { it.accounts.isEmpty() }
                        if (state.accounts.isEmpty() || allEmpty) {
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
            groups = state.groups,
            onSave = { viewModel.onSaveAccount(it) },
            onDelete = { accountId -> viewModel.onDeleteAccount(accountId) },
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
