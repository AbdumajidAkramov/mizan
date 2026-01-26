package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.CameraInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.VoiceInputState


interface AmountInputStore :
    Store<AmountInputStore.Intent, AmountInputState, AmountInputStore.Label> {

    data class State(
        val inputMode: InputMode = InputMode.Manual,
        val transactionType: TransactionType = TransactionType.Expense,
        val keypadState: KeypadState = KeypadState(),
        val voiceInputState: VoiceInputState = VoiceInputState(),
        val cameraInputState: CameraInputState = CameraInputState()
    ) {
        companion object
    }

    /**
     * Intents for the Amount Input screen
     */
    sealed interface Intent {
        data object NavigateBack : Intent
        class OnNumberClick(val key: Keypad) : Intent
        class OnModeChange(val mode: InputMode) : Intent
        object OnSubmit : Intent

        //
        data object OnStartVoiceRecognition : Intent
        data object OnStopVoiceRecognition : Intent
        data object OnStartListening : Intent
        data object OnStopListening : Intent
        class OnVoiceRecognitionError(val error: String) : Intent
        class OnVoiceResult(val text: String) : Intent

        // Camera
        data object OnStartCameraScan : Intent
        data object OnStopCameraScan : Intent
        class OnAmountExtracted(val amount: Double) : Intent
        class OnReceiptScanResult(
            val text: String,
            val confidence: Float
        ) : Intent

        class OnQrCodeScanned(val text: String) : Intent
        class OnCameraScanError(val error: String) : Intent

        data object OnKeypadNext : Intent

        class OnTypeSelect(val type: TransactionType) : Intent
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
    }
}
