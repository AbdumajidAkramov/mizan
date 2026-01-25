package dev.esbi.mizan.feature.newtransaction.amountinput.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.utils.AMOUNT_MAX
import dev.esbi.mizan.utils.DOT
import dev.esbi.mizan.utils.FRAC_LENGTH
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class ManualInputExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : CoroutineExecutor<
        AmountInputStore.Intent,
        AmountInputStore.Action,
        AmountInputState,
        AmountInputStore.Message,
        AmountInputStore.Label>(
    mainContext = mainDispatcher
) {

    override fun executeAction(action: AmountInputStore.Action) {
        super.executeAction(action)
    }

    override fun executeIntent(intent: AmountInputStore.Intent) {
        super.executeIntent(intent)
        when (intent) {
            is AmountInputStore.Intent.OnNumberClick -> handleNumberClick(
                intent.key,
                state().keypadState
            )

            is AmountInputStore.Intent.OnModeChange -> {
                dispatch(AmountInputStore.Message.UpdateMode(intent.mode))
            }

            is AmountInputStore.Intent.OnSubmit -> {}
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
    private fun handleNumberClick(key: Keypad, state: KeypadState) {
        val isLeftNumberActive = state.operator.isBlank()
        val newState: KeypadState = when (key) {
            in Keypad.Companion.numbers -> with(state) {
                if (key in listOf(Keypad.ZERO, Keypad.ZERO_ZERO, Keypad.ZERO_ZERO_ZERO)) {
                    if (isLeftNumberActive && leftNumber.isEmpty() || !isLeftNumberActive && rightNumber.isEmpty()) {
                        return
                    }
                }
                when {
                    isEqualed -> {
                        isEqualed = false
                        if (operator.isEmpty()) {
                            state.copy(leftNumber = Keypad.Companion.number(key))
                        } else {
                            state.copy(rightNumber = Keypad.Companion.number(key))
                        }
                    }

                    isLeftNumberActive -> {
                        if ((leftNumber.toDoubleOrNull() ?: 0.0) > AMOUNT_MAX) return
                        val separatorIndex = leftNumber.lastIndexOf(DOT)
                        if (separatorIndex != -1 && leftNumber.length - separatorIndex > FRAC_LENGTH) return
                        val newLeftNumber = leftNumber + Keypad.Companion.number(key)
                        state.copy(leftNumber = newLeftNumber)
                    }

                    else -> {
                        if ((rightNumber.toDoubleOrNull() ?: 0.0) > AMOUNT_MAX) return
                        val separatorIndex = rightNumber.lastIndexOf(DOT)
                        if (separatorIndex != -1 && rightNumber.length - separatorIndex > FRAC_LENGTH) {
                            return
                        }
                        val newRightNumber = rightNumber + Keypad.Companion.number(key)
                        state.copy(rightNumber = newRightNumber)
                    }
                }
            }

            in Keypad.Companion.operators -> with(state) {
                val op = Keypad.Companion.operator(key)
                val a = leftNumber.toDoubleOrNull() ?: 0.0
                val b = rightNumber.toDoubleOrNull() ?: 0.0
                val s = calc(a, b, op)
                state.copy(
                    operator = op,
                    leftNumber = s.toString(),
                    rightNumber = ""
                )
            }

            Keypad.DOT -> with(state) {
                if (isLeftNumberActive) {
                    val newLeftNumber = if (leftNumber.isBlank()) "0." else "$leftNumber."
                    state.copy(leftNumber = newLeftNumber)
                } else {
                    val newRightNumber = if (rightNumber.isBlank()) "0." else "$rightNumber."
                    state.copy(rightNumber = newRightNumber)
                }
            }

            Keypad.EQUALS -> {
                val s = if (state.operator.isNotBlank()) {
                    calc(
                        left = state.leftNumber.toDoubleOrNull() ?: 0.0,
                        right = state.rightNumber.toDoubleOrNull() ?: 0.0,
                        operator = state.operator
                    )
                } else 0.0
                isEqualed = true
                state.copy(
                    leftNumber = s.toString(),
                    rightNumber = "",
                    operator = "",
                )
            }

            Keypad.CLEAR -> {
                isEqualed = false
                KeypadState()
            }

            Keypad.DEL -> with(state) {
                if (isLeftNumberActive) {
                    if (leftNumber.isNotBlank()) {
                        val newLeftNumber = leftNumber.take(leftNumber.length - 1)
                        state.copy(leftNumber = newLeftNumber)
                    } else {
                        state
                    }
                } else {
                    if (rightNumber.isNotBlank()) {
                        val newRightNumber = rightNumber.take(rightNumber.length - 1)
                        state.copy(rightNumber = newRightNumber)
                    } else {
                        state
                    }
                }
            }

            else -> state
        }

        dispatch(AmountInputStore.Message.UpdateKeypadState(state = newState))
    }
}