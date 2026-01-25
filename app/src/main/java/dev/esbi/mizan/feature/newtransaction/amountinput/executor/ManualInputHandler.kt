package dev.esbi.mizan.feature.newtransaction.amountinput.executor

import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.utils.AMOUNT_MAX
import dev.esbi.mizan.utils.DOT
import dev.esbi.mizan.utils.FRAC_LENGTH
import javax.inject.Inject

/**
 * Handles calculator logic for keypad inputs.
 * Pure math and string manipulation logic without side effects.
 */
internal class ManualInputHandler @Inject constructor() {
    
    private var isEqualed = false
    
    /**
     * Handles keypad input and returns updated KeypadState
     */
    fun handleNumberClick(key: Keypad, currentState: KeypadState): KeypadState {
        val isLeftNumberActive = currentState.operator.isBlank()
        
        return when (key) {
            in Keypad.Companion.numbers -> handleNumberInput(key, currentState, isLeftNumberActive)
            in Keypad.Companion.operators -> handleOperatorInput(key, currentState)
            Keypad.DOT -> handleDotInput(currentState, isLeftNumberActive)
            Keypad.EQUALS -> handleEqualsInput(currentState)
            Keypad.CLEAR -> handleClearInput()
            Keypad.DEL -> handleDeleteInput(currentState, isLeftNumberActive)
            else -> currentState
        }
    }
    
    private fun handleNumberInput(key: Keypad, state: KeypadState, isLeftNumberActive: Boolean): KeypadState {
        with(state) {
            if (key in listOf(Keypad.ZERO, Keypad.ZERO_ZERO, Keypad.ZERO_ZERO_ZERO)) {
                if (isLeftNumberActive && leftNumber.isEmpty() || !isLeftNumberActive && rightNumber.isEmpty()) {
                    return state
                }
            }
            
            return when {
                isEqualed -> {
                    isEqualed = false
                    if (operator.isEmpty()) {
                        state.copy(leftNumber = Keypad.Companion.number(key))
                    } else {
                        state.copy(rightNumber = Keypad.Companion.number(key))
                    }
                }
                
                isLeftNumberActive -> {
                    if ((leftNumber.toDoubleOrNull() ?: 0.0) > AMOUNT_MAX) return state
                    val separatorIndex = leftNumber.lastIndexOf(DOT)
                    if (separatorIndex != -1 && leftNumber.length - separatorIndex > FRAC_LENGTH) return state
                    val newLeftNumber = leftNumber + Keypad.Companion.number(key)
                    state.copy(leftNumber = newLeftNumber)
                }
                
                else -> {
                    if ((rightNumber.toDoubleOrNull() ?: 0.0) > AMOUNT_MAX) return state
                    val separatorIndex = rightNumber.lastIndexOf(DOT)
                    if (separatorIndex != -1 && rightNumber.length - separatorIndex > FRAC_LENGTH) {
                        return state
                    }
                    val newRightNumber = rightNumber + Keypad.Companion.number(key)
                    state.copy(rightNumber = newRightNumber)
                }
            }
        }
    }
    
    private fun handleOperatorInput(key: Keypad, state: KeypadState): KeypadState {
        return with(state) {
            val op = Keypad.Companion.operator(key)
            val a = leftNumber.toDoubleOrNull() ?: 0.0
            val b = rightNumber.toDoubleOrNull() ?: 0.0
            val result = calculate(a, b, op)
            state.copy(
                operator = op,
                leftNumber = result.toString(),
                rightNumber = ""
            )
        }
    }
    
    private fun handleDotInput(state: KeypadState, isLeftNumberActive: Boolean): KeypadState {
        return with(state) {
            if (isLeftNumberActive) {
                val newLeftNumber = if (leftNumber.isBlank()) "0." else "$leftNumber."
                state.copy(leftNumber = newLeftNumber)
            } else {
                val newRightNumber = if (rightNumber.isBlank()) "0." else "$rightNumber."
                state.copy(rightNumber = newRightNumber)
            }
        }
    }
    
    private fun handleEqualsInput(state: KeypadState): KeypadState {
        val result = if (state.operator.isNotBlank()) {
            calculate(
                left = state.leftNumber.toDoubleOrNull() ?: 0.0,
                right = state.rightNumber.toDoubleOrNull() ?: 0.0,
                operator = state.operator
            )
        } else 0.0
        
        isEqualed = true
        return state.copy(
            leftNumber = result.toString(),
            rightNumber = "",
            operator = "",
        )
    }
    
    private fun handleClearInput(): KeypadState {
        isEqualed = false
        return KeypadState()
    }
    
    private fun handleDeleteInput(state: KeypadState, isLeftNumberActive: Boolean): KeypadState {
        return with(state) {
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
    }
    
    private fun calculate(left: Double, right: Double, operator: String): Double {
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
}
