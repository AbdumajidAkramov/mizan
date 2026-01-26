package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.core.store.Reducer

internal object AmountInputReducer : Reducer<AmountInputState, AmountInputStore.Message> {
    override fun AmountInputState.reduce(msg: AmountInputStore.Message): AmountInputState {
        return when (msg) {
            is AmountInputStore.Message.UpdateTransactionType -> copy(transactionType = msg.type)
            is AmountInputStore.Message.UpdateKeypadState -> copy(keypadState = msg.state)
            is AmountInputStore.Message.UpdateVoiceInputStateState -> copy(voiceInputState = msg.state)
            is AmountInputStore.Message.UpdateMode -> copy(inputMode = msg.mode)
            is AmountInputStore.Message.UpdateCameraInputState -> copy(cameraInputState = msg.state)
            is AmountInputStore.Message.QrCodeDetected -> copy(
                cameraInputState = cameraInputState.copy(
                    qrtext = msg.text,
                    isScanning = false
                )
            )
            is AmountInputStore.Message.UpdateListeningState -> copy(
                voiceInputState = voiceInputState.copy(isListening = msg.isListening)
            )
            is AmountInputStore.Message.UpdateVoiceError -> copy(
                voiceInputState = voiceInputState.copy(voiceRecognitionError = msg.error)
            )
            is AmountInputStore.Message.UpdateVoiceResult -> copy(
                voiceInputState = voiceInputState.copy(voiceResult = msg.result)
            )
        }
    }
}
