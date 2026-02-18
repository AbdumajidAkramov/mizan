package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import android.util.Log
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.model.Template
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.TemplateRepository
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class AddNewTransactionConfirmExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val manualInputHandler: ManualInputHandler,
    private val navigationHandler: NavigationHandler,
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val templateRepository: TemplateRepository
) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeAction(action: AddNewTransactionStore.Action) {
        when (action) {
            is AddNewTransactionStore.Action.CheckAndConfirm -> {
                checkPadState()
            }

            else -> Unit
        }
    }

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            is AddNewTransactionStore.Intent.UpdateNote -> {
                dispatch(AddNewTransactionStore.Message.UpdateNote(note = intent.note))
            }

            is AddNewTransactionStore.Intent.UpdateDate -> {
                dispatch(AddNewTransactionStore.Message.UpdateTransactionDate(date = intent.date))
            }

            is AddNewTransactionStore.Intent.UpdateSaveAsTemplate -> {
                dispatch(AddNewTransactionStore.Message.UpdateSaveAsTemplate(saveAsTemplate = intent.value))
            }

            is AddNewTransactionStore.Intent.Back -> {
                dispatch(AddNewTransactionStore.Message.UpdateIsConfirm(false))
            }

            is AddNewTransactionStore.Intent.ConfirmSave -> {
                scope.launch {
                    dispatch(AddNewTransactionStore.Message.UpdateLoading(true))
                    dispatch(AddNewTransactionStore.Message.UpdateError(null))

                    try {
                        val currentState = state()

                        // Get exchange rate for the selected currency
                        val currency =
                            currencyRepository.getCurrencyByCode(currentState.currency)
                        val exchangeRate = currency?.rateToBase ?: 1.0

                        // Get category ID (prefer child category if selected)
                        val categoryId = currentState.selectedSubCategory?.id
                            ?: currentState.selectedCategory?.id

                        // Create Transaction domain model
                        val transaction = Transaction(
                            id = 0, // New transaction
                            type = when (currentState.transactionType) {
                                dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType.EXPENSE ->
                                    Transaction.Type.EXPENSE

                                dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType.INCOME ->
                                    Transaction.Type.INCOME

                                dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType.TRANSFER ->
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
                            accountId = currentState.selectedAccount?.id,
                            categoryId = categoryId,
                            subCategoryId = null,
                            targetAccountId = currentState.targetAccount?.id,
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
                                val categoryName = currentState.selectedCategory?.name ?: "Template"
                                val template = Template(
                                    name = categoryName,
                                    amount = currentState.amount,
                                    iconName = currentState.selectedCategory?.iconName,
                                    transactionType = transaction.type,
                                    categoryId = categoryId,
                                    accountId = currentState.selectedAccount?.id,
                                    note = currentState.note.takeIf { it.isNotBlank() }
                                )
                                templateRepository.addTemplate(template)
                            }
                            publish(AddNewTransactionStore.Label.TransactionSaved)
                        } else {
                            dispatch(AddNewTransactionStore.Message.UpdateError("Failed to save transaction"))
                        }
                    } catch (e: Exception) {
                        dispatch(
                            AddNewTransactionStore.Message.UpdateError(
                                e.message ?: "Unknown error"
                            )
                        )
                    } finally {
                        dispatch(AddNewTransactionStore.Message.UpdateLoading(false))
                    }
                }
//                checkPadState()
            }

            else -> Unit
        }
    }

    private fun checkPadState() {
        val state = state()
        when {
            state.amount == 0.0 -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.AmountInput)
                )
            }

            state.selectedCategory == null -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.CategorySelector)
                )
            }

            state.selectedAccount == null -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.AccountSelector)
                )
            }

            state.transactionType == Transaction.Type.TRANSFER && state.targetAccount == null -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.TargetAccountSelector)
                )
            }

            else -> {
                Log.d("TTT", "Confirm screen open")
                dispatch(AddNewTransactionStore.Message.UpdateIsConfirm(true))
            }
        }
    }

}