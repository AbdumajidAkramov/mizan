package dev.esbi.mizan.feature.newtransaction2.amountinput.voice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Manages voice recognition using Android's SpeechRecognizer
 * Provides state flows for different recognition states
 */
class VoiceRecognitionManager(private val context: Context) {
    
    private var speechRecognizer: SpeechRecognizer? = null
    
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()
    
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result.asStateFlow()
    
    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _isReady.value = true
            _error.value = null
        }
        
        override fun onBeginningOfSpeech() {
            _isListening.value = true
        }
        
        override fun onRmsChanged(rmsdB: Float) {
            // Can be used for visual feedback
        }
        
        override fun onBufferReceived(buffer: ByteArray?) {
            // Not used
        }
        
        override fun onEndOfSpeech() {
            _isListening.value = false
            _isReady.value = false
        }
        
        override fun onError(errorCode: Int) {
            _isListening.value = false
            _isReady.value = false
            _error.value = getErrorMessage(errorCode)
        }
        
        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                _result.value = matches.first()
            }
            _isListening.value = false
            _isReady.value = false
        }
        
        override fun onPartialResults(partialResults: Bundle?) {
            // Not used for now
        }
        
        override fun onEvent(eventType: Int, params: Bundle?) {
            // Not used
        }
    }
    
    fun initialize(): Boolean {
        return if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(recognitionListener)
            true
        } else {
            _error.value = "Speech recognition is not available on this device"
            false
        }
    }
    
    fun startListening(language: String = Locale.US.toString()) {
        if (!hasRecordAudioPermission()) {
            _error.value = "Record audio permission is required"
            return
        }
        
        speechRecognizer?.let { recognizer ->
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }
            
            try {
                recognizer.startListening(intent)
            } catch (e: Exception) {
                _error.value = "Failed to start listening: ${e.message}"
            }
        } ?: run {
            _error.value = "Speech recognizer not initialized"
        }
    }
    
    fun stopListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
        _isReady.value = false
    }
    
    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun clearResult() {
        _result.value = null
    }
    
    private fun hasRecordAudioPermission(): Boolean {
        return context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == 
               android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    
    private fun getErrorMessage(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer is busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error occurred"
        }
    }
}
