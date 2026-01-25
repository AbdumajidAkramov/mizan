package dev.esbi.mizan.feature.newtransaction.amountinput.store.state

data class VoiceInputState(
    val isListening: Boolean = false,
    val voiceRecognitionResult: String = "",
    val voiceRecognitionError: String? = null,
    val amountText: String = "0",
)
