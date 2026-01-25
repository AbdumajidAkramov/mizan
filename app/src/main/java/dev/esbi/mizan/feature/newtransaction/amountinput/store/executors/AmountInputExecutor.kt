package dev.esbi.mizan.feature.newtransaction.amountinput.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Composite Executor that delegates specific logic to specialized handler classes.
 * Acts as the main entry point for all intent execution.
 */
internal class AmountInputExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
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

    override fun executeAction(action: AmountInputStore.Action) {
        super.executeAction(action)
    }

    override fun executeIntent(intent: AmountInputStore.Intent) {
        when (intent) {
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
            else -> Unit
        }
    }
}
