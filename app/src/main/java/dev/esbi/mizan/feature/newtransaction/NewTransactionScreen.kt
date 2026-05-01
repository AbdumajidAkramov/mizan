package dev.esbi.mizan.feature.newtransaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.state.ConfirmTransactionUiState
import dev.esbi.mizan.feature.newtransaction.ui.ConfirmTransactionContent
import dev.esbi.mizan.feature.newtransaction.ui.DeleteConfirmationDialog
import dev.esbi.mizan.feature.newtransaction.ui.MizanDatePickerDialog
import dev.esbi.mizan.feature.newtransaction.ui.MizanTimePickerDialog
import dev.esbi.mizan.feature.newtransaction.ui.PremiumNewTransaction
import dev.esbi.mizan.navigation.NavRoute
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.ui.components.currency.ExchangeRateBottomSheet
import dev.esbi.mizan.ui.toast.MizanToast
import dev.esbi.mizan.ui.toast.MizanToastStatus


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewTransactionScreen(
    viewModel: AmountInputViewModel,
    navController: NavHostController,
) {

    val state by viewModel.addNewTransactionState.collectAsState(initial = State())
    val accept = viewModel::onNewTransactionStoreIntent
    val labels by viewModel.addNewTransactionLabels.collectAsState(initial = null)

    // Track toast message separately to trigger recomposition
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(labels) {
        when (val currentLabel = labels) {
            Label.OpenCategoryManageScreen -> {
                navController.navigate(NavRoute.ManageCategories)
            }

            Label.NavigateToAccountManage -> {
                navController.navigate(NavRoute.AccountManagement)
            }

            Label.NavigateToTemplateManage -> {
                navController.navigate(NavRoute.AccountManagement)
            }

            Label.NavigateToAccountSelector -> {
                navController.navigate(NavRoute.AccountSelector)
            }

            Label.NavigateToCategorySelector -> {
                navController.navigate(NavRoute.CategorySelect(transactionType = "EXPENSE"))
            }

            Label.TransactionSaved -> {
                navController.navigate(NavRoute.Transactions) {
                    popUpTo(NavRoute.Transactions) {
                        inclusive = true
                    }
                }
            }

            Label.TransactionDeleted -> {
                toastMessage = "Transaction deleted"
                navController.navigate(NavRoute.Transactions) {
                    popUpTo(NavRoute.Transactions) {
                        inclusive = true
                    }
                }
            }

            Label.BackTo -> {
                navController.popBackStack()
            }

            is Label.ShowToast -> {
                toastMessage = currentLabel.message
            }

            is Label.NavigateToSubCurrency -> {
                navController.navigate(
                    NavRoute.SubCurrencyList
                )
            }

            null -> {}
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        when (state.step) {
            State.Step.CONFIRMATION -> {
                ConfirmTransactionContent(
                    state = ConfirmTransactionUiState(
                        amount = state.amount,
                        transactionType = state.transactionType,
                        categoryName = state.selectedCategory?.name,
                        subCategoryName = state.selectedSubCategory?.name,
                        categoryIcon = "",
                        accountName = state.selectedAccount?.name.orEmpty(),
                        toAccountName = state.targetAccount?.name,
                        date = state.transactionDate,
                        note = state.note,
                        saveAsTemplate = state.saveAsTemplate,
                        isLoading = state.isLoading,
                    ),
                    onNoteChange = { note ->
                        accept(Intent.UpdateNote(note))
                    },
                    onDateClick = {
                        showDatePicker = true
                    },
                    onTimeClick = {
                        showTimePicker = true
                    },
                    onConfirmClick = {
                        accept(Intent.SaveTransaction)
                    },
                    onBackClick = {
                        accept(Intent.OnCloseConfirmSave)
                    },
                    onSaveAsTemplateChange = { saveAsTemplate ->
                        accept(Intent.UpdateSaveAsTemplate(saveAsTemplate))
                    },
                    isEditMode = state.isEditMode,
                    onDeleteClick = {
                        showDeleteConfirmation = true
                    }
                )
            }

            State.Step.INPUT -> {
                PremiumNewTransaction(
                    state = state,
                    accept = accept
                )
            }
        }

        // Toast qatlami (Har doim eng tepada turadi)
        state.error?.let { error ->
            MizanToast(
                message = error,
                status = MizanToastStatus.ERROR,
                isVisible = true,
                onDismiss = { accept(Intent.CloseToast) }
            )
        }

        // Validation error toast
        toastMessage?.let { message ->
            MizanToast(
                modifier = Modifier.padding(top = 36.dp),
                message = message,
                status = MizanToastStatus.ATTENTION,
                isVisible = true,
                onDismiss = { toastMessage = null }
            )
        }
    }

    MizanDatePickerDialog(
        isVisible = showDatePicker,
        initialDate = state.transactionDate,
        onDateSelected = { newDate ->
            accept(Intent.UpdateDate(newDate))
            showDatePicker = false
        },
        onDismiss = {
            showDatePicker = false
        }
    )

    MizanTimePickerDialog(
        isVisible = showTimePicker,
        initialTime = state.transactionDate,
        onTimeSelected = { hour, minute ->
            accept(Intent.UpdateTime(hour, minute))
            showTimePicker = false
        },
        onDismiss = {
            showTimePicker = false
        }
    )

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onConfirm = {
                accept(Intent.DeleteTransaction)
                showDeleteConfirmation = false
            },
            onDismiss = {
                showDeleteConfirmation = false
            }
        )
    }

    // Exchange Rate Bottom Sheet
    if (state.isExchangeRateBottomSheetVisible) {
        val mainCurrency = state.currencies.firstOrNull { it.isMainCurrency }
        val selectedCurrency = state.selectedCurrency

        if (selectedCurrency != null && mainCurrency != null) {
            ExchangeRateBottomSheet(
                isVisible = true,
                currencyCode = selectedCurrency.code,
                mainCurrencyCode = mainCurrency.code,
                currentRate = state.manualExchangeRate ?: selectedCurrency.exchangeRate,
                transactionAmount = state.amountDecimal,
                onRateChanged = { newRate ->
                    accept(Intent.UpdateManualExchangeRate(newRate))
                },
                onSyncRate = {
                    accept(Intent.SyncExchangeRateFromCBU)
                },
                onDismiss = {
                    accept(Intent.CloseExchangeRateBottomSheet)
                }
            )
        }
    }
}

@Composable
fun Transaction.Type.color(): Color = when (this) {
    Transaction.Type.EXPENSE -> Color(0xFFF5576C)
    Transaction.Type.INCOME -> Color(0xFF4FACFE)
    Transaction.Type.TRANSFER -> Color(0xFF10B981)
}
