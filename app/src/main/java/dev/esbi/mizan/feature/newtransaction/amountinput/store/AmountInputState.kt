package dev.esbi.mizan.feature.newtransaction.amountinput.store

import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.VoiceInputState

/**
 * State for the Amount Input screen
 */
data class AmountInputState(
    val inputMode: InputMode = InputMode.Manual,
    val keypadState: KeypadState = KeypadState(),
    val voiceInputState: VoiceInputState = VoiceInputState()
) {
    companion object
}
