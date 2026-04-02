package dev.esbi.mizan.feature.newtransaction.store.executors

import android.content.Context
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Template
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.newtransaction.TransactionStep
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.voice.TransactionVoiceParser
import dev.esbi.mizan.feature.newtransaction.amountinput.voice.VoiceRecognitionManager
import dev.esbi.mizan.feature.newtransaction.input.TransactionInputState
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.CategoryChooserMessage.ParentCategorySelected
import dev.esbi.mizan.feature.newtransaction.store.state.KeypadState
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.presentation.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.presentation.feature.addtransaction.domain.repository.TemplateRepository
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.TransactionType
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
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val templateRepository: TemplateRepository
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
        loadAccounts()
    }

    private fun loadAccounts() {
        accountRepository.observeAccounts()
            .onEach { accounts ->
                dispatch(NewTransactionStore.Message.UpdateAccounts(accounts))

                // Auto-select first account if none selected
                if (state().selectedAccountId == null && accounts.isNotEmpty()) {
                    val defaultAccount = accounts.first()
                    dispatch(NewTransactionStore.Message.UpdateSelectedAccount(defaultAccount.id))
                }
            }
            .launchIn(scope)
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
//                    val currentKeypadState = state().keypadState
                    /*
                                        dispatch(
                                            NewTransactionStore.Message.UpdateKeypadState(
                                                currentKeypadState.copy(leftNumber = amountStr)
                                            )
                                        )
                    */
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
                scope.launch {
                    categoryRepository.getAllCategories()
                        .collect {
                            println(it)
                            dispatch(NewTransactionStore.CategoryChooserMessage.CategoriesLoaded(it))
                        }
                }
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

            is NewTransactionStore.Intent.ConfirmSave -> {
                scope.launch {
                    dispatch(NewTransactionStore.Message.SetLoading(true))
                    dispatch(NewTransactionStore.Message.SetError(null))

                    try {
                        val currentState = state()

                        // Get exchange rate for the selected currency
                        val currency =
                            currencyRepository.getCurrencyByCode(currentState.currency)
                        val exchangeRate = currency?.rateToBase ?: 1.0

                        // Get category ID (prefer child category if selected)
                        val categoryId = currentState.categoryChooserState.selectedChildId
                            ?: currentState.categoryChooserState.selectedParentId

                        // Create Transaction domain model
                        val transaction = Transaction(
                            id = 0, // New transaction
                            type = when (currentState.transactionType) {
                                TransactionType.EXPENSE ->
                                    Transaction.Type.EXPENSE

                                TransactionType.INCOME ->
                                    Transaction.Type.INCOME

                                TransactionType.TRANSFER ->
                                    Transaction.Type.TRANSFER
                            },
                            amount = currentState.amount,
                            currency = currency ?: dev.esbi.mizan.domain.model.Currency(
                                code = currentState.currency,
                                name = currentState.currency,
                                symbol = currentState.currency,
                                rateToBase = exchangeRate,
                                isBaseCurrency = currentState.currency == "UZS"
                            ),
                            exchangeRate = exchangeRate,
                            targetAmount = null, // TODO: Calculate for transfers if needed
                            date = currentState.transactionDate,
                            note = currentState.note.takeIf { it.isNotBlank() },
                            description = null,
                            photoPaths = emptyList(),
                            accountId = currentState.selectedAccountId,
                            categoryId = categoryId,
                            subCategoryId = null,
                            targetAccountId = currentState.targetAccountId,
                            fee = 0.0,
                            isBookmarked = false,
                            recurrenceRule = null,
                            isInstallment = false,
                            installmentTotalMonths = null,
                            installmentCurrentMonth = null,
                            parentTransactionId = null,
                            merchantName = null,
                            fiscalSign = null
                        )

                        // Save transaction
                        val result = transactionRepository.saveTransaction(transaction)

                        if (result.isSuccess) {
                            // Save as template if enabled
                            if (currentState.saveAsTemplate) {
                                val categoryName =
                                    currentState.categoryChooserState.selectedCategory?.name
                                        ?: "Template"
                                val template = Template(
                                    name = categoryName,
                                    amount = currentState.amount,
                                    iconName = currentState.categoryChooserState.selectedCategory?.iconName,
                                    transactionType = transaction.type,
                                    categoryId = categoryId,
                                    accountId = currentState.selectedAccountId,
                                    note = currentState.note.takeIf { it.isNotBlank() }
                                )
                                templateRepository.addTemplate(template)
                            }
                            publish(NewTransactionStore.Label.TransactionSaved)
                        } else {
                            dispatch(NewTransactionStore.Message.SetError("Failed to save transaction"))
                        }
                    } catch (e: Exception) {
                        dispatch(NewTransactionStore.Message.SetError(e.message ?: "Unknown error"))
                    } finally {
                        dispatch(NewTransactionStore.Message.SetLoading(false))
                    }
                }
            }

            is NewTransactionStore.Intent.UpdateNote -> {
                dispatch(NewTransactionStore.Message.UpdateNote(intent.note))
            }

            is NewTransactionStore.Intent.UpdateDate -> {
                dispatch(NewTransactionStore.Message.UpdateDate(intent.date))
            }

            is NewTransactionStore.Intent.UpdateSelectedAccount -> {
                dispatch(NewTransactionStore.Message.UpdateSelectedAccount(intent.accountId))
            }

            is NewTransactionStore.Intent.UpdateTargetAccount -> {
                dispatch(NewTransactionStore.Message.UpdateTargetAccount(intent.accountId))
            }

            is NewTransactionStore.Intent.OpenAccountSelection -> {
                dispatch(NewTransactionStore.Message.SetAccountSheetVisible(true))
            }

            is NewTransactionStore.Intent.OpenTargetAccountSelection -> {
                // For now, use same sheet - in future, could differentiate target selection
                dispatch(NewTransactionStore.Message.SetAccountSheetVisible(true))
            }

            is NewTransactionStore.Intent.OpenAccountManageScreen -> {
                publish(NewTransactionStore.Label.NavigateToAccountManage)
            }

            is NewTransactionStore.Intent.CloseAccountSelection -> {
                dispatch(NewTransactionStore.Message.SetAccountSheetVisible(false))
            }

            is NewTransactionStore.Intent.SelectAccount -> {
                dispatch(NewTransactionStore.Message.UpdateSelectedAccount(intent.accountId))
                dispatch(NewTransactionStore.Message.SetAccountSheetVisible(false))
            }

            is NewTransactionStore.Intent.UpdateSaveAsTemplate -> {
                dispatch(NewTransactionStore.Message.SetSaveAsTemplate(intent.saveAsTemplate))
            }

            is NewTransactionStore.Intent.ShowAmountInputPad -> {
                dispatch(
                    NewTransactionStore.Message.UpdateTransactionInputState(
                        state = TransactionInputState.TransactionAmountInput()
                    )
                )
            }

            is NewTransactionStore.Intent.ShowSelectAccountSelector -> {
                dispatch(
                    NewTransactionStore.Message.UpdateSelectedAccountActive(isActive = true)
                )
                dispatch(
                    NewTransactionStore.Message.UpdateTransactionInputState(
                        state = TransactionInputState.TransactionAccountSelector()
                    )
                )
            }

            is NewTransactionStore.Intent.ShowTargetAccountSelector -> {
                dispatch(
                    NewTransactionStore.Message.UpdateSelectedAccountActive(isActive = false)
                )

                dispatch(
                    NewTransactionStore.Message.UpdateTransactionInputState(
                        state = TransactionInputState.TransactionAccountSelector()
                    )
                )
            }

            is NewTransactionStore.Intent.ShowCategorySelector -> {
                dispatch(
                    NewTransactionStore.Message.UpdateTransactionInputState(
                        state = TransactionInputState.TransactionCategorySelector()
                    )
                )
            }

            is NewTransactionStore.Intent.ShowTypeSelector -> {
//                dispatch(NewTransactionStore.Message.SetTypeSelectorVisible(true))
                dispatch(
                    NewTransactionStore.Message.UpdateTransactionInputState(
                        state = TransactionInputState.TransactionTypeSelector()
                    )
                )
            }

            is NewTransactionStore.Intent.HideTypeSelector -> {
                dispatch(NewTransactionStore.Message.SetTypeSelectorVisible(false))
            }

            is NewTransactionStore.Intent.SelectTransactionType -> {
                dispatch(NewTransactionStore.Message.UpdateTransactionType(intent.type))
                dispatch(NewTransactionStore.Message.SetTypeSelectorVisible(false))
                next()
            }

            is NewTransactionStore.Intent.OpenCategorySheet -> {
                dispatch(NewTransactionStore.Message.SetCategorySheetVisible(true))
            }

            is NewTransactionStore.Intent.CloseCategorySheet -> {
                dispatch(NewTransactionStore.Message.SetCategorySheetVisible(false))
            }

            is NewTransactionStore.Intent.SelectParentCategory -> {
                // Update category chooser state
                dispatch(ParentCategorySelected(category = intent.category))
                dispatch(NewTransactionStore.Message.SetCategorySheetVisible(false))
            }

            is NewTransactionStore.Intent.SelectChildCategory -> {
                // Update category chooser state with child
                dispatch(
                    NewTransactionStore.CategoryChooserMessage.SubCategorySelected(
                        category = intent.category
                    )
                )
                dispatch(NewTransactionStore.Message.SetCategorySheetVisible(false))
            }

            is NewTransactionStore.Intent.SmartNext -> {
                handleSmartNext()
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

            is NewTransactionStore.Intent.HidePart2 -> {
                dispatch(
                    NewTransactionStore.Message.UpdateTransactionInputState(
                        TransactionInputState.TransactionEmpty
                    )
                )
            }

            is NewTransactionStore.Intent.OpenCategoryManageScreen -> {
                publish(NewTransactionStore.Label.NavigateToManageCategories)
            }

            is NewTransactionStore.Intent.OnCategorySelect -> {
                if (intent.category.parentId == null) {
                    selectParentCategory(intent.category)
                } else {
                    selectSubCategory(intent.category)
                }
            }

            is NewTransactionStore.Intent.NavigateToAccountSelector -> {
                publish(NewTransactionStore.Label.NavigateToAccountSelector)
            }

            is NewTransactionStore.Intent.NavigateToCategorySelector -> {
                publish(NewTransactionStore.Label.NavigateToCategorySelector)
            }

            is NewTransactionStore.Intent.OnAccountSelected -> {
                dispatch(NewTransactionStore.Message.AccountUpdated(intent.account))
            }

            is NewTransactionStore.Intent.OnCategorySelected -> {
                dispatch(NewTransactionStore.Message.CategoryUpdated(intent.category))
            }
        }
    }

    private fun next() {
        with(state()) {
            when {
                amount == 0.0 -> {
                    dispatch(
                        NewTransactionStore.Message.UpdateTransactionInputState(
                            TransactionInputState.TransactionAmountInput()
                        )
                    )
                }

                categoryChooserState.selectedCategory == null -> {
                    dispatch(
                        NewTransactionStore.Message.UpdateTransactionInputState(
                            TransactionInputState.TransactionCategorySelector()
                        )
                    )
                }

                selectedAccountId == null || targetAccountId == null -> {
                    dispatch(
                        NewTransactionStore.Message.UpdateTransactionInputState(
                            TransactionInputState.TransactionAccountSelector()
                        )
                    )
                }

                else -> {
                    pages.push(TransactionStep.ConfirmSave())
                    updateCurrentPage()
                }
            }
        }
    }

    private fun handleAmountInputIntent(intent: NewTransactionStore.AmountInputIntent) {
        when (intent) {
            is NewTransactionStore.AmountInputIntent.OnNumberClick -> {
                with(state()) {
                    val newKeypadState = manualInputHandler.handleNumberClick(
                        intent.key,
                        KeypadState(
                            operator = operator,
                            leftNumber = leftNumber.toBigDecimalOrNull()
                                ?: java.math.BigDecimal.ZERO,
                            rightNumber = rightNumber.toBigDecimalOrNull()
                                ?: java.math.BigDecimal.ZERO,
                            currency = currency,
                        )
                    )
                    dispatch(NewTransactionStore.Message.UpdateKeypadState(newKeypadState))
                    if (intent.key == Keypad.EQUAL) {
                        dispatch(
                            NewTransactionStore.Message.UpdateTransactionInputState(
                                TransactionInputState.TransactionEmpty
                            )
                        )
                        next()
                    }

                }
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

    /**
     * Smart validation chain for the "Next" button.
     * Checks all required fields in order and opens the appropriate selector
     * if something is missing, or navigates to Confirm if all fields are valid.
     *
     * Validation Order:
     * 1. Amount > 0
     * 2. Category selected
     * 3. Account selected
     * 4. All valid -> Navigate to Confirm
     */
    private fun handleSmartNext() {
        val currentState = state()

        // Step 1: Check if amount is valid (> 0)
        if (currentState.amount <= 0.0) {
            // Show error - amount is required
            publish(NewTransactionStore.Label.ShowError("Please enter an amount"))
            return
        }

        // Step 2: Check if category is selected
        if (currentState.categoryChooserState.selectedCategory == null) {
            // Open category sheet
            dispatch(NewTransactionStore.Message.SetCategorySheetVisible(true))
            return
        }

        // Step 3: Check if account is selected
        if (currentState.selectedAccountId == null) {
            // Open account sheet
            dispatch(NewTransactionStore.Message.SetAccountSheetVisible(true))
            return
        }

        // Step 4: All checks passed - navigate to Confirm
        pages.push(TransactionStep.ConfirmSave())
        updateCurrentPage()
    }
}
