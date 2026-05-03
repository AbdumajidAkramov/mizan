package dev.esbi.mizan.feature.newtransaction.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.color
import dev.esbi.mizan.feature.newtransaction.ui.accountselector.AccountSelectionContentSimple
import dev.esbi.mizan.feature.newtransaction.ui.categoryselector.CategoryChooserState
import dev.esbi.mizan.feature.newtransaction.ui.categoryselector.CategorySelectorBottomSheet
import dev.esbi.mizan.feature.newtransaction.ui.header.AddTransactionHeader
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow

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
            AddTransactionHeader(
                showTemplates = state.showTemplates,
                isEditMode = state.isEditMode,
                onTemplatesToggle = {
                    accept(Intent.ToggleTemplates)
                },
                onClose = {
                    accept(Intent.Back)
                },
                modifier = Modifier.background(color = Color.Transparent)
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
                    .padding(top = 8.dp)
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
                onClick = {},
                onExchangeRateClick = {
                    accept(Intent.OpenExchangeRateBottomSheet)
                }
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
                            topStart = MizanTheme.premium.radius.xl,
                            topEnd = MizanTheme.premium.radius.xl
                        )
                    )
                    .background(MizanTheme.premium.background.secondary)
                    .padding(horizontal = MizanTheme.premium.spacing.lg)
                    .padding(bottom = MizanTheme.premium.spacing.lg)
                    .padding(top = MizanTheme.premium.spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddTransactionCurrencySelector(
                    modifier = Modifier.padding(vertical = 8.dp),
                    currencies = state.currencies,
                    selectedCurrency = state.selectedCurrency,
                    onCurrencySelected = {
                        accept(Intent.OnUpdateCurrency(it))
                    },
                    onAddNewSubCurrency = {
                        accept(Intent.OnAddSubCategory)
                    }
                )
                PremiumCalculatorKeypad(
                    modifier = Modifier.padding(top = 8.dp),
                    onNumberClick = {
                        accept(Intent.OnNumberClick(it))
                    }
                )

                // Save Button
                Box(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        // Glow Shadow
                        .premiumShadow(
                            shadowInfo = MizanTheme.premium.shadows.glowPrimary
                                .copy(
                                    color = state.transactionType.color()
                                        .copy(
                                            alpha = 0.3f
                                        )
                                ),
                            borderRadius = MizanTheme.premium.radius.md
                        )
                        // Gradient Background
                        .background(
                            color = state.transactionType.color(),
                            shape = RoundedCornerShape(MizanTheme.premium.radius.full)
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

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "PremiumNewTransactionPreviewNight"
)
@Composable
fun PremiumNewTransactionPreviewNight() {
    MizanTheme {
        PremiumNewTransaction(
            state = State(),
            accept = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
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
