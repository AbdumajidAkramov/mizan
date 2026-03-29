package dev.esbi.mizan.feature.premiumaddtransaction.part1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.premiumaddtransaction.ui.AccountChip
import dev.esbi.mizan.feature.premiumaddtransaction.ui.CategoryChip
import dev.esbi.mizan.feature.premiumaddtransaction.ui.TransactionTypeChip
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

// Transaction Context Row - Shows Type, Category, Account chips

@Composable
fun TransactionContextContent(
    transactionType: TransactionType,
    selectedCategory: Category?,
    selectedSubCategory: Category?,
    // Transfer-specific
    selectedAccount: Account? = null,
    targetAccount: Account? = null,

    onTypeClick: () -> Unit = {},
    onCategoryClick: () -> Unit = {},
    onSelectedAccountClick: () -> Unit = {},
    onTargetAccountClick: () -> Unit = {},
) {
    val typeColor = when (transactionType) {
        TransactionType.EXPENSE -> Color(0xFFF5576C) // Coral/Red
        TransactionType.INCOME -> Color(0xFF4FACFE) // Blue for income
        TransactionType.TRANSFER -> MizanTheme.premium.colors.emerald
    }

    val typeName = when (transactionType) {
        TransactionType.EXPENSE -> "Expense"
        TransactionType.INCOME -> "Income"
        TransactionType.TRANSFER -> "Transfer"
    }

    val typeIcon = when (transactionType) {
        TransactionType.EXPENSE -> Icons.ic_trend_up
        TransactionType.INCOME -> Icons.ic_down_trend
        TransactionType.TRANSFER -> Icons.ic_swap_horizontal
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
    ) {
        val chipSpacing = MizanTheme.premium.spacing.sm
        if (transactionType == TransactionType.TRANSFER) {
            // Transfer mode: Type chip + From/To Account chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(chipSpacing),
                verticalArrangement = Arrangement.spacedBy(chipSpacing), // Qatorlar orasidagi masofa
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = Int.MAX_VALUE // Standart holatda
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
                    label = selectedAccount?.name ?: "From Account",
                    isPlaceholder = selectedAccount == null,
                    icon = IconValue(Icons.ic_arrow_up),
                    onClick = onSelectedAccountClick
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
                    label = targetAccount?.name ?: "To Account",
                    isPlaceholder = targetAccount == null,
                    icon = IconValue(Icons.ic_arrow_down),
                    onClick = onTargetAccountClick
                )
            }
        } else {
            // Expense/Income mode: Type, Category, Account chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(chipSpacing),
                verticalArrangement = Arrangement.spacedBy(chipSpacing),
                modifier = Modifier.fillMaxWidth()
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
                    onClick = onSelectedAccountClick
                )
            }
        }
    }
}

@Composable
fun TransferAccountChip(
    label: String,
    isPlaceholder: Boolean,
    icon: IconValue,
    onClick: () -> Unit
) {
    val borderColor = MizanTheme.premium.text.tertiary
    val cornerRadius = 50f // Full rounded

    Row(
        modifier = Modifier
            .then(
                if (isPlaceholder) {
                    Modifier.drawBehind {
                        drawRoundRect(
                            color = borderColor,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(8f, 6f),
                                    0f
                                )
                            ),
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                        )
                    }
                } else {
                    Modifier
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
                        .background(MizanTheme.premium.colors.surface2)
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MizanIcon(
            icon = icon,
            contentDescription = null,
            tint = MizanTheme.premium.text.secondary,
            modifier = Modifier.size(14.dp)
        )

        Text(
            text = label,
            style = MizanTheme.typography.bodySm,
            color = if (isPlaceholder) MizanTheme.premium.text.tertiary
            else MizanTheme.premium.text.primary,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
