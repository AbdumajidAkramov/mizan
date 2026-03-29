package dev.esbi.mizan.feature.newtransaction.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.premiumaddtransaction.ui.AccountChip
import dev.esbi.mizan.feature.premiumaddtransaction.ui.CategoryChip
import dev.esbi.mizan.feature.premiumaddtransaction.ui.TransactionTypeChip
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

// Transaction Context Row - Shows Type, Category, Account chips

@Composable
fun TransactionContextRow(
    transactionType: TransactionType?,
    selectedCategory: Category?,
    selectedSubCategory: Category?,
    selectedAccount: Account?,
    // Transfer-specific
    fromAccount: Account? = null,
    toAccount: Account? = null,
    onFromAccountClick: () -> Unit = {},
    onToAccountClick: () -> Unit = {},
    onTypeClick: () -> Unit = {},
    onCategoryClick: () -> Unit = {},
) {
    val typeColor = when (transactionType) {
        TransactionType.EXPENSE -> Color(0xFFF5576C) // Coral/Red
        TransactionType.INCOME -> Color(0xFF4FACFE) // Blue for income
        TransactionType.TRANSFER -> MizanTheme.premium.colors.emerald
        null -> MizanTheme.premium.text.secondary
    }

    val typeName = when (transactionType) {
        TransactionType.EXPENSE -> "Expense"
        TransactionType.INCOME -> "Income"
        TransactionType.TRANSFER -> "Transfer"
        null -> "Expense"
    }

    val typeIcon = when (transactionType) {
        TransactionType.EXPENSE -> Icons.ic_trend_up
        TransactionType.INCOME -> Icons.ic_down_trend
        TransactionType.TRANSFER -> Icons.ic_swap_horizontal
        null -> Icons.ic_trend_up
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
    ) {
        if (transactionType == TransactionType.TRANSFER) {
            // Transfer mode: Type chip + From/To Account chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Transaction Type Chip
                TransactionTypeChip(
                    label = typeName,
                    color = typeColor,
                    iconRes = typeIcon,
                    onClick = onTypeClick
                )

                // From Account Chip
                TransferAccountChip(
                    label = fromAccount?.name ?: "From Account",
                    isPlaceholder = fromAccount == null,
                    iconRes = Icons.ic_trend_up,
                    onClick = onFromAccountClick
                )

                // Swap Icon
                Icon(
                    painter = painterResource(id = Icons.ic_swap_horizontal),
                    contentDescription = null,
                    tint = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.size(16.dp)
                )

                // To Account Chip
                TransferAccountChip(
                    label = toAccount?.name ?: "To Account",
                    isPlaceholder = toAccount == null,
                    iconRes = Icons.ic_down_trend,
                    onClick = onToAccountClick
                )
            }
        } else {
            // Expense/Income mode: Type, Category, Account chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Transaction Type Chip (outlined with dropdown)
                TransactionTypeChip(
                    label = typeName,
                    color = typeColor,
                    iconRes = typeIcon,
                    onClick = onTypeClick
                )

                // Category Chip - filled or placeholder
                CategoryChip(
                    categoryName = selectedCategory?.name,
                    subCategoryName = selectedSubCategory?.name,
                    onClick = onCategoryClick
                )

                // Account Chip - filled or placeholder
                AccountChip(
                    accountName = selectedAccount?.name,
                    onClick = onFromAccountClick
                )
            }
        }
    }
}

@Composable
fun TransferAccountChip(label: String, isPlaceholder: Boolean, iconRes: Int, onClick: () -> Unit) {
    TODO("Not yet implemented")
}
