package dev.esbi.mizan.feature.addtransaction.presentation.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.DynamicAccountSelector
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.DynamicCategoryGrid
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

// ==========================================
// 1. DETAILS STEP (Kategoriya yoki Akkaunt tanlash)
// ==========================================

@Composable
internal fun DetailsStep(
    state: AddTransactionStore.State,
    accept: (AddTransactionStore.Intent) -> Unit,
) {
    DetailsStep(
        amount = state.amountText,
        currency = state.currency,
        type = state.type,
        selectedCategory = state.selectedCategory,
        availableCategories = state.availableCategories,
        availableAccounts = state.availableAccounts,
        transferSource = state.transferSource,
        transferDestination = state.transferDestination,
        onSelectCategory = {
            accept(AddTransactionStore.Intent.OnCategorySelect(it))
        },
        onSelectFromAccount = { account ->
            accept(AddTransactionStore.Intent.OnSelectFromAccount(account.id))
        },
        onSelectToAccount = { account ->
            accept(AddTransactionStore.Intent.OnSelectToAccount(account.id))
        },
        onNextTransfer = {
            accept(AddTransactionStore.Intent.OnNextTransfer)
        }
    )
}

@Composable
fun DetailsStep(
    amount: String,
    currency: String,
    type: TransactionType,
    selectedCategory: String?,
    availableCategories: List<dev.esbi.mizan.feature.addtransaction.domain.model.Category>,
    availableAccounts: List<dev.esbi.mizan.feature.addtransaction.domain.model.Account>,
    transferSource: dev.esbi.mizan.feature.addtransaction.domain.model.Account?,
    transferDestination: dev.esbi.mizan.feature.addtransaction.domain.model.Account?,
    onSelectCategory: (String) -> Unit,
    onSelectFromAccount: (dev.esbi.mizan.feature.addtransaction.domain.model.Account) -> Unit,
    onSelectToAccount: (dev.esbi.mizan.feature.addtransaction.domain.model.Account) -> Unit,
    onNextTransfer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.xl
            )
    ) {
        // --- HEADER SUMMARY (Amount & Type) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MizanTheme.premium.spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = type.name, // Expense, Income, Transfer
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = amount.annotatedString(currency = currency),
                style = MizanTheme.typography.displaySm, // heading-2xl ga mos
                color = MizanTheme.premium.text.primary
            )
        }

        // --- CONTENT ---
        if (type == TransactionType.Transfer) {
            // TRANSFER UCHUN AKKAUNTLAR
            Column(verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xl)) {

                DynamicAccountSelector(
                    accounts = availableAccounts,
                    selectedAccount = transferSource,
                    onSelectAccount = onSelectFromAccount,
                    label = "From Account",
                    excludeAccount = transferDestination
                )

                DynamicAccountSelector(
                    accounts = availableAccounts,
                    selectedAccount = transferDestination,
                    onSelectAccount = onSelectToAccount,
                    label = "To Account",
                    excludeAccount = transferSource
                )

                // Next Button
                val isEnabled = transferSource != null && transferDestination != null
                Button(
                    onClick = onNextTransfer,
                    enabled = isEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50), // rounded-full
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEnabled) Color(0xFF10B981) else MizanTheme.premium.colors.surface3, // Emerald color
                        contentColor = if (isEnabled) Color.White else MizanTheme.premium.text.muted,
                        disabledContainerColor = MizanTheme.premium.colors.surface3,
                        disabledContentColor = MizanTheme.premium.text.muted
                    )
                ) {
                    Text(
                        text = "Next",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_chevron_right),
                        modifier = Modifier.size(20.dp),
                        tint = MizanTheme.premium.text.primary
                    )
                }
            }
        } else {
            // EXPENSE / INCOME UCHUN KATEGORIYA
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Choose a category",
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.lg)
                )

                DynamicCategoryGrid(
                    categories = availableCategories,
                    selectedCategory = selectedCategory,
                    onSelectCategory = onSelectCategory
                )
            }
        }
    }
}
