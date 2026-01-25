package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.VoiceInputState


interface AmountInputStore : Store<AmountInputStore.Intent, AmountInputState, AmountInputStore.Label> {

    data class State(
        val inputMode: InputMode = InputMode.Manual,
        val keypadState: KeypadState = KeypadState(),
        val voiceInputState: VoiceInputState = VoiceInputState()
    ) {
        companion object
    }
    /**
     * Intents for the Amount Input screen
     */
    sealed interface Intent {
        class OnNumberClick(val key: Keypad) : Intent
        class OnModeChange(val mode: InputMode) : Intent
        object OnSubmit : Intent

        //
        data object OnStartVoiceRecognition : Intent
        data object OnStopVoiceRecognition : Intent
        class OnVoiceRecognitionError(val error: String) : Intent
    }

    sealed interface Message {
        class UpdateKeypadState(val state: KeypadState) : Message
        class UpdateVoiceInputStateState(val state: VoiceInputState) : Message
        class UpdateMode(val mode: InputMode) : Message
    }

    sealed interface Action{
        object Init : Action
    }

    /**
     * Labels for state changes
     */
    sealed interface Label {
        object MapsToNextStep : Label
    }
}
