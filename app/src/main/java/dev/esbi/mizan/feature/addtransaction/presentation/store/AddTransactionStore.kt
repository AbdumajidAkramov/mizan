package dev.esbi.mizan.feature.addtransaction.presentation.store

import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.utils.annotatedString
import java.time.LocalDate

internal interface AddTransactionStore :
    Store<AddTransactionStore.Intent, AddTransactionStore.State, AddTransactionStore.Label> {
    data class State(
        val flowState: FlowState = FlowState.Amount,
        val inputMode: InputMode = InputMode.Manual,
        val type: TransactionType = TransactionType.EXPENSE,

        val currency: String = "UZS",

        val leftNumber: String = "",
        val rightNumber: String = "",
        val operator: String = "",

        // Category Selection State
        val selectedCategory: String? = null,
        val availableCategories: List<Category> = emptyList(),
        val availableSubcategories: List<Category> = emptyList(),
        val selectedParentCategory: String? = null,
        val isShowingSubcategories: Boolean = false,

        // Account Selection State
        val availableAccounts: List<Account> = emptyList(),
        val transferSource: Account? = null,
        val transferDestination: Account? = null,

        // Date & Note State
        val selectedDate: LocalDate = LocalDate.now(),
        val notes: String = "",
        val showDatePicker: Boolean = false,
        val showNotesInput: Boolean = false,

        // Voice Recognition State
        val isListening: Boolean = false,
        val voiceRecognitionResult: String = "",

        // Camera Recognition State
        val isScanning: Boolean = false,
        val cameraRecognitionResult: String = "",
        val cameraPermissionGranted: Boolean = false,

        // UI State
        val showKeypad: Boolean = true,
        val showVoiceInput: Boolean = false,
        val showCameraInput: Boolean = false,

        val date: LocalDate = LocalDate.now(),
        // Dynamic Data Fields
        val fromAccountId: String? = null,
        val toAccountId: String? = null,
        val receiptScanText: String = "",
        val lastRecognizedAmount: Double = 0.0,
        val voiceRecognitionError: String? = null,
        val cameraScanError: String? = null
    ) {

        val isLeftNumberActive: Boolean = operator.isEmpty()
        val displayText: String
            get() {
                return if (operator.isEmpty()) {
                    leftNumber
                } else {
                    "$leftNumber $operator $rightNumber"
                }
            }

        val amountText: String
            get() {
                return if (operator.isEmpty()) {
                    leftNumber
                } else {
                    rightNumber
                }
            }

        val amount: Double get() = amountText.toDoubleOrNull() ?: 0.0

        val annotatedString: AnnotatedString get() = amountText.annotatedString(currency = currency)

        val isFormValid = amount > 0.0 &&
                (type != TransactionType.Transfer || (transferSource != null && transferDestination != null)) &&
                (type == TransactionType.Transfer || selectedCategory != null)

    }

    sealed interface Intent {
        data object BackToPrev : Intent
        data object OnKeypadNext : Intent
        data object OnKeypadBack : Intent
        data object OnNextTransfer : Intent
        data object OnSaveTransaction : Intent
        
        // Keypad Intents
        data class OnKeypadClick(val key: Keypad) : Intent
        
        // Category Selection Intents
        data class OnCategorySelect(val category: String) : Intent
        data class OnParentCategorySelect(val parentCategory: String) : Intent
        data class OnSubcategorySelect(val subcategory: String) : Intent
        data object OnBackToCategories : Intent
        data object OnManageCategories : Intent
        
        // Transaction Type Intents
        data class OnTransactionTypeChange(val type: TransactionType) : Intent
        data class OnTransactionTypeSelect(val type: TransactionType) : Intent
        data class OnInputModeChange(val inputMode: InputMode) : Intent
        
        // Account Selection Intents
        data class OnSelectFromAccount(val accountId: String? = null) : Intent
        data class OnSelectToAccount(val accountId: String? = null) : Intent
        
        // Date & Note Intents
        data class OnDateChange(val date: LocalDate) : Intent
        data class OnNoteChange(val notes: String) : Intent
        
        // Voice Recognition Intents
        data object OnStartVoiceRecognition : Intent
        data object OnStopVoiceRecognition : Intent
        class OnVoiceRecognitionError(val error: String) : Intent

        // Camera Recognition Intents
        data object OnStartCameraRecognition : Intent
        data object OnStopCameraRecognition : Intent
        data object OnCameraPermissionDenied : Intent
        
        // UI State Intents
        data object Close : Intent
    }

    sealed interface Label {
        data object Close : Label
    }

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Message {
        class UpdateFlowState(val flowState: FlowState) : Message
        class UpdateInputMode(val inputMode: InputMode) : Message
        class UpdateLeftText(val text: String) : Message
        class UpdateRightText(val text: String) : Message
        class UpdateOperator(val text: String) : Message
        class UpdateTransactionCategory(val category: String) : Message
        class UpdateTransactionType(val type: TransactionType) : Message
        class UpdateTransactionDate(val date: LocalDate) : Message
        class UpdateTransactionNotes(val notes: String) : Message
        data object ClearText : Message
        
        // Dynamic Data Messages
        class UpdateAvailableCategories(val categories: List<Category>) : Message
        class UpdateAvailableSubcategories(val subcategories: List<Category>) : Message
        class UpdateAvailableAccounts(val accounts: List<Account>) : Message
        class UpdateTransferSource(val account: Account?) : Message
        class UpdateTransferDestination(val account: Account?) : Message
        class UpdateSelectedParentCategory(val parentCategory: String?) : Message
        class UpdateShowingSubcategories(val showing: Boolean) : Message
        
        // Voice Recognition Messages
        class UpdateVoiceListeningState(val isListening: Boolean) : Message
        class UpdateVoiceRecognitionText(val text: String) : Message
        class UpdateVoiceRecognitionError(val error: String?) : Message
        
        // Camera Scan Messages
        class UpdateCameraScanningState(val isScanning: Boolean) : Message
        class UpdateReceiptScanText(val text: String) : Message
        class UpdateCameraScanError(val error: String?) : Message
        class UpdateRecognizedAmount(val amount: Double) : Message
    }
}
