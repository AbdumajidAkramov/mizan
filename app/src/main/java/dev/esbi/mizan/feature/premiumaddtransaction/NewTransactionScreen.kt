package dev.esbi.mizan.feature.premiumaddtransaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputViewModel
import dev.esbi.mizan.feature.newtransaction.confirm.ConfirmTransactionContent
import dev.esbi.mizan.feature.newtransaction.confirm.MizanDatePickerDialog
import dev.esbi.mizan.feature.newtransaction.confirm.MizanTimePickerDialog
import dev.esbi.mizan.feature.newtransaction.confirm.state.ConfirmTransactionUiState
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.ui.toast.MizanToast
import dev.esbi.mizan.ui.toast.MizanToastStatus


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewTransactionScreen(
    viewModel: AmountInputViewModel,
    onBackPressed: () -> Unit,
    onSubmit: () -> Unit,
    onNavigateToManageCategories: () -> Unit,
    onNavigateToAccountManage: () -> Unit,
    onNavigateToAccountSelector: () -> Unit,
    onNavigateToCategorySelector: () -> Unit
) {

    val state by viewModel.addNewTransactionState.collectAsState(initial = State())
    val accept = viewModel::onNewTransactionStoreIntent
    val labels by viewModel.addNewTransactionLabels.collectAsState(initial = null)

    // Track toast message separately to trigger recomposition
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(labels) {
        when (val currentLabel = labels) {
            Label.OpenCategoryManageScreen -> onNavigateToManageCategories()
            Label.NavigateToAccountManage -> onNavigateToAccountManage()
            Label.NavigateToTemplateManage -> onNavigateToAccountManage()
            Label.NavigateToAccountSelector -> onNavigateToAccountSelector()
            Label.NavigateToCategorySelector -> onNavigateToCategorySelector()
            Label.TransactionSaved -> onSubmit()
            Label.TransactionDeleted -> {
                toastMessage = "Transaction deleted"
                onSubmit()
            }
            Label.BackTo -> onBackPressed()
            is Label.ShowToast -> {
                toastMessage = currentLabel.message
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
                status = MizanToastStatus.ATTENTION,
                isVisible = true,
                onDismiss = { accept(Intent.CloseToast) }
            )
        }

        // Validation error toast
        toastMessage?.let { message ->
            MizanToast(
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

}
