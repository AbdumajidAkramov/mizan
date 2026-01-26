package dev.esbi.mizan.feature.newtransaction.amountinput.store.executors

import android.content.Context
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.feature.newtransaction.amountinput.voice.TransactionVoiceParser
import dev.esbi.mizan.feature.newtransaction.amountinput.voice.VoiceRecognitionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Composite Executor that delegates specific logic to specialized handler classes.
 * Acts as the main entry point for all intent execution.
 */
internal class AmountInputExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val context: Context,
    private val manualInputHandler: ManualInputHandler,
    private val navigationHandler: NavigationHandler
) : CoroutineExecutor<
        AmountInputStore.Intent,
        AmountInputStore.Action,
        AmountInputStore.State,
        AmountInputStore.Message,
        AmountInputStore.Label>(
    mainContext = mainDispatcher
) {
    
    private val voiceRecognitionManager = VoiceRecognitionManager(context)
    private val voiceParser = TransactionVoiceParser()
    
    init {
        voiceRecognitionManager.initialize()
        setupVoiceRecognitionFlow()
    }
    
    private fun setupVoiceRecognitionFlow() {
        // Listen for voice recognition results
        voiceRecognitionManager.result
            .onEach { result ->
                if (result != null) {
                    handleVoiceResult(result)
                }
            }
            .launchIn(scope)
            
        // Listen for voice recognition errors
        voiceRecognitionManager.error
            .onEach { error ->
                if (error != null) {
                    dispatch(AmountInputStore.Message.UpdateVoiceError(error))
                    dispatch(AmountInputStore.Message.UpdateListeningState(false))
                }
            }
            .launchIn(scope)
            
        // Listen for listening state changes
        voiceRecognitionManager.isListening
            .onEach { isListening ->
                dispatch(AmountInputStore.Message.UpdateListeningState(isListening))
            }
            .launchIn(scope)
    }
    
    private fun handleVoiceResult(text: String) {
        scope.launch {
            try {
                // Parse the voice input
                val parseResult = voiceParser.parse(text)
                
                // Update voice result
                dispatch(AmountInputStore.Message.UpdateVoiceResult(text))
                
                // Update keypad state with parsed amount if available
                parseResult.amount?.let { amount ->
                    // Convert amount to keypad state
                    val amountStr = amount.toString()
                    // For simplicity, we'll update the leftNumber directly
                    // In a real implementation, you might want to build the full keypad state
                    val currentKeypadState = state().keypadState
                    dispatch(AmountInputStore.Message.UpdateKeypadState(
                        currentKeypadState.copy(leftNumber = amountStr)
                    ))
                }
                
                // Turn off listening state
                dispatch(AmountInputStore.Message.UpdateListeningState(false))
                
            } catch (e: Exception) {
                dispatch(AmountInputStore.Message.UpdateVoiceError("Failed to parse voice input: ${e.message}"))
                dispatch(AmountInputStore.Message.UpdateListeningState(false))
            }
        }
    }

    override fun executeAction(action: AmountInputStore.Action) {
        super.executeAction(action)
    }

    override fun executeIntent(intent: AmountInputStore.Intent) {
        when (intent) {
            is AmountInputStore.Intent.NavigateBack -> {
                publish(AmountInputStore.Label.Back)
            }
            // Delegate Calculator logic
            is AmountInputStore.Intent.OnNumberClick -> {
                val newKeypadState = manualInputHandler.handleNumberClick(
                    intent.key,
                    state().keypadState
                )
                dispatch(AmountInputStore.Message.UpdateKeypadState(newKeypadState))
            }

            // Delegate Navigation logic
            is AmountInputStore.Intent.OnModeChange -> {
                val message = navigationHandler.handleModeChange(intent.mode)
                dispatch(message)
            }

            is AmountInputStore.Intent.OnSubmit -> {
                val label = navigationHandler.handleSubmit(state())
                label?.let { publish(it) }
            }

            // Voice input handling - could be moved to a VoiceHandler in the future
            is AmountInputStore.Intent.OnStartVoiceRecognition -> {
                val newVoiceState = state().voiceInputState.copy(
                    isListening = true,
                    voiceRecognitionError = null
                )
                dispatch(AmountInputStore.Message.UpdateVoiceInputStateState(newVoiceState))
            }

            is AmountInputStore.Intent.OnStopVoiceRecognition -> {
                val newVoiceState = state().voiceInputState.copy(
                    isListening = false
                )
                dispatch(AmountInputStore.Message.UpdateVoiceInputStateState(newVoiceState))
            }

            is AmountInputStore.Intent.OnVoiceRecognitionError -> {
                val newVoiceState = state().voiceInputState.copy(
                    isListening = false,
                    voiceRecognitionError = intent.error
                )
                dispatch(AmountInputStore.Message.UpdateVoiceInputStateState(newVoiceState))
            }

            // New Voice Recognition handling
            is AmountInputStore.Intent.OnStartListening -> {
                voiceRecognitionManager.clearError()
                voiceRecognitionManager.clearResult()
                voiceRecognitionManager.startListening()
            }

            is AmountInputStore.Intent.OnStopListening -> {
                voiceRecognitionManager.stopListening()
            }

            is AmountInputStore.Intent.OnVoiceResult -> {
                handleVoiceResult(intent.text)
            }

            // QR Code scanning logic
            is AmountInputStore.Intent.OnQrCodeScanned -> {
                // Debounce: If already not scanning, ignore
                if (!state().cameraInputState.isScanning) return
                
                // Dispatch QR code detection message
                dispatch(AmountInputStore.Message.QrCodeDetected(intent.text))
            }

            is AmountInputStore.Intent.OnStartCameraScan -> {
                val newCameraState = state().cameraInputState.copy(
                    isScanning = true,
                    cameraScanError = null
                )
                dispatch(AmountInputStore.Message.UpdateCameraInputState(newCameraState))
            }

            is AmountInputStore.Intent.OnStopCameraScan -> {
                val newCameraState = state().cameraInputState.copy(
                    isScanning = false
                )
                dispatch(AmountInputStore.Message.UpdateCameraInputState(newCameraState))
            }

            is AmountInputStore.Intent.OnCameraScanError -> {
                val newCameraState = state().cameraInputState.copy(
                    isScanning = false,
                    cameraScanError = intent.error
                )
                dispatch(AmountInputStore.Message.UpdateCameraInputState(newCameraState))
            }

            else -> Unit
        }
    }
}
