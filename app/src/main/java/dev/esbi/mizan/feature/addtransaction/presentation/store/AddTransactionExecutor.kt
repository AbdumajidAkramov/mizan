package dev.esbi.mizan.feature.addtransaction.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.BackToPrev
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnCategorySelect
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnDateChange
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnInputModeChange
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnKeypadClick
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnKeypadNext
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnNextTransfer
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnNoteChange
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnSaveTransaction
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnTransactionTypeChange
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnTransactionTypeSelect
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateFlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateInputMode
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateLeftText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateRightText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionType
import dev.esbi.mizan.utils.DOT
import dev.esbi.mizan.utils.FRAC_LENGTH
import kotlinx.coroutines.CoroutineDispatcher

internal class AddTransactionExecutor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : CoroutineExecutor<
        AddTransactionStore.Intent,
        AddTransactionStore.Action,
        AddTransactionStore.State,
        AddTransactionStore.Message,
        AddTransactionStore.Label>(
    mainContext = mainDispatcher
) {
    override fun executeAction(action: AddTransactionStore.Action) {
        super.executeAction(action)
    }

    override fun executeIntent(intent: AddTransactionStore.Intent) {
        super.executeIntent(intent)
        when (intent) {
            is OnKeypadNext -> {
                onEqualClick(state())
                dispatch(UpdateFlowState(FlowState.Type))
            }

            is OnKeypadClick -> onKeypadClick(intent.key)
            is OnInputModeChange -> dispatch(UpdateInputMode(intent.inputMode))
            is OnTransactionTypeChange -> {
                dispatch(UpdateTransactionType(intent.type))
            }

            is OnTransactionTypeSelect -> {
                dispatch(UpdateTransactionType(intent.type))
                dispatch(UpdateFlowState(FlowState.Details))
            }

            is OnCategorySelect -> {
                dispatch(AddTransactionStore.Message.UpdateTransactionCategory(intent.category))
            }

            is OnNextTransfer -> with(state()) {
                if (fromAccountId != null && toAccountId != null) {
                    dispatch(UpdateFlowState(FlowState.Confirm))
                }
            }

            is OnDateChange -> {
                dispatch(AddTransactionStore.Message.UpdateTransactionDate(intent.date))
            }

            is OnNoteChange -> {
                dispatch(AddTransactionStore.Message.UpdateTransactionNotes(intent.notes))
            }

            is BackToPrev -> {
                val flowState = when (state().flowState) {
                    FlowState.Type -> FlowState.Amount
                    FlowState.Details -> FlowState.Type
                    FlowState.Confirm -> FlowState.Details
                    else -> FlowState.Amount
                }
                dispatch(UpdateFlowState(flowState))
            }

            is OnSaveTransaction -> {
                publish(AddTransactionStore.Label.Close)
            }

            else -> {}

        }
    }

    private fun calc(left: Double, right: Double, operator: String): Double {
        return when (operator) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> {
                if (right != 0.0) {
                    left / right
                } else {
                    0.0
                }
            }

            else -> 0.0
        }
    }

    private var isEqualed = false
    private fun onKeypadClick(key: Keypad) {
        when (key) {
            in Keypad.numbers -> with(state()) {
                if (key in listOf(Keypad.ZERO, Keypad.ZERO_ZERO, Keypad.ZERO_ZERO_ZERO)) {
                    if (isLeftNumberActive && leftNumber.isEmpty() || !isLeftNumberActive && rightNumber.isEmpty()) {
                        return
                    }
                }
                when {
                    isEqualed -> {
                        if (operator.isEmpty()) {
                            dispatch(UpdateLeftText(Keypad.number(key)))
                        } else {
                            dispatch(UpdateRightText(Keypad.number(key)))
                        }
                        isEqualed = false
                    }

                    isLeftNumberActive -> {
                        val separatorIndex = leftNumber.lastIndexOf(DOT)
                        if (separatorIndex != -1 && leftNumber.length - separatorIndex > FRAC_LENGTH) {
                            return
                        }
                        val newLeftNumber = leftNumber + Keypad.number(key)
                        dispatch(UpdateLeftText(newLeftNumber))
                    }

                    else -> {
                        val separatorIndex = rightNumber.lastIndexOf(DOT)
                        if (separatorIndex != -1 && rightNumber.length - separatorIndex > FRAC_LENGTH) {
                            return
                        }
                        val newRightNumber = rightNumber + Keypad.number(key)
                        dispatch(UpdateRightText(newRightNumber))
                    }
                }
            }

            in Keypad.operators -> with(state()) {
                if (operator.isNotEmpty()) {
                    val a = leftNumber.toDoubleOrNull() ?: 0.0
                    val b = rightNumber.toDoubleOrNull() ?: 0.0
                    val s = calc(a, b, operator)
                    dispatch(UpdateLeftText(s.toString()))
                }
                dispatch(UpdateRightText(""))
                dispatch(AddTransactionStore.Message.UpdateOperator(Keypad.operator(key)))
            }

            Keypad.DOT -> with(state()) {
                if (isLeftNumberActive) {
                    val newLeftNumber = if (leftNumber.isBlank()) "0." else "$leftNumber."
                    dispatch(UpdateLeftText(newLeftNumber))
                } else {
                    val newRightNumber = if (rightNumber.isBlank()) "0." else "$rightNumber."
                    dispatch(UpdateRightText(newRightNumber))
                }
            }

            Keypad.EQUALS -> onEqualClick(state())

            Keypad.CLEAR -> {
                dispatch(AddTransactionStore.Message.ClearText)
                isEqualed = false
            }

            Keypad.DEL -> with(state()) {
                if (isLeftNumberActive) {
                    if (leftNumber.isNotBlank()) {
                        val newLeftNumber = leftNumber.take(leftNumber.length - 1)
                        dispatch(UpdateLeftText(newLeftNumber))
                    }
                } else {
                    if (rightNumber.isNotBlank()) {
                        val newRightNumber = rightNumber.take(rightNumber.length - 1)
                        dispatch(UpdateRightText(newRightNumber))
                    }
                }
            }

            else -> Unit
        }
    }

    private fun onEqualClick(state: AddTransactionStore.State) = with(state) {
        if (operator.isNotEmpty()) {
            val a = leftNumber.toDoubleOrNull() ?: 0.0
            val b = rightNumber.toDoubleOrNull() ?: 0.0
            val s = formatGroupedNumber(calc(a, b, operator).toString())
            dispatch(UpdateLeftText(s))
        }
        dispatch(UpdateRightText(""))
        dispatch(AddTransactionStore.Message.UpdateOperator(""))
        isEqualed = true
    }

    fun formatGroupedNumber(input: String): String {
        val s = input.trim()
        if (s.isEmpty()) return s

        val parts = s.split('.', limit = 2)
        val intPartRaw = parts[0]
        val fracPartRaw = parts.getOrNull(1)

        val isNegative = intPartRaw.startsWith("-")
        val intDigits = if (isNegative) intPartRaw.drop(1) else intPartRaw


        // Group fraction part from the left (first 3, then the rest): 12342 -> 123 42
        val fracGrouped = fracPartRaw?.take(FRAC_LENGTH)

        val sign = if (isNegative) "-" else ""
        return if (fracGrouped != null && (fracPartRaw.toIntOrNull() ?: 0) > 0)
            "$sign$intDigits.$fracGrouped"
        else
            "$sign$intDigits"
    }
}
