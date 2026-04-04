package dev.esbi.mizan.feature.addaccount

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.accounts.components.AccountGroupSelectorBottomSheet
import dev.esbi.mizan.feature.accounts.components.AccountGroupSelectorRow
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStore
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.components.input.CurrencyScrollSelector
import dev.esbi.mizan.ui.components.input.MizanTextField
import dev.esbi.mizan.ui.components.input.SelectorCurrency
import dev.esbi.mizan.ui.kit.dialogs.PremiumConfirmDialog
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.kit.premium.PremiumButton
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow
import dev.esbi.mizan.ui.utils.Icons as MizanIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAccountScreen(
    store: AddAccountStore,
    onBackClick: () -> Unit
) {
    val state by store.states.collectAsState(initial = AddAccountStore.State())
    val context = LocalContext.current
    var showGroupSelector by remember { mutableStateOf(false) }
    var accountToDelete by remember { mutableStateOf<Long?>(null) }


    LaunchedEffect(Unit) {
        store.labels.collect { label ->
            when (label) {
                is AddAccountStore.Label.AccountSaved -> onBackClick()
                is AddAccountStore.Label.AccountDeleted -> {
                    Toast.makeText(context, "Account deleted!", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }

                is AddAccountStore.Label.ShowMessage -> {
                    Toast.makeText(context, label.message, Toast.LENGTH_SHORT).show()
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
                state.accountId?.let { accountId ->
                    store.accept(AddAccountStore.Intent.ConfirmDeleteAccount(accountId))
                }
                accountToDelete = null
            },
            onDismiss = {
                accountToDelete = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Account", color = MizanTheme.premium.text.primary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        MizanIcon(
                            icon = IconValue(MizanIcons.ic_arrow_back),
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MizanTheme.premium.background.primary
                ),
                actions = {
                    if (state.accountId != null) {
                        IconButton(
                            onClick = {
                                accountToDelete = state.accountId
                            },
                            content = {
                                MizanIcon(
                                    icon = IconValue(MizanIcons.ic_delete),
                                    tint = MizanTheme.premium.text.primary
                                )
                            }
                        )
                    }
                }
            )
        },
        containerColor = MizanTheme.premium.background.primary
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                MizanTextField(
                    value = state.name,
                    onValueChange = { store.accept(AddAccountStore.Intent.UpdateName(it)) },
                    label = "Account Name",
                    placeholder = "e.g., Main Checking",
                    errorText = state.validationErrors[AddAccountStore.State.Field.NAME]
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Account Group Field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Account Group",
                        color = MizanTheme.premium.text.secondary,
                        style = MizanTheme.typography.bodyMd.copy(fontWeight = FontWeight.Medium)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AccountGroupSelectorRow(
                        selectedGroup = state.availableGroups.find { it.id == state.selectedGroupId },
                        onClick = { showGroupSelector = true }
                    )

                    state.validationErrors[AddAccountStore.State.Field.GROUP]?.let { error ->
                        Text(
                            text = error,
                            color = MizanTheme.premium.colors.error,
                            style = MizanTheme.typography.bodyXs,
                            modifier = androidx.compose.ui.Modifier.padding(
                                top = 4.dp,
                                start = 4.dp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                MizanTextField(
                    value = state.balance,
                    onValueChange = { store.accept(AddAccountStore.Intent.UpdateBalance(it)) },
                    label = "Initial Balance",
                    placeholder = "0.00",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Spacer(modifier = Modifier.height(24.dp))

                val selectorCurrencies = state.availableCurrencies.map {
                    SelectorCurrency(code = it.code, symbol = it.symbol)
                }
                val currentSelected = state.selectedCurrency?.let {
                    SelectorCurrency(code = it.code, symbol = it.symbol)
                }

                CurrencyScrollSelector(
                    currencies = selectorCurrencies,
                    selectedCurrency = currentSelected,
                    onCurrencySelected = { selected ->
                        val domainCurrency =
                            state.availableCurrencies.find { it.code == selected.code }
                        if (domainCurrency != null) {
                            store.accept(AddAccountStore.Intent.SelectCurrency(domainCurrency))
                        }
                    },
                    onAddCustomClick = {
                        Toast.makeText(
                            context,
                            "Adding custom currency coming soon!",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    errorText = state.validationErrors[AddAccountStore.State.Field.CURRENCY]
                )

                Spacer(modifier = Modifier.height(24.dp))

                MizanTextField(
                    value = state.description,
                    onValueChange = { store.accept(AddAccountStore.Intent.UpdateDescription(it)) },
                    label = "Description (Optional)",
                    placeholder = "Notes or remarks",
                    errorText = null // Not validated
                )

                Spacer(modifier = Modifier.height(16.dp))

                PremiumCard(
                    variant = PremiumCardVariant.Glass,
                    modifier = Modifier.premiumShadow(MizanTheme.premium.shadows.sm),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Include in Totals",
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.text.primary,
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Switch(
                            checked = state.excludeFromTotal,
                            onCheckedChange = {
                                store.accept(AddAccountStore.Intent.UpdateIncludeInTotals(it))
                            },
                        )

                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                PremiumButton(
                    onClick = { store.accept(AddAccountStore.Intent.SaveAccount) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        "Save Account",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MizanTheme.premium.colors.emerald)
                }
            }

            // Group Selector Bottom Sheet
            if (showGroupSelector) {
                ModalBottomSheet(
                    onDismissRequest = { showGroupSelector = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = MizanTheme.premium.background.primary
                ) {
                    AccountGroupSelectorBottomSheet(
                        groups = state.availableGroups,
                        selectedGroupId = state.selectedGroupId,
                        onGroupSelected = { group ->
                            store.accept(AddAccountStore.Intent.SelectGroup(group.id))
                            showGroupSelector = false
                        },
                        onDismiss = { showGroupSelector = false }
                    )
                }
            }
        }
    }
}
