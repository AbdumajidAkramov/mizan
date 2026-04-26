package dev.esbi.mizan.feature.newtransaction2.store.executors

import dev.esbi.mizan.feature.newtransaction2.store.state.KeypadState
import dev.esbi.mizan.presentation.feature.addtransaction.model.Keypad
import dev.esbi.mizan.presentation.utils.AMOUNT_MAX
import dev.esbi.mizan.presentation.utils.DOT
import java.math.BigDecimal
import java.math.RoundingMode
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
            Keypad.EQUAL -> handleEqualsInput(currentState)
            Keypad.CLEAR -> handleClearInput()
            Keypad.DELETE -> handleDeleteInput(currentState, isLeftNumberActive)
            else -> currentState
        }
    }

    private fun handleNumberInput(
        key: Keypad,
        state: KeypadState,
        isLeftNumberActive: Boolean
    ): KeypadState {
        with(state) {
            val digitToAdd = Keypad.Companion.number(key)

            return when {
                isEqualed -> {
                    isEqualed = false
                    if (operator.isEmpty()) {
                        state.copy(leftNumber = BigDecimal(digitToAdd))
                    } else {
                        state.copy(rightNumber = BigDecimal(digitToAdd))
                    }
                }

                isLeftNumberActive -> {
                    val newLeftNumber = appendDigit(leftNumber.toPlainString(), digitToAdd)
                    val newLeftDecimal = BigDecimal(newLeftNumber)
                    if (newLeftDecimal.toDouble() > AMOUNT_MAX) return state
                    state.copy(leftNumber = newLeftDecimal)
                }

                else -> {
                    val newRightNumber = appendDigit(rightNumber.toPlainString(), digitToAdd)
                    val newRightDecimal = BigDecimal(newRightNumber)
                    if (newRightDecimal.toDouble() > AMOUNT_MAX) return state
                    state.copy(rightNumber = newRightDecimal)
                }
            }
        }
    }

    /**
     * Appends a digit to the current amount string with validation:
     * - Replaces "0" with the new digit (avoid "05")
     * - Enforces max 2 digits after decimal point
     */
    private fun appendDigit(current: String, digit: String): String {
        // If current is exactly "0", replace it with the new digit
        if (current == "0") {
            return digit
        }

        // Check if decimal point exists and enforce 2-digit limit after decimal
        val decimalIndex = current.indexOf(DOT)
        if (decimalIndex != -1) {
            val digitsAfterDecimal = current.length - decimalIndex - 1
            if (digitsAfterDecimal >= 2) {
                return current // Already have 2 digits after decimal
            }
        }

        return current + digit
    }

    private fun handleOperatorInput(key: Keypad, state: KeypadState): KeypadState {
        return with(state) {
            val newOp = Keypad.Companion.operator(key)

            // If operator is empty, just set the new operator
            if (operator.isEmpty()) {
                state.copy(operator = newOp)
            } else {
                // Chained operation: evaluate current expression first
                val result = calculateBigDecimal(leftNumber, rightNumber, operator)
                state.copy(
                    operator = newOp,
                    leftNumber = result,
                    rightNumber = BigDecimal.ZERO
                )
            }
        }
    }

    private fun handleDotInput(state: KeypadState, isLeftNumberActive: Boolean): KeypadState {
        return with(state) {
            if (isLeftNumberActive) {
                val leftStr = leftNumber.toPlainString()
                // Only add decimal if one doesn't already exist
                if (!leftStr.contains(DOT)) {
                    val newLeftNumber = if (leftNumber == BigDecimal.ZERO) "0." else "$leftStr."
                    state.copy(leftNumber = BigDecimal(newLeftNumber))
                } else {
                    state // Already has decimal point
                }
            } else {
                val rightStr = rightNumber.toPlainString()
                // Only add decimal if one doesn't already exist
                if (!rightStr.contains(DOT)) {
                    val newRightNumber = if (rightNumber == BigDecimal.ZERO) "0." else "$rightStr."
                    state.copy(rightNumber = BigDecimal(newRightNumber))
                } else {
                    state // Already has decimal point
                }
            }
        }
    }

    private fun handleEqualsInput(state: KeypadState): KeypadState {
        val result = if (state.operator.isNotBlank()) {
            calculateBigDecimal(
                left = state.leftNumber,
                right = state.rightNumber,
                operator = state.operator
            )
        } else BigDecimal.ZERO

        isEqualed = true
        return state.copy(
            leftNumber = result,
            rightNumber = BigDecimal.ZERO,
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
                val leftStr = leftNumber.toPlainString()
                if (leftStr.isNotBlank() && leftStr != "0") {
                    val newLeftNumber = leftStr.dropLast(1)
                    // If empty or just "-", reset to "0"
                    val finalLeftNumber =
                        if (newLeftNumber.isEmpty() || newLeftNumber == "-") "0" else newLeftNumber
                    state.copy(leftNumber = BigDecimal(finalLeftNumber))
                } else {
                    state
                }
            } else {
                // Deleting from rightNumber
                val rightStr = rightNumber.toPlainString()
                if (rightStr.isNotBlank() && rightStr != "0") {
                    val newRightNumber = rightStr.dropLast(1)
                    // If empty or just "-", reset to "0"
                    val finalRightNumber =
                        if (newRightNumber.isEmpty() || newRightNumber == "-") "0" else newRightNumber
                    state.copy(rightNumber = BigDecimal(finalRightNumber))
                } else {
                    // rightNumber is already 0, clear operator and switch to leftNumber
                    state.copy(
                        operator = "",
                        rightNumber = BigDecimal.ZERO
                    )
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

    private fun calculateBigDecimal(
        left: BigDecimal,
        right: BigDecimal,
        operator: String
    ): BigDecimal {
        return when (operator) {
            "+" -> left.add(right)
            "-" -> left.subtract(right)
            "*" -> left.multiply(right)
            "/" -> {
                if (right != BigDecimal.ZERO) {
                    left.divide(right, 2, RoundingMode.HALF_UP)
                } else {
                    BigDecimal.ZERO
                }
            }

            else -> BigDecimal.ZERO
        }
    }
}