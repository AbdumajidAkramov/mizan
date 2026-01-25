package dev.esbi.mizan.feature.newtransaction.amountinput.store.state

data class VoiceInputState(
    val isListening: Boolean = false,
    val voiceRecognitionResult: String = "",
    val voiceResult: String? = null,
    val voiceRecognitionError: String? = null,
    val amountText: String = "0",
) {
    val isValid: Boolean
        get() = amountText.isNotBlank() && 
                amountText.toDoubleOrNull() != null && 
                (amountText.toDoubleOrNull() ?: 0.0) > 0.0
}
