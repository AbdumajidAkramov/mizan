package dev.esbi.mizan.feature.accountmanagement

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.accountmanagement.components.AccountGroupSelectorBottomSheet
import dev.esbi.mizan.feature.accountmanagement.components.AccountGroupSelectorRow
import dev.esbi.mizan.presentation.feature.accountmanagement.store.AccountManagementStore
import dev.esbi.mizan.ui.components.input.CurrencyScrollSelector
import dev.esbi.mizan.ui.components.input.MizanTextField
import dev.esbi.mizan.ui.components.input.SelectorCurrency
import dev.esbi.mizan.ui.theme.colors.MizanTheme

private val DEFAULT_CURRENCIES = listOf(
    SelectorCurrency("UZS", "сўм"),
    SelectorCurrency("USD", "$"),
    SelectorCurrency("EUR", "€"),
    SelectorCurrency("RUB", "₽")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAccountSheet(
    account: AccountManagementStore.AccountItem?,
    groups: List<AccountManagementStore.AccountGroupItem>,
    onSave: (AccountManagementStore.AccountItem) -> Unit,
    onDelete: (Long) -> Unit = {},
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditing = account != null

    var name by remember { mutableStateOf(account?.name ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var balance by remember { mutableDoubleStateOf(account?.balance ?: 0.0) }
    var balanceText by remember {
        mutableStateOf(
            if (account?.balance != null && account.balance != 0.0) account.balance.toString() else ""
        )
    }
    var selectedGroupId by remember { mutableLongStateOf(account?.groupId ?: 1L) } // Default to "General" group
    var groupError by remember { mutableStateOf<String?>(null) }
    var showGroupSelector by remember { mutableStateOf(false) }
    var selectedCurrency by remember {
        mutableStateOf(
            DEFAULT_CURRENCIES.find { it.code == account?.currencyCode } ?: DEFAULT_CURRENCIES.first()
        )
    }
    var description by remember { mutableStateOf(account?.description ?: "") }

    val isValid = name.isNotBlank() && selectedGroupId > 0L

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MizanTheme.premium.background.primary,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MizanTheme.premium.text.tertiary.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.lg)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(if (isEditing) 1f else 0.1f))

                Text(
                    text = if (isEditing) "Edit Account" else "Add New Account",
                    style = MizanTheme.premium.typography.headingMd,
                    color = MizanTheme.premium.text.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(3f)
                )

                if (isEditing) {
                    IconButton(
                        onClick = { account?.id?.let { onDelete(it) } },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Account",
                            tint = MizanTheme.premium.colors.error
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))

            // Form Fields
            Column(
                verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
            ) {
                // Account Group (mandatory)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Account Group",
                        color = MizanTheme.premium.text.secondary,
                        style = MizanTheme.typography.bodyMd.copy(fontWeight = FontWeight.Medium)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    AccountGroupSelectorRow(
                        selectedGroup = groups.find { it.id == selectedGroupId }?.let { 
                            dev.esbi.mizan.domain.model.AccountGroup(
                                id = it.id,
                                name = it.name,
                                iconName = null,
                                orderIndex = 0,
                                type = dev.esbi.mizan.domain.model.AccountGroupType.DEFAULT,
                                isSystemGroup = it.isSystemGroup
                            )
                        },
                        onClick = { showGroupSelector = true }
                    )
                    
                    groupError?.let { error ->
                        Text(
                            text = error,
                            color = MizanTheme.premium.colors.error,
                            style = MizanTheme.typography.bodyXs,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }
                }

                // Account Name (mandatory)
                MizanTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    label = "Account Name",
                    placeholder = "e.g., Cash Wallet, Humo Card",
                    errorText = nameError
                )

                // Initial Balance / Amount
                MizanTextField(
                    value = balanceText,
                    onValueChange = {
                        balanceText = it
                        balance = it.toDoubleOrNull() ?: 0.0
                    },
                    label = "Initial Balance",
                    placeholder = "0.00",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                // Currency (Horizontal Scroll Selector)
                CurrencyScrollSelector(
                    currencies = DEFAULT_CURRENCIES,
                    selectedCurrency = selectedCurrency,
                    onCurrencySelected = { selectedCurrency = it },
                    onAddCustomClick = { /* TODO: custom currency */ }
                )

                // Description
                MizanTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description",
                    placeholder = "Optional notes about this account",
                    singleLine = false
                )
            }

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MizanTheme.premium.text.secondary
                    )
                ) {
                    Text(text = "Cancel")
                }

                Button(
                    onClick = {
                        // Validation
                        var hasError = false
                        if (name.isBlank()) {
                            nameError = "Account name is required"
                            hasError = true
                        }
                        if (selectedGroupId <= 0L) {
                            groupError = "Please select a group"
                            hasError = true
                        }
                        if (hasError) return@Button

                        val savedAccount = AccountManagementStore.AccountItem(
                            id = account?.id ?: 0L,
                            groupId = selectedGroupId,
                            groupName = groups.find { it.id == selectedGroupId }?.name ?: "",
                            name = name,
                            balance = balance,
                            currencyCode = selectedCurrency.code,
                            isArchived = false,
                            excludeFromTotal = false,
                            description = description.ifBlank { null }
                        )
                        onSave(savedAccount)
                    },
                    enabled = isValid,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.premium.colors.emerald,
                        disabledContainerColor = MizanTheme.premium.colors.emerald.copy(alpha = 0.5f)
                    )
                ) {
                    Text(text = if (isEditing) "Save Changes" else "Add Account")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
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
                groups = groups.map { 
                    dev.esbi.mizan.domain.model.AccountGroup(
                        id = it.id,
                        name = it.name,
                        iconName = null,
                        orderIndex = 0,
                        type = dev.esbi.mizan.domain.model.AccountGroupType.DEFAULT,
                        isSystemGroup = it.isSystemGroup
                    )
                },
                selectedGroupId = selectedGroupId,
                onGroupSelected = { group ->
                    selectedGroupId = group.id
                    groupError = null
                    showGroupSelector = false
                },
                onDismiss = { showGroupSelector = false }
            )
        }
    }
}
