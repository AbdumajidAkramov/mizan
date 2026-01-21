package dev.esbi.mizan.feature.newtransaction.root

import androidx.compose.runtime.Immutable
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import java.time.LocalDate

@Immutable
data class NewTransactionState(
    // Amount Input State
    val displayValue: String = "0",
    val calculationString: String = "",
    val operator: String? = null,
    val previousValue: Double? = null,
    val inputMode: InputMode = InputMode.Manual,
    val isListening: Boolean = false,
    val isScanning: Boolean = false,
    
    // Navigation State
    val currentStep: NewTransactionStep = NewTransactionStep.AMOUNT_INPUT,
    val transactionType: TransactionType? = null,
    
    // Details State
    val categoryId: String? = null,
    val subcategoryId: String? = null,
    val fromAccountId: String? = null,
    val toAccountId: String? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val notes: String = "",
    val showDatePicker: Boolean = false,
    val showNotesInput: Boolean = false,
    
    // Available Data
    val availableCategories: List<Category> = emptyList(),
    val availableAccounts: List<Account> = emptyList()
) {
    // Navigation Validation
    val canProceedToType: Boolean get() = displayValue.toDoubleOrNull() ?: 0.0 > 0
    val canProceedToDetails: Boolean get() = transactionType != null
    val canProceedToConfirm: Boolean = when (transactionType) {
        TransactionType.Transfer -> fromAccountId != null && toAccountId != null
        else -> categoryId != null && fromAccountId != null
    }
    val canSave: Boolean get() = (displayValue.toDoubleOrNull() ?: 0.0) > 0 && transactionType != null && canProceedToConfirm
}

enum class NewTransactionStep {
    AMOUNT_INPUT,
    TRANSACTION_TYPE,
    DETAILS,
    CONFIRM
}

sealed interface NewTransactionIntent {
    // Navigation Intents
    data object NavigateToType : NewTransactionIntent
    data object NavigateToDetails : NewTransactionIntent
    data object NavigateToConfirm : NewTransactionIntent
    data object NavigateBack : NewTransactionIntent
    data object Reset : NewTransactionIntent
    data object LoadInitialData : NewTransactionIntent
    
    // Amount Input Intents
    data class OnNumberClick(val key: Keypad) : NewTransactionIntent
    data class OnOperatorClick(val operator: String) : NewTransactionIntent
    data object OnEqualsClick : NewTransactionIntent
    data object OnDeleteClick : NewTransactionIntent
    data object OnDecimalClick : NewTransactionIntent
    data object OnClearClick : NewTransactionIntent
    data class OnInputModeChange(val mode: InputMode) : NewTransactionIntent
    
    // Voice Input Intents
    data object OnStartListening : NewTransactionIntent
    data object OnStopListening : NewTransactionIntent
    data class OnVoiceParsed(val result: VoiceParseResult) : NewTransactionIntent
    
    // Scan Input Intents
    data object OnStartScanning : NewTransactionIntent
    data object OnStopScanning : NewTransactionIntent
    data class OnScanResult(val amount: Double) : NewTransactionIntent
    
    // Transaction Type Intent
    data class OnTransactionTypeSelect(val type: TransactionType) : NewTransactionIntent
    
    // Category Selection Intent
    data class OnCategorySelect(val category: String, val subcategory: String? = null) : NewTransactionIntent
    
    // Account Selection Intents
    data class OnFromAccountSelect(val accountId: String) : NewTransactionIntent
    data class OnToAccountSelect(val accountId: String) : NewTransactionIntent
    
    // Date and Notes Intents
    data class OnDateChange(val date: LocalDate) : NewTransactionIntent
    data class OnNotesChange(val notes: String) : NewTransactionIntent
    data object OnToggleDatePicker : NewTransactionIntent
    data object OnToggleNotesInput : NewTransactionIntent
    
    // Save Intent
    data object OnSaveTransaction : NewTransactionIntent
}

@Immutable
data class VoiceParseResult(
    val amount: Double? = null,
    val type: TransactionType? = null,
    val category: String? = null,
    val fromAccount: String? = null,
    val toAccount: String? = null,
    val description: String? = null
)

sealed interface NewTransactionMessage {
    data class UpdateDisplayValue(val value: String) : NewTransactionMessage
    data class UpdateCalculationString(val string: String) : NewTransactionMessage
    data class UpdateOperator(val operator: String?) : NewTransactionMessage
    data class UpdatePreviousValue(val value: Double?) : NewTransactionMessage
    data class UpdateInputMode(val mode: InputMode) : NewTransactionMessage
    data class UpdateListeningState(val isListening: Boolean) : NewTransactionMessage
    data class UpdateScanningState(val isScanning: Boolean) : NewTransactionMessage
    data class UpdateTransactionType(val type: TransactionType) : NewTransactionMessage
    data class UpdateCategory(val category: String?, val subcategory: String? = null) : NewTransactionMessage
    data class UpdateFromAccount(val accountId: String?) : NewTransactionMessage
    data class UpdateToAccount(val accountId: String?) : NewTransactionMessage
    data class UpdateDate(val date: LocalDate) : NewTransactionMessage
    data class UpdateNotes(val notes: String) : NewTransactionMessage
    data class UpdateDatePickerVisibility(val visible: Boolean) : NewTransactionMessage
    data class UpdateNotesInputVisibility(val visible: Boolean) : NewTransactionMessage
    data class NavigateToStep(val step: NewTransactionStep) : NewTransactionMessage
    data class UpdateAvailableCategories(val categories: List<dev.esbi.mizan.feature.addtransaction.domain.model.Category>) : NewTransactionMessage
    data class UpdateAvailableAccounts(val accounts: List<dev.esbi.mizan.feature.addtransaction.domain.model.Account>) : NewTransactionMessage
}

sealed interface NewTransactionAction {
    data object LoadInitialData : NewTransactionAction
    data object SaveTransaction : NewTransactionAction
}
