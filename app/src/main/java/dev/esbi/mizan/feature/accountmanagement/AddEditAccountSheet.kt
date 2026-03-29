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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.presentation.feature.accountmanagement.store.AccountManagementStore
import dev.esbi.mizan.ui.kit.icon.AccountIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme

private val ACCOUNT_COLORS = listOf(
    "#10B981", // Emerald
    "#667EEA", // Indigo
    "#4FACFE", // Blue
    "#F5576C", // Red/Pink
    "#C471F5", // Purple
    "#F093FB", // Pink
    "#FFD93D", // Yellow
    "#FF9A3C", // Orange
    "#00C9FF", // Cyan
    "#92FE9D"  // Light Green
)

private val ACCOUNT_ICONS = listOf(
    "ic_wallet",
    "ic_card",
    "ic_bank",
    "ic_piggy_bank",
    "ic_cash",
    "ic_savings",
    "ic_investment"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAccountSheet(
    account: AccountManagementStore.AccountItem?,
    onSave: (AccountManagementStore.AccountItem) -> Unit,
    onDelete: (Long) -> Unit = {},
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditing = account != null

    var name by remember { mutableStateOf(account?.name ?: "") }
    var balance by remember { mutableDoubleStateOf(account?.balance ?: 0.0) }
    var balanceText by remember { mutableStateOf(if (account?.balance != null && account.balance != 0.0) account.balance.toString() else "") }
    var selectedType by remember { mutableStateOf(account?.type ?: Account.Type.CASH) }
    var selectedColor by remember { mutableStateOf(account?.color ?: ACCOUNT_COLORS.first()) }
    var selectedIcon by remember { mutableStateOf(account?.iconName ?: ACCOUNT_ICONS.first()) }

    val isValid = name.isNotBlank()

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
                // Empty spacer for balance
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
                // Account Name
                Column {
                    Text(
                        text = "Account Name",
                        style = MizanTheme.typography.bodyMd.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MizanTheme.premium.text.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = {
                            Text(
                                text = "e.g., Cash Wallet, Humo Card",
                                color = MizanTheme.premium.text.tertiary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MizanTheme.premium.colors.emerald,
                            unfocusedBorderColor = MizanTheme.premium.glass.border,
                            focusedContainerColor = MizanTheme.premium.colors.surface2,
                            unfocusedContainerColor = MizanTheme.premium.colors.surface2
                        ),
                        shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
                        singleLine = true
                    )
                }

                // Initial Balance
                Column {
                    Text(
                        text = "Initial Balance",
                        style = MizanTheme.typography.bodyMd.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MizanTheme.premium.text.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = balanceText,
                        onValueChange = {
                            balanceText = it
                            balance = it.toDoubleOrNull() ?: 0.0
                        },
                        placeholder = {
                            Text(
                                text = "0.00",
                                color = MizanTheme.premium.text.tertiary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MizanTheme.premium.colors.emerald,
                            unfocusedBorderColor = MizanTheme.premium.glass.border,
                            focusedContainerColor = MizanTheme.premium.colors.surface2,
                            unfocusedContainerColor = MizanTheme.premium.colors.surface2
                        ),
                        shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        suffix = {
                            Text(
                                text = "UZS",
                                color = MizanTheme.premium.text.tertiary
                            )
                        }
                    )
                }

                // Account Type
                Column {
                    Text(
                        text = "Account Type",
                        style = MizanTheme.typography.bodyMd.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MizanTheme.premium.text.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    AccountTypeSelector(
                        selectedType = selectedType,
                        onTypeSelected = { selectedType = it }
                    )
                }

                // Color Selector
                Column {
                    Text(
                        text = "Color",
                        style = MizanTheme.typography.bodyMd.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MizanTheme.premium.text.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ColorSelector(
                        selectedColor = selectedColor,
                        onColorSelected = { selectedColor = it }
                    )
                }

                // Icon Selector
                Column {
                    Text(
                        text = "Icon",
                        style = MizanTheme.typography.bodyMd.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MizanTheme.premium.text.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconSelector(
                        selectedIcon = selectedIcon,
                        selectedColor = selectedColor,
                        onIconSelected = { selectedIcon = it }
                    )
                }
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
                        val savedAccount = AccountManagementStore.AccountItem(
                            id = account?.id ?: 0L,
                            groupId = account?.groupId ?: 1L,
                            name = name,
                            type = selectedType,
                            balance = balance,
                            currencyCode = "UZS",
                            iconName = selectedIcon,
                            color = selectedColor,
                            isArchived = false,
                            excludeFromTotal = false,
                            description = null
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
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AccountTypeSelector(
    selectedType: Account.Type,
    onTypeSelected: (Account.Type) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Account.Type.entries.forEach { type ->
            val isSelected = type == selectedType
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                    .background(
                        if (isSelected) MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)
                        else MizanTheme.premium.colors.surface2
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) MizanTheme.premium.colors.emerald
                        else MizanTheme.premium.glass.border,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.lg)
                    )
                    .clickable { onTypeSelected(type) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = getTypeLabel(type),
                    style = MizanTheme.typography.bodySm.copy(
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    ),
                    color = if (isSelected) MizanTheme.premium.colors.emerald
                    else MizanTheme.premium.text.secondary
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorSelector(
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ACCOUNT_COLORS.forEach { colorHex ->
            val color = try {
                Color(android.graphics.Color.parseColor(colorHex))
            } catch (e: Exception) {
                MizanTheme.premium.colors.emerald
            }
            val isSelected = colorHex == selectedColor

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = if (isSelected) Color.White else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(colorHex) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IconSelector(
    selectedIcon: String,
    selectedColor: String,
    onIconSelected: (String) -> Unit
) {
    val color = try {
        Color(android.graphics.Color.parseColor(selectedColor))
    } catch (e: Exception) {
        MizanTheme.premium.colors.emerald
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ACCOUNT_ICONS.forEach { iconName ->
            val isSelected = iconName == selectedIcon

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                    .background(
                        if (isSelected) color.copy(alpha = 0.15f)
                        else MizanTheme.premium.colors.surface2
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) color else MizanTheme.premium.glass.border,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.md)
                    )
                    .clickable { onIconSelected(iconName) },
                contentAlignment = Alignment.Center
            ) {
                AccountIcon(
                    iconName = iconName,
                    accountType = Account.Type.CASH,
                    tint = if (isSelected) color else MizanTheme.premium.text.tertiary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

private fun getTypeLabel(type: Account.Type): String {
    return when (type) {
        Account.Type.CASH -> "Cash"
        Account.Type.CARD -> "Card"
        Account.Type.SAVINGS -> "Savings"
        Account.Type.DEBT -> "Debt"
        Account.Type.INVESTMENT -> "Investment"
        Account.Type.BANK -> "Bank"
        Account.Type.CREDIT -> "Credit"
    }
}
