package dev.esbi.mizan.feature.newtransaction.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.TransactionStep
import dev.esbi.mizan.feature.newtransaction.categorychooser.CategoryChooserState
import dev.esbi.mizan.feature.newtransaction.inputcontent.ActivePads
import dev.esbi.mizan.feature.newtransaction.store.state.CameraInputState
import dev.esbi.mizan.feature.newtransaction.store.state.KeypadState
import dev.esbi.mizan.feature.newtransaction.store.state.VoiceInputState
import dev.esbi.mizan.domain.model.Account


interface NewTransactionStore :
    Store<NewTransactionStore.Intent, NewTransactionStore.State, NewTransactionStore.Label> {

    data class State(
        val inputMode: InputMode = InputMode.Manual,
        val transactionType: TransactionType = TransactionType.EXPENSE,
        val currentPage: TransactionStep = TransactionStep.AmountInput(),
        val keypadState: KeypadState = KeypadState(),
        val voiceInputState: VoiceInputState = VoiceInputState(),
        val cameraInputState: CameraInputState = CameraInputState(),
        val categoryChooserState: CategoryChooserState = CategoryChooserState(Transaction.Type.EXPENSE),
        val activePad: ActivePads = ActivePads.AmountPad(),
        val note: String = "",
        val description: String = "",
        val transactionDate: Long = System.currentTimeMillis(),
        val selectedAccountId: Long? = null,
        val targetAccountId: Long? = null,
        val accounts: List<Account> = emptyList(),
        val isAccountSheetVisible: Boolean = false,
        val saveAsTemplate: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    ) {
        companion object
    }

    sealed interface VoiceRecognitionIntent : Intent {
        data object OnStartVoiceRecognition : VoiceRecognitionIntent
        data object OnStopVoiceRecognition : VoiceRecognitionIntent
        data object OnStartListening : VoiceRecognitionIntent
        data object OnStopListening : VoiceRecognitionIntent
        class OnVoiceRecognitionError(val error: String) : VoiceRecognitionIntent
        class OnVoiceResult(val text: String) : VoiceRecognitionIntent
    }

    sealed interface CameraScanIntent : Intent {
        data object OnStartCameraScan : CameraScanIntent
        data object OnStopCameraScan : CameraScanIntent
        class OnAmountExtracted(val amount: Double) : CameraScanIntent
        class OnReceiptScanResult(val text: String, val confidence: Float) : CameraScanIntent
        class OnQrCodeScanned(val text: String) : CameraScanIntent
        class OnCameraScanError(val error: String) : CameraScanIntent
    }

    sealed interface CategoryChooserIntent : Intent {
        object LoadCategories : CategoryChooserIntent
        class SelectParentCategory(val category: Category) : CategoryChooserIntent
        class SelectSubCategory(val category: Category) : CategoryChooserIntent
        object Continue : CategoryChooserIntent
        data object RetryLoad : CategoryChooserIntent
        object ManageCategories : CategoryChooserIntent
    }

    /**
     * Intents for the Amount Input screen
     */
    sealed interface Intent {
        data object Back : Intent
        class OnModeChange(val mode: InputMode) : Intent
        class OnTypeSelect(val type: Transaction.Type) : Intent
        object OnSubmit : Intent
        object TransactionTypesShow : Intent
        data object ConfirmSave : Intent
        class UpdateNote(val note: String) : Intent
        class UpdateDate(val date: Long) : Intent
        class UpdateSelectedAccount(val accountId: Long?) : Intent
        class UpdateTargetAccount(val accountId: Long?) : Intent
        data object OpenAccountSelection : Intent
        data object CloseAccountSelection : Intent
        class SelectAccount(val accountId: Long) : Intent
        class UpdateSaveAsTemplate(val saveAsTemplate: Boolean) : Intent
    }

    sealed interface AmountInputIntent : Intent {
        class OnNumberClick(val key: Keypad) : AmountInputIntent
        data object OnNextKeyButtonClick : AmountInputIntent
    }


    sealed interface Message {
        class UpdateTransactionType(val type: TransactionType) : Message
        class UpdateKeypadState(val state: KeypadState) : Message
        class UpdateVoiceInputStateState(val state: VoiceInputState) : Message
        class UpdateMode(val mode: InputMode) : Message
        class UpdateCameraInputState(val state: CameraInputState) : Message
        class QrCodeDetected(val text: String) : Message
        class UpdateListeningState(val isListening: Boolean) : Message
        class UpdateVoiceError(val error: String?) : Message
        class UpdateVoiceResult(val result: String?) : Message

        class UpdateTransactionStep(val step: TransactionStep) : Message
        class UpdateNote(val note: String) : Message
        class UpdateDate(val date: Long) : Message
        class UpdateSelectedAccount(val accountId: Long?) : Message
        class UpdateTargetAccount(val accountId: Long?) : Message
        class UpdateAccounts(val accounts: List<Account>) : Message
        class SetAccountSheetVisible(val visible: Boolean) : Message
        class SetSaveAsTemplate(val saveAsTemplate: Boolean) : Message
        class SetLoading(val isLoading: Boolean) : Message
        class SetError(val error: String?) : Message
    }

    sealed interface CategoryChooserMessage : Message {
        class CategoriesLoaded(val categories: List<Category>) : CategoryChooserMessage
        class LoadingChanged(val isLoading: Boolean) : CategoryChooserMessage
        class ErrorChanged(val error: String?) : CategoryChooserMessage
        class ParentCategorySelected(val category: Category?) : CategoryChooserMessage
        class SubCategorySelected(val category: Category) : CategoryChooserMessage
        object NavigateToParent : CategoryChooserMessage
    }


    sealed interface Action {
        object Init : Action
    }

    /**
     * Labels for state changes
     */
    sealed interface Label {
        object MapsToNextStep : Label
        object Back : Label
        class ShowError(val message: String) : Label
        object TransactionSaved : Label
    }
}
