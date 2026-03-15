package dev.esbi.mizan.feature.premiumaddtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.accountselector.AccountSelectionContentSimple
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.premiumaddtransaction.ui.AccountChip
import dev.esbi.mizan.feature.premiumaddtransaction.ui.AmountInputHeader
import dev.esbi.mizan.feature.premiumaddtransaction.ui.CategoryChip
import dev.esbi.mizan.feature.newtransaction.categorychooser.CategoryChooserState
import dev.esbi.mizan.feature.premiumaddtransaction.bottomsheet.CategorySelectorBottomSheet
import dev.esbi.mizan.feature.premiumaddtransaction.currency.MizanCurrencySelector
import dev.esbi.mizan.feature.premiumaddtransaction.part1.TransactionTypeSelector
import dev.esbi.mizan.feature.premiumaddtransaction.part2.MizanResizableAmount
import dev.esbi.mizan.feature.premiumaddtransaction.part2.color
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@UiComposable
@Composable
fun PremiumNewTransaction(
    state: State,
    accept: (Intent) -> Unit,
) {
    val selectAccountSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val targetAccountSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySelectSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    state.displayText
    remember { MutableInteractionSource() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AmountInputHeader(
                showTemplates = state.showTemplates,
                onTemplatesToggle = {
                    accept(Intent.ToggleTemplates)
                },
                onClose = {
                    accept(Intent.Back)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TransactionTypeSelector(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                selectedType = state.transactionType,
                onTypeSelect = {
                    accept(Intent.SelectTransactionType(it))
                }
            )
            AmountContent(
                modifier = Modifier,
                state = state,
                accept = accept
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MizanTheme.premium.spacing.md,
                        vertical = MizanTheme.premium.spacing.md
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = MizanTheme.premium.spacing.sm)
                        .weight(1f),
                ) {
                    Text(text = "Account", style = MizanTheme.typography.bodySm)
                    AccountChip(
                        modifier = Modifier
                            .padding(top = MizanTheme.premium.spacing.sm)
                            .fillMaxWidth()
                            .height(48.dp),
                        accountName = state.selectedAccount?.name,
                        onClick = {
                            accept(Intent.OpenAccountsBottomSheet)
                        }
                    )
                }
                if (state.transactionType == Transaction.Type.TRANSFER) {
                    Column(
                        modifier = Modifier
                            .padding(end = MizanTheme.premium.spacing.sm)
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "Account",
                            style = MizanTheme.typography.bodySm
                        )
                        AccountChip(
                            modifier = Modifier
                                .padding(top = MizanTheme.premium.spacing.sm)
                                .fillMaxWidth()
                                .height(48.dp),
                            accountName = state.targetAccount?.name,
                            onClick = {
                                accept(Intent.OpenTargetAccountsBottomSheet)
                            }
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .padding(end = MizanTheme.premium.spacing.sm)
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "Category",
                            style = MizanTheme.typography.bodySm
                        )
                        CategoryChip(
                            categoryName = state.selectedCategory?.name,
                            subCategoryName = state.selectedSubCategory?.name,
                            onClick = {
                                accept(Intent.OpenCategoriesBottomSheet)
                            },
                            modifier = Modifier
                                .padding(top = MizanTheme.premium.spacing.sm)
                                .fillMaxWidth()
                                .height(48.dp),
                        )
                    }
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = MizanTheme.premium.radius.xxl,
                            topEnd = MizanTheme.premium.radius.xxl
                        )
                    )
                    .background(MizanTheme.premium.background.secondary)
                    .padding(horizontal = MizanTheme.premium.spacing.lg)
                    .padding(bottom = MizanTheme.premium.spacing.lg)
                    .padding(top = MizanTheme.premium.spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MizanCurrencySelector(
                    currencies = state.currencies,
                    selectedCurrency = state.selectedCurrency,
                    onCurrencySelected = {
                        accept(Intent.OnUpdateCurrency(it))
                    }
                )
                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
                PremiumCalculatorKeypad(
                    onNumberClick = {
                        accept(Intent.OnNumberClick(it))
                    }
                )
            }

            // Save Button
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    // Glow Shadow
                    .premiumShadow(
                        shadowInfo = MizanTheme.premium.shadows.glowPrimary,
                        borderRadius = MizanTheme.premium.radius.md
                    )
                    // Gradient Background
                    .background(
                        brush = MizanTheme.premium.gradients.primary,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.sm)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            accept(Intent.Next)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Next to Confirm",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.colors.white
                )
            }
        }
    }

    when {
        state.isSelectAccountsBottomSheetVisible -> {
            ModalBottomSheet(
                onDismissRequest = {
                    accept(Intent.CloseAccountsBottomSheet)
                },
                sheetState = selectAccountSheetState,
                containerColor = MizanTheme.premium.background.primary,
                dragHandle = null
            ) {
                AccountSelectionContentSimple(
                    accounts = state.accounts,
                    selectedAccount = state.selectedAccount,
                    onAccountClick = { account ->
                        accept(Intent.UpdateSelectedAccount(account))
                    },
                    onAddAccountClick = {
                        accept(Intent.OpenAccountManageScreen)
                    },
                )
            }
        }

        state.isTargetAccountsBottomSheetVisible -> {
            ModalBottomSheet(
                onDismissRequest = {
                    accept(Intent.CloseTargetAccountsBottomSheet)
                },
                sheetState = targetAccountSheetState,
                containerColor = MizanTheme.premium.background.primary,
                dragHandle = null
            ) {
                AccountSelectionContentSimple(
                    accounts = state.accounts,
                    selectedAccount = state.targetAccount,
                    onAccountClick = { account ->
                        accept(Intent.UpdateTargetAccount(account))
                    },
                    onAddAccountClick = {
                        accept(Intent.OpenAccountManageScreen)
                    },
                )
            }
        }

        state.isCategoriesBottomSheetVisible -> {
            ModalBottomSheet(
                onDismissRequest = {
                    accept(Intent.CloseCategoriesBottomSheet)
                },
                sheetState = categorySelectSheetState,
                containerColor = MizanTheme.premium.background.primary,
                dragHandle = null
            ) {
                CategorySelectorBottomSheet(
                    state = CategoryChooserState(
                        transactionType = state.transactionType,
                        categories = state.categories,
                        selectedParentId = state.selectedCategory?.id,
                        selectedChildId = state.selectedSubCategory?.id,
                        isLoading = state.isLoading,
                        error = null,
                        selectedCategory = state.selectedSubCategory
                    ),
                    accept = accept
                )
            }
        }

    }
}

@Composable
fun AmountContent(
    modifier: Modifier = Modifier,
    state: State,
    accept: (Intent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(text = state.expression, color = MizanTheme.premium.text.primary)
            MizanResizableAmount(
                modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.sm),
                amount = state.currentValue.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                color = state.transactionType.color(),
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PremiumNewTransactionPreview() {
    MizanTheme {
        PremiumNewTransaction(
            state = State(),
            accept = {}
        )
    }
}