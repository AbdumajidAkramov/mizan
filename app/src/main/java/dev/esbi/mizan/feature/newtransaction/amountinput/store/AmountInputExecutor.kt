package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.utils.DOT
import dev.esbi.mizan.utils.FRAC_LENGTH
import kotlinx.coroutines.CoroutineDispatcher

internal class AmountInputExecutor(
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
            in Keypad.numbers -> with(state) {
                if (key in listOf(Keypad.ZERO, Keypad.ZERO_ZERO, Keypad.ZERO_ZERO_ZERO)) {
                    if (isLeftNumberActive && leftNumber.isEmpty() || !isLeftNumberActive && rightNumber.isEmpty()) {
                        return
                    }
                }
                when {
                    isEqualed -> {
                        isEqualed = false
                        if (operator.isEmpty()) {
                            state.copy(leftNumber = Keypad.number(key))
                        } else {
                            state.copy(rightNumber = Keypad.number(key))
                        }
                    }

                    isLeftNumberActive -> {
                        val separatorIndex = leftNumber.lastIndexOf(DOT)
                        if (separatorIndex != -1 && leftNumber.length - separatorIndex > FRAC_LENGTH) {
                            return
                        }
                        val newLeftNumber = leftNumber + Keypad.number(key)
                        state.copy(leftNumber = newLeftNumber)
                    }

                    else -> {
                        val separatorIndex = rightNumber.lastIndexOf(DOT)
                        if (separatorIndex != -1 && rightNumber.length - separatorIndex > FRAC_LENGTH) {
                            return
                        }
                        val newRightNumber = rightNumber + Keypad.number(key)
                        state.copy(rightNumber = rightNumber)
                    }
                }
            }

            in Keypad.operators -> with(state) {
                val a = leftNumber.toDoubleOrNull() ?: 0.0
                val b = rightNumber.toDoubleOrNull() ?: 0.0
                val s = calc(a, b, operator)
                state.copy(
                    operator = Keypad.operator(key),
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
