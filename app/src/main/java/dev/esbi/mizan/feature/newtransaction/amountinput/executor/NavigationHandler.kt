package dev.esbi.mizan.feature.newtransaction.amountinput.executor

import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.store.AmountInputState
import javax.inject.Inject

/**
 * Handles navigation logic and validation for the Amount Input screen.
 * Manages state transitions and publishes navigation labels.
 */
internal class NavigationHandler @Inject constructor() {
    
    /**
     * Handles mode change logic
     */
    fun handleModeChange(mode: InputMode): NewTransactionStore.Message {
        return NewTransactionStore.Message.UpdateMode(mode)
    }
    
    /**
     * Handles submit logic with validation
     * Returns a label if navigation should occur, null otherwise
     */
    fun handleSubmit(state: AmountInputState): NewTransactionStore.Label? {
        return when (state.inputMode) {
            InputMode.Manual -> {
                if (state.keypadState.canSubmit) {
                    NewTransactionStore.Label.MapsToNextStep
                } else {
                    null
                }
            }
            InputMode.Voice -> {
                if (state.voiceInputState.isValid) {
                    NewTransactionStore.Label.MapsToNextStep
                } else {
                    null
                }
            }
            InputMode.Scan -> {
                // TODO: Implement scan validation logic
                NewTransactionStore.Label.MapsToNextStep
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
