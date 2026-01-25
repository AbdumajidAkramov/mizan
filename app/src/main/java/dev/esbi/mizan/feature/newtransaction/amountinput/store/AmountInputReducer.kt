package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.core.store.Reducer

internal object AmountInputReducer : Reducer<AmountInputState, AmountInputStore.Message> {
    override fun AmountInputState.reduce(msg: AmountInputStore.Message): AmountInputState {
        return when (msg) {
            is AmountInputStore.Message.UpdateKeypadState -> copy(keypadState = msg.state)
            is AmountInputStore.Message.UpdateVoiceInputStateState -> copy(voiceInputState = msg.state)
            is AmountInputStore.Message.UpdateMode -> copy(inputMode = msg.mode)
        }
    }
}
