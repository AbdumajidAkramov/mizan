package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import android.util.Log
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.Template
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.TemplateRepository
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Action
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.math.BigDecimal
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
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.CheckAndConfirm -> {
                checkPadState()
            }

            else -> Unit
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.UpdateNote -> {
                dispatch(Message.UpdateNote(note = intent.note))
            }

            is Intent.UpdateDate -> {
                dispatch(Message.UpdateTransactionDate(date = intent.date))
            }

            is Intent.UpdateSaveAsTemplate -> {
                dispatch(Message.UpdateSaveAsTemplate(saveAsTemplate = intent.value))
            }

            is Intent.Back -> {
                dispatch(Message.UpdateIsConfirm(false))
            }

            is Intent.ConfirmSave -> {
                scope.launch {
                    dispatch(Message.UpdateLoading(true))
                    dispatch(Message.UpdateError(null))

                    try {
                        val state = state()
                        when {
                            state.amount.value == BigDecimal.ZERO -> {
                                dispatch(Message.UpdateError("Amount not be zero"))
                            }

                            state.selectedAccount == null -> {
                                dispatch(Message.UpdateError("Select account"))
                            }

                            state.transactionType != Transaction.Type.TRANSFER && state.selectedCategory == null -> {
                                dispatch(Message.UpdateError("Select category"))
                            }

                            state.transactionType == Transaction.Type.TRANSFER && state.targetAccount == null -> {
                                dispatch(Message.UpdateError("Select Target account"))
                            }

                            else -> {
                                // Get exchange rate for the selected currency
                                val currency = currencyRepository.getCurrencyByCode(state.currency)
                                val exchangeRate = currency?.rateToBase ?: 1.0

                                // Get category ID (prefer child category if selected)
                                val categoryId = state.selectedSubCategory?.id
                                    ?: state.selectedCategory?.id

                                // Create Transaction domain model
                                val transaction = Transaction(
                                    id = 0, // New transaction
                                    type = state.transactionType,
                                    amount = state.amount.value.toDouble(),
                                    currency = currency ?: Currency(
                                        code = state.currency,
                                        name = state.currency,
                                        symbol = state.currency,
                                        rateToBase = exchangeRate,
                                        isBaseCurrency = state.currency == "UZS"
                                    ),
                                    exchangeRate = exchangeRate,
                                    targetAmount = null, // TODO: Calculate for transfers if needed
                                    date = state.transactionDate,
                                    note = state.note.takeIf { it.isNotBlank() },
                                    description = state.description.takeIf { it.isNotBlank() },
                                    photoPaths = state.photoPaths,
                                    accountId = state.selectedAccount?.id,
                                    categoryId = categoryId,
                                    subCategoryId = null,
                                    targetAccountId = state.targetAccount?.id,
                                    fee = state.fee ?: 0.0,
                                    isBookmarked = state.isBookmarked,
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
                                    if (state.saveAsTemplate) {
                                        val categoryName =
                                            state.selectedCategory?.name ?: "Template"
                                        val template = Template(
                                            name = categoryName,
                                            amount = state.amount.value.toDouble(),
                                            iconName = state.selectedCategory?.iconName,
                                            transactionType = transaction.type,
                                            categoryId = categoryId,
                                            accountId = state.selectedAccount?.id,
                                            note = state.note.takeIf { it.isNotBlank() }
                                        )
                                        templateRepository.addTemplate(template)
                                    }
                                    publish(Label.TransactionSaved)
                                } else {
                                    dispatch(Message.UpdateError("Failed to save transaction"))
                                }
                            }
                        }
                    } catch (e: Exception) {
                        dispatch(
                            Message.UpdateError(
                                e.message ?: "Unknown error"
                            )
                        )
                    } finally {
                        dispatch(Message.UpdateLoading(false))
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
            state.amount.value.toDouble() == 0.0 -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.AmountInput)
                )
            }

            state.selectedCategory == null -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.CategorySelector)
                )
            }

            state.selectedAccount == null -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.AccountSelector)
                )
            }

            state.transactionType == Transaction.Type.TRANSFER && state.targetAccount == null -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.TargetAccountSelector)
                )
            }

            else -> {
                Log.d("TTT", "Confirm screen open")
                dispatch(Message.UpdateIsConfirm(true))
            }
        }
    }

}