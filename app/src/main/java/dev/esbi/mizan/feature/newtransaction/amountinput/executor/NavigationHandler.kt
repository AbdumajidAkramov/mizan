package dev.esbi.mizan.feature.newtransaction.amountinput.executor

import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.VoiceInputState
import javax.inject.Inject

/**
 * Handles navigation logic and validation for the Amount Input screen.
 * Manages state transitions and publishes navigation labels.
 */
internal class NavigationHandler @Inject constructor() {
    
    /**
     * Handles mode change logic
     */
    fun handleModeChange(mode: InputMode): AmountInputStore.Message {
        return AmountInputStore.Message.UpdateMode(mode)
    }
    
    /**
     * Handles submit logic with validation
     * Returns a label if navigation should occur, null otherwise
     */
    fun handleSubmit(state: AmountInputState): AmountInputStore.Label? {
        return when (state.inputMode) {
            InputMode.Manual -> {
                if (state.keypadState.canSubmit) {
                    AmountInputStore.Label.MapsToNextStep
                } else {
                    null
                }
            }
            InputMode.Voice -> {
                if (state.voiceInputState.isValid) {
                    AmountInputStore.Label.MapsToNextStep
                } else {
                    null
                }
            }
            InputMode.Scan -> {
                // TODO: Implement scan validation logic
                AmountInputStore.Label.MapsToNextStep
            }
        }
    }
    
    /**
     * Validates current state for submit eligibility
     */
    fun canSubmit(state: AmountInputState): Boolean {
        return when (state.inputMode) {
            InputMode.Manual -> state.keypadState.canSubmit
            InputMode.Voice -> state.voiceInputState.isValid
            InputMode.Scan -> true // TODO: Implement scan validation
        }
    }
}
