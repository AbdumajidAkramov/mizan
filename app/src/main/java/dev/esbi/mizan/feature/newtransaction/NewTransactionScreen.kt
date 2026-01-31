package dev.esbi.mizan.feature.newtransaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.accountselect.AccountSelectionContent
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputContent
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputViewModel
import dev.esbi.mizan.feature.newtransaction.categorychooser.CategoryChooserContentV2
import dev.esbi.mizan.feature.newtransaction.confirm.ConfirmTransactionContent
import dev.esbi.mizan.feature.newtransaction.confirm.MizanDatePickerDialog
import dev.esbi.mizan.feature.newtransaction.confirm.state.ConfirmTransactionUiState
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.categoryselect.CategorySelectionSheet
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeContent
import dev.esbi.mizan.feature.newtransaction.transactiontype.PremiumTransactionTypeSelector
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewTransactionScreen(
    viewModel: AmountInputViewModel,
    onBackPressed: () -> Unit,
    onSubmit: () -> Unit,
    onNavigateToManageCategories: () -> Unit
) {
    val labels by viewModel.labels.collectAsState(initial = null)
    val state by viewModel.state.collectAsState(initial = NewTransactionStore.State())
    val accept = viewModel::onIntent
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    val accountSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Get selected account name
    val selectedAccountName = remember(state.selectedAccountId, state.accounts) {
        state.accounts.find { it.id == state.selectedAccountId }?.name
    }
    LaunchedEffect(labels) {
        when (labels) {
            NewTransactionStore.Label.MapsToNextStep -> onSubmit()
            NewTransactionStore.Label.TransactionSaved -> onSubmit()
            NewTransactionStore.Label.Back -> onBackPressed()
            is NewTransactionStore.Label.ShowError -> {
                Toast.makeText(
                    context,
                    (labels as NewTransactionStore.Label.ShowError).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            null -> { /* Ignore */
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
    ) {
        when (state.currentPage) {
            is TransactionStep.AmountInput -> {
                // Get selected account and category
                val selectedAccount = state.accounts.find { it.id == state.selectedAccountId }
                val selectedCategory = state.categoryChooserState.selectedCategory
                val selectedSubCategory =
                    state.categoryChooserState.selectedChildId?.let { childId ->
                        state.categoryChooserState.categories.find { it.id == childId }
                    }

                AmountInputContent(
                    state = state,
                    selectedAccount = selectedAccount,
                    selectedCategory = selectedCategory,
                    selectedSubCategory = selectedSubCategory,
                    onTypeClick = { accept(NewTransactionStore.Intent.ShowTypeSelector) },
                    onCategoryClick = { accept(NewTransactionStore.Intent.OpenCategorySheet) },
                    onAccountClick = { accept(NewTransactionStore.Intent.OpenAccountSelection) },
                    accept = accept
                )
            }

            is TransactionStep.TypeSelector -> {
                TransactionTypeContent(
                    amount = state.keypadState.amountText,
                    onTypeSelect = { type ->
                        accept(NewTransactionStore.Intent.OnTypeSelect(type))
                    }
                )
            }

            is TransactionStep.CategoryChooser -> {
                CategoryChooserContentV2(
                    amount = state.keypadState.amountText,
                    state = state.categoryChooserState,
                    selectedAccountName = selectedAccountName,
                    accept = accept,
                    onBack = { accept(NewTransactionStore.Intent.Back) },
                    onClose = onBackPressed,
                    onAccountClick = { accept(NewTransactionStore.Intent.OpenAccountSelection) }
                )
            }

            is TransactionStep.Transfer -> {

            }

            is TransactionStep.ConfirmSave -> {
                // Get selected account details
                val selectedAccount = state.accounts.find { it.id == state.selectedAccountId }
                val targetAccount = state.accounts.find { it.id == state.targetAccountId }

                // Get subcategory name if child is selected
                val subCategoryName = state.categoryChooserState.selectedChildId?.let { childId ->
                    state.categoryChooserState.categories.find { it.id == childId }?.name
                }

                val confirmState = ConfirmTransactionUiState(
                    amount = state.keypadState.amountText.ifEmpty { "0" },
                    currencyCode = selectedAccount?.currency?.code ?: "UZS",
                    transactionType = state.transactionType ?: Transaction.Type.EXPENSE,
                    categoryName = state.categoryChooserState.selectedCategory?.name,
                    subCategoryName = subCategoryName,
                    categoryIcon = state.categoryChooserState.selectedCategory?.iconName,
                    accountName = selectedAccount?.name ?: "No Account",
                    toAccountName = targetAccount?.name,
                    date = state.transactionDate,
                    note = state.note,
                    saveAsTemplate = state.saveAsTemplate,
                    isLoading = state.isLoading
                )

                ConfirmTransactionContent(
                    state = confirmState,
                    onNoteChange = { note ->
                        accept(NewTransactionStore.Intent.UpdateNote(note))
                    },
                    onDateClick = {
                        showDatePicker = true
                    },
                    onConfirmClick = {
                        accept(NewTransactionStore.Intent.ConfirmSave)
                    },
                    onBackClick = {
                        accept(NewTransactionStore.Intent.Back)
                    },
                    onSaveAsTemplateChange = { saveAsTemplate ->
                        accept(NewTransactionStore.Intent.UpdateSaveAsTemplate(saveAsTemplate))
                    }
                )

                MizanDatePickerDialog(
                    isVisible = showDatePicker,
                    initialDate = state.transactionDate,
                    onDateSelected = { newDate ->
                        accept(NewTransactionStore.Intent.UpdateDate(newDate))
                        showDatePicker = false
                    },
                    onDismiss = {
                        showDatePicker = false
                    }
                )
            }

            else -> {}
        }
    }

    // Transaction Type Selector Overlay
    if (state.isTypeSelectorVisible) {
        PremiumTransactionTypeSelector(
            isVisible = state.isTypeSelectorVisible,
            selectedType = state.transactionType ?: TransactionType.EXPENSE,
            onTypeSelected = { type ->
                accept(NewTransactionStore.Intent.SelectTransactionType(type))
            },
            onDismiss = {
                accept(NewTransactionStore.Intent.HideTypeSelector)
            }
        )
    }

    // Category Selection Sheet
    if (state.isCategorySheetVisible) {
        CategorySelectionSheet(
            isVisible = state.isCategorySheetVisible,
            categories = state.categoryChooserState.categories,
            selectedParentId = state.categoryChooserState.selectedParentId,
            selectedChildId = state.categoryChooserState.selectedChildId,
            onParentSelected = { category ->
                accept(NewTransactionStore.Intent.SelectParentCategory(category))
            },
            onChildSelected = { category ->
                accept(NewTransactionStore.Intent.SelectChildCategory(category))
            },
            onNavigateToManageCategories = onNavigateToManageCategories,
            onBack = {
                accept(NewTransactionStore.Intent.CloseCategorySheet)
            },
            onDismiss = {
                accept(NewTransactionStore.Intent.CloseCategorySheet)
            }
        )
    }

    // Account Selection Bottom Sheet
    if (state.isAccountSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { accept(NewTransactionStore.Intent.CloseAccountSelection) },
            sheetState = accountSheetState,
            containerColor = MizanTheme.premium.background.primary,
            dragHandle = null
        ) {
            AccountSelectionContent(
                accounts = state.accounts,
                selectedAccountId = state.selectedAccountId,
                onAccountClick = { account ->
                    accept(NewTransactionStore.Intent.SelectAccount(account.id))
                },
                onAddAccountClick = {
                    // TODO: Navigate to add account
                    accept(NewTransactionStore.Intent.CloseAccountSelection)
                },
                onClose = { accept(NewTransactionStore.Intent.CloseAccountSelection) }
            )
        }
    }

}
