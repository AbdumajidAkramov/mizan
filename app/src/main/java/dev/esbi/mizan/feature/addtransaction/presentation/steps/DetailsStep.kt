package dev.esbi.mizan.feature.addtransaction.presentation.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumAccountSelector
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCategoryPicker
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.SubcategoryPicker
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
        availableSubcategories = state.availableSubcategories,
        selectedParentCategory = state.selectedParentCategory,
        isShowingSubcategories = state.isShowingSubcategories,
        availableAccounts = state.availableAccounts,
        transferSource = state.transferSource,
        transferDestination = state.transferDestination,
        onSelectCategory = { category -> accept(AddTransactionStore.Intent.OnCategorySelect(category)) },
        onSelectParentCategory = { parentCategory ->
            accept(
                AddTransactionStore.Intent.OnParentCategorySelect(
                    parentCategory
                )
            )
        },
        onSelectSubcategory = { subcategory ->
            accept(
                AddTransactionStore.Intent.OnSubcategorySelect(
                    subcategory
                )
            )
        },
        onBackToCategories = { accept(AddTransactionStore.Intent.OnBackToCategories) },
        onManageCategories = { accept(AddTransactionStore.Intent.OnManageCategories) },
        onSelectFromAccount = { account ->
            accept(
                AddTransactionStore.Intent.OnSelectFromAccount(
                    account.id
                )
            )
        },
        onSelectToAccount = { account -> accept(AddTransactionStore.Intent.OnSelectToAccount(account.id)) },
        onNextTransfer = { accept(AddTransactionStore.Intent.OnNextTransfer) }
    )
}

@Composable
fun DetailsStep(
    amount: String,
    currency: String,
    type: TransactionType,
    selectedCategory: String?,
    availableCategories: List<Category>,
    availableSubcategories: List<Category>,
    selectedParentCategory: String?,
    isShowingSubcategories: Boolean,
    availableAccounts: List<Account>,
    transferSource: Account?,
    transferDestination: Account?,
    onSelectCategory: (String) -> Unit,
    onSelectParentCategory: (String) -> Unit,
    onSelectSubcategory: (String) -> Unit,
    onBackToCategories: () -> Unit,
    onManageCategories: () -> Unit,
    onSelectFromAccount: (Account) -> Unit,
    onSelectToAccount: (Account) -> Unit,
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
        when (type) {
            TransactionType.Expense, TransactionType.Income -> {
                // Premium Category Selection with hierarchical navigation
                Column {
                    if (isShowingSubcategories) {
                        // Show subcategories with back navigation
                        SubcategoryPicker(
                            subcategories = availableSubcategories,
                            parentCategoryName = selectedParentCategory ?: "",
                            selectedSubcategory = selectedCategory,
                            onSelectSubcategory = onSelectSubcategory,
                            onBackToCategories = onBackToCategories,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // Show main categories with manage button
                        Column {
                            PremiumCategoryPicker(
                                categories = availableCategories,
                                selectedCategory = selectedCategory,
                                onSelectCategory = onSelectCategory,
                                onSelectParentCategory = onSelectParentCategory,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Manage Categories Button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = MizanTheme.premium.spacing.lg)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                                        .background(MizanTheme.premium.colors.surface2)
                                        .clickable { onManageCategories() }
                                        .padding(MizanTheme.premium.spacing.md),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        icon = IconValue("edit"),
                                        modifier = Modifier.size(20.dp),
                                        tint = MizanTheme.premium.text.secondary
                                    )

                                    Text(
                                        text = "Manage Categories",
                                        style = MizanTheme.typography.bodyMd,
                                        color = MizanTheme.premium.text.secondary
                                    )

                                    Icon(
                                        icon = IconValue("chevron_right"),
                                        modifier = Modifier.size(20.dp),
                                        tint = MizanTheme.premium.text.tertiary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            TransactionType.Transfer -> {
                // Account Selection for Transfers
                Column(
                    verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xl)
                ) {
                    // From Account Selector
                    PremiumAccountSelector(
                        label = "From Account",
                        selectedAccountId = transferSource?.id,
                        onSelectAccount = { accountId ->
                            val account = availableAccounts.find { it.id == accountId }
                            onSelectFromAccount(account ?: return@PremiumAccountSelector)
                        }
                    )

                    // To Account Selector
                    PremiumAccountSelector(
                        label = "To Account",
                        selectedAccountId = transferDestination?.id,
                        onSelectAccount = { accountId ->
                            val account = availableAccounts.find { it.id == accountId }
                            onSelectToAccount(account ?: return@PremiumAccountSelector)
                        }
                    )

                    // Next Button
                    Button(
                        onClick = onNextTransfer,
                        enabled = transferSource != null && transferDestination != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (transferSource != null && transferDestination != null) {
                                MizanTheme.premium.colors.primary
                            } else {
                                MizanTheme.premium.colors.surface3
                            }
                        )
                    ) {
                        Text(
                            text = "Next",
                            style = MizanTheme.typography.bodyLg,
                            fontWeight = FontWeight.Medium,
                            color = if (transferSource != null && transferDestination != null) {
                                Color.White
                            } else {
                                MizanTheme.premium.text.muted
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun DetailsStepPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme() {
        DetailsStep(
            amount = "90000",
            currency = "UZS",
            type = TransactionType.Expense,
            selectedCategory = "food",
            availableCategories = emptyList(),
            availableSubcategories = emptyList(),
            selectedParentCategory = "",
            isShowingSubcategories = false,
            availableAccounts = emptyList(),
            transferSource = null,
            transferDestination = null,
            onSelectCategory = { },
            onSelectParentCategory = { },
            onSelectSubcategory = { },
            onBackToCategories = { },
            onManageCategories = { },
            onSelectFromAccount = { },
            onSelectToAccount = { },
            onNextTransfer = { }
        )
    }
}
