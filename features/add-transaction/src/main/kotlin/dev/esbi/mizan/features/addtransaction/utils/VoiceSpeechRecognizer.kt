package dev.esbi.mizan.features.addtransaction.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Voice recognition events
 */
sealed interface VoiceRecognitionEvent {
    data class OnReadyForSpeech(val params: Bundle) : VoiceRecognitionEvent
    data class OnBeginningOfSpeech(val params: Bundle) : VoiceRecognitionEvent
    data class OnResults(val results: ArrayList<String>?) : VoiceRecognitionEvent
    data class OnError(val error: VoiceRecognitionError) : VoiceRecognitionEvent
    data class OnEndOfSpeech(val params: Bundle) : VoiceRecognitionEvent
    data object OnStop : VoiceRecognitionEvent
}

/**
 * Voice recognition errors with user-friendly messages
 */
sealed interface VoiceRecognitionError {
    val message: String

    data class NetworkError(override val message: String = "Network error. Please check your connection.") :
        VoiceRecognitionError

    data class NetworkTimeoutError(override val message: String = "Network timeout. Please try again.") :
        VoiceRecognitionError

    data class NoMatch(override val message: String = "No speech detected. Please try again.") :
        VoiceRecognitionError

    data class InsufficientPermissions(override val message: String = "Microphone permission required.") :
        VoiceRecognitionError

    data class ClientError(override val message: String = "Voice recognition error. Please try again.") :
        VoiceRecognitionError

    data class ServerError(override val message: String = "Server error. Please try again later.") :
        VoiceRecognitionError

    data class UnknownError(override val message: String = "Unknown error occurred. Please try again.") :
        VoiceRecognitionError

    companion object {
        fun fromErrorCode(errorCode: Int): VoiceRecognitionError {
            return when (errorCode) {
                SpeechRecognizer.ERROR_NETWORK -> NetworkError()
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> NetworkTimeoutError()
                SpeechRecognizer.ERROR_NO_MATCH -> NoMatch()
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> InsufficientPermissions()
                SpeechRecognizer.ERROR_CLIENT -> ClientError()
                SpeechRecognizer.ERROR_SERVER -> ServerError()
                else -> UnknownError()
            }
        }
    }
}

/**
 * Wrapper class for Android SpeechRecognizer
 * Provides a clean interface for voice recognition with StateFlow events
 */
@Singleton
class VoiceSpeechRecognizer @Inject constructor(
    private val context: Context
) : RecognitionListener {

    private var speechRecognizer: SpeechRecognizer? = null

    private val _events = MutableStateFlow<VoiceRecognitionEvent?>(null)
    val events: StateFlow<VoiceRecognitionEvent?> = _events

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening

    init {
        initializeSpeechRecognizer()
    }

    private fun initializeSpeechRecognizer() {
        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(this)
        } catch (e: Exception) {
            _events.value = VoiceRecognitionEvent.OnError(
                VoiceRecognitionError.ClientError("Failed to initialize speech recognizer")
            )
        }
    }

    /**
     * Start voice recognition
     */
    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _events.value = VoiceRecognitionEvent.OnError(
                VoiceRecognitionError.ClientError("Speech recognition not available on this device")
            )
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            speechRecognizer?.startListening(intent)
            _isListening.value = true
        } catch (e: Exception) {
            _events.value = VoiceRecognitionEvent.OnError(
                VoiceRecognitionError.ClientError("Failed to start speech recognition")
            )
            _isListening.value = false
        }
    }

    /**
     * Stop voice recognition
     */
    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            _isListening.value = false
            _events.value = VoiceRecognitionEvent.OnStop
        } catch (e: Exception) {
            _events.value = VoiceRecognitionEvent.OnError(
                VoiceRecognitionError.ClientError("Failed to stop speech recognition")
            )
        }
    }

    /**
     * Destroy the speech recognizer
     */
    fun destroy() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (e: Exception) {
            // Ignore errors during cleanup
        }
    }

    // RecognitionListener implementation
    override fun onReadyForSpeech(params: Bundle?) {
        _events.value = VoiceRecognitionEvent.OnReadyForSpeech(params ?: Bundle())
    }

    override fun onBeginningOfSpeech() {
        _events.value = VoiceRecognitionEvent.OnBeginningOfSpeech(Bundle())
    }

    override fun onRmsChanged(rmsdB: Float) {
        // Not used for now, but could be used for visualization
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        // Not used for now
    }

    override fun onEndOfSpeech() {
        _events.value = VoiceRecognitionEvent.OnEndOfSpeech(Bundle())
        _isListening.value = false
    }

    override fun onError(error: Int) {
        _isListening.value = false
        _events.value = VoiceRecognitionEvent.OnError(VoiceRecognitionError.fromErrorCode(error))
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        _events.value = VoiceRecognitionEvent.OnResults(matches)
        _isListening.value = false
    }

    override fun onPartialResults(partialResults: Bundle?) {
        // Handle partial results if needed
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            // Could emit partial results for real-time feedback
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        // Not used for now
    }

    /**
     * Clear the current event
     */
    fun clearEvent() {
        _events.value = null
    }
}
