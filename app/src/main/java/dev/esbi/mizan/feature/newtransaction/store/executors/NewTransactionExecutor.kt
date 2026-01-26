package dev.esbi.mizan.feature.newtransaction.store.executors

import android.content.Context
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.newtransaction.TransactionStep
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.voice.TransactionVoiceParser
import dev.esbi.mizan.feature.newtransaction.amountinput.voice.VoiceRecognitionManager
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.CategoryChooserMessage.ParentCategorySelected
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

/**
 * Composite Executor that delegates specific logic to specialized handler classes.
 * Acts as the main entry point for all intent execution.
 */
internal class NewTransactionExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val context: Context,
    private val manualInputHandler: ManualInputHandler,
    private val navigationHandler: NavigationHandler,
    private val categoryRepository: CategoryRepository
) : CoroutineExecutor<
        NewTransactionStore.Intent,
        NewTransactionStore.Action,
        NewTransactionStore.State,
        NewTransactionStore.Message,
        NewTransactionStore.Label>(
    mainContext = mainDispatcher
) {

    private val voiceRecognitionManager = VoiceRecognitionManager(context)
    private val voiceParser = TransactionVoiceParser()
    private val pages: Stack<TransactionStep> = Stack()

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
                    dispatch(NewTransactionStore.Message.UpdateVoiceError(error))
                    dispatch(NewTransactionStore.Message.UpdateListeningState(false))
                }
            }
            .launchIn(scope)

        // Listen for listening state changes
        voiceRecognitionManager.isListening
            .onEach { isListening ->
                dispatch(NewTransactionStore.Message.UpdateListeningState(isListening))
            }
            .launchIn(scope)
    }

    private fun handleVoiceResult(text: String) {
        scope.launch {
            try {
                // Parse the voice input
                val parseResult = voiceParser.parse(text)

                // Update voice result
                dispatch(NewTransactionStore.Message.UpdateVoiceResult(text))

                // Update keypad state with parsed amount if available
                parseResult.amount?.let { amount ->
                    // Convert amount to keypad state
                    val amountStr = amount.toString()
                    // For simplicity, we'll update the leftNumber directly
                    // In a real implementation, you might want to build the full keypad state
                    val currentKeypadState = state().keypadState
                    dispatch(
                        NewTransactionStore.Message.UpdateKeypadState(
                            currentKeypadState.copy(leftNumber = amountStr)
                        )
                    )
                }

                // Turn off listening state
                dispatch(NewTransactionStore.Message.UpdateListeningState(false))

            } catch (e: Exception) {
                dispatch(NewTransactionStore.Message.UpdateVoiceError("Failed to parse voice input: ${e.message}"))
                dispatch(NewTransactionStore.Message.UpdateListeningState(false))
            }
        }
    }

    override fun executeAction(action: NewTransactionStore.Action) {
        super.executeAction(action)
        when (action) {
            is NewTransactionStore.Action.Init -> {
                pages.push(TransactionStep.AmountInput())
            }
        }
    }

    private fun updateCurrentPage() {
        if (pages.isNotEmpty()) {
            dispatch(NewTransactionStore.Message.UpdateTransactionStep(pages.peek()))
        }
    }


    override fun executeIntent(intent: NewTransactionStore.Intent) {
        when (intent) {
            is NewTransactionStore.Intent.Back -> {
                if (pages.size <= 1) {
                    publish(NewTransactionStore.Label.Back)
                } else {
                    pages.pop()
                    updateCurrentPage()
                }
            }

            is NewTransactionStore.Intent.OnTypeSelect -> {
                dispatch(NewTransactionStore.Message.UpdateTransactionType(type = intent.type))
                when (intent.type) {
                    Transaction.Type.EXPENSE,
                    Transaction.Type.INCOME -> {
                        pages.push(TransactionStep.CategoryChooser())
                        loadCategories(intent.type)
                    }

                    Transaction.Type.TRANSFER -> pages.push(TransactionStep.Transfer())
                }
                updateCurrentPage()
            }
            // Delegate Navigation logic
            is NewTransactionStore.Intent.OnModeChange -> {
                val message = navigationHandler.handleModeChange(intent.mode)
                dispatch(message)
            }

            is NewTransactionStore.Intent.OnSubmit -> {
                val label = navigationHandler.handleSubmit(state())
                label?.let { publish(it) }
            }

            is NewTransactionStore.Intent.TransactionTypesShow -> {
                pages.push(TransactionStep.TypeSelector())
                updateCurrentPage()
            }
            // Delegate Calculator logic
            is NewTransactionStore.AmountInputIntent -> {
                handleAmountInputIntent(intent)
            }

            is NewTransactionStore.VoiceRecognitionIntent -> {
                handleVoiceEntryIntent(intent)
            }

            is NewTransactionStore.CameraScanIntent -> {
                handleCameraScanIntent(intent)
            }

            is NewTransactionStore.CategoryChooserIntent -> {
                handleCategoryChooserIntent(intent)
            }
        }
    }

    private fun handleAmountInputIntent(intent: NewTransactionStore.AmountInputIntent) {
        when (intent) {
            is NewTransactionStore.AmountInputIntent.OnNumberClick -> {
                val newKeypadState = manualInputHandler.handleNumberClick(
                    intent.key,
                    state().keypadState
                )
                dispatch(NewTransactionStore.Message.UpdateKeypadState(newKeypadState))
            }

            is NewTransactionStore.AmountInputIntent.OnNextKeyButtonClick -> {

            }

        }
    }

    private fun handleVoiceEntryIntent(intent: NewTransactionStore.VoiceRecognitionIntent) {
        when (intent) {
            // Voice input handling - could be moved to a VoiceHandler in the future
            is NewTransactionStore.VoiceRecognitionIntent.OnStartVoiceRecognition -> {
                val newVoiceState = state().voiceInputState.copy(
                    isListening = true,
                    voiceRecognitionError = null
                )
                dispatch(NewTransactionStore.Message.UpdateVoiceInputStateState(newVoiceState))
            }

            is NewTransactionStore.VoiceRecognitionIntent.OnStopVoiceRecognition -> {
                val newVoiceState = state().voiceInputState.copy(
                    isListening = false
                )
                dispatch(NewTransactionStore.Message.UpdateVoiceInputStateState(newVoiceState))
            }

            is NewTransactionStore.VoiceRecognitionIntent.OnVoiceRecognitionError -> {
                val newVoiceState = state().voiceInputState.copy(
                    isListening = false,
                    voiceRecognitionError = intent.error
                )
                dispatch(NewTransactionStore.Message.UpdateVoiceInputStateState(newVoiceState))
            }

            // New Voice Recognition handling
            is NewTransactionStore.VoiceRecognitionIntent.OnStartListening -> {
                voiceRecognitionManager.clearError()
                voiceRecognitionManager.clearResult()
                voiceRecognitionManager.startListening()
            }

            is NewTransactionStore.VoiceRecognitionIntent.OnStopListening -> {
                voiceRecognitionManager.stopListening()
            }

            is NewTransactionStore.VoiceRecognitionIntent.OnVoiceResult -> {
                handleVoiceResult(intent.text)
            }

        }
    }

    private fun handleCameraScanIntent(intent: NewTransactionStore.CameraScanIntent) {
        when (intent) {
            // QR Code scanning logic
            is NewTransactionStore.CameraScanIntent.OnQrCodeScanned -> {
                // Debounce: If already not scanning, ignore
                if (!state().cameraInputState.isScanning) return

                // Dispatch QR code detection message
                dispatch(NewTransactionStore.Message.QrCodeDetected(intent.text))
            }

            is NewTransactionStore.CameraScanIntent.OnStartCameraScan -> {
                val newCameraState = state().cameraInputState.copy(
                    isScanning = true,
                    cameraScanError = null
                )
                dispatch(NewTransactionStore.Message.UpdateCameraInputState(newCameraState))
            }

            is NewTransactionStore.CameraScanIntent.OnStopCameraScan -> {
                val newCameraState = state().cameraInputState.copy(
                    isScanning = false
                )
                dispatch(NewTransactionStore.Message.UpdateCameraInputState(newCameraState))
            }

            is NewTransactionStore.CameraScanIntent.OnCameraScanError -> {
                val newCameraState = state().cameraInputState.copy(
                    isScanning = false,
                    cameraScanError = intent.error
                )
                dispatch(NewTransactionStore.Message.UpdateCameraInputState(newCameraState))
            }

            is NewTransactionStore.CameraScanIntent.OnAmountExtracted -> {}
            is NewTransactionStore.CameraScanIntent.OnReceiptScanResult -> {}
        }
    }

    private fun handleCategoryChooserIntent(intent: NewTransactionStore.CategoryChooserIntent) {
        when (intent) {
            is NewTransactionStore.CategoryChooserIntent.LoadCategories -> {
                loadCategories(state().transactionType)
            }

            is NewTransactionStore.CategoryChooserIntent.SelectParentCategory -> {
                selectParentCategory(intent.category)
            }

            is NewTransactionStore.CategoryChooserIntent.SelectSubCategory -> {
                selectSubCategory(intent.category)
            }

            is NewTransactionStore.CategoryChooserIntent.Continue -> {
                val selected = state().categoryChooserState.selectedCategory
                if (selected != null) {
                    pages.push(TransactionStep.ConfirmSave())
                    updateCurrentPage()
                }
            }

            is NewTransactionStore.CategoryChooserIntent.RetryLoad -> {
                loadCategories(state().transactionType)
            }

            is NewTransactionStore.CategoryChooserIntent.ManageCategories -> {
//                publish(Label.NavigateToManageCategories)
            }
        }
    }

    private fun loadCategories(transactionType: Transaction.Type) {
        scope.launch {
            dispatch(NewTransactionStore.CategoryChooserMessage.LoadingChanged(true))
            try {
                categoryRepository.getCategoriesByType(transactionType.name)
                    .collect { categories ->
                        dispatch(
                            NewTransactionStore.CategoryChooserMessage.CategoriesLoaded(
                                categories
                            )
                        )
                    }
            } catch (e: Exception) {
                dispatch(NewTransactionStore.CategoryChooserMessage.ErrorChanged("Failed to load categories: ${e.message}"))
            }
        }
    }

    private fun selectParentCategory(category: Category) {
        if (state().categoryChooserState.selectedParentId == category.id) {
            dispatch(ParentCategorySelected(null))
            return
        }
        dispatch(ParentCategorySelected(category))
    }

    private fun selectSubCategory(category: Category) {
        dispatch(NewTransactionStore.CategoryChooserMessage.SubCategorySelected(category))
    }
}
