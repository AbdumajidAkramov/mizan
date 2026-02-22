package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.utils.DOT
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

internal class AddNewTransactionAmountInputExecutor @Inject constructor() :
    CoroutineExecutor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>() {

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            is AddNewTransactionStore.Intent.OnNumberClick -> {
                with(state()) {
                    if (intent.key == Keypad.EQUALS) {
                        forward(AddNewTransactionStore.Action.CheckAndConfirm)
                    } else {
                        val (newOperator, newLeft, newRight) = when (intent.key) {
                            in Keypad.numbers -> handleNumberInput(
                                key = intent.key,
                                operator = operator,
                                leftDecimal = leftDecimal,
                                rightDecimal = rightDecimal
                            )
                            
                            in Keypad.operators -> handleOperatorInput(
                                key = intent.key,
                                operator = operator,
                                leftDecimal = leftDecimal,
                                rightDecimal = rightDecimal
                            )
                            
                            Keypad.DOT -> handleDotInput(
                                operator = operator,
                                leftDecimal = leftDecimal,
                                rightDecimal = rightDecimal
                            )
                            
                            Keypad.CLEAR -> Triple("", BigDecimal.ZERO, BigDecimal.ZERO)
                            
                            Keypad.DEL -> handleDeleteInput(
                                operator = operator,
                                leftDecimal = leftDecimal,
                                rightDecimal = rightDecimal
                            )
                            
                            else -> Triple(operator, leftDecimal, rightDecimal)
                        }
                        
                        val newDisplayText = when (intent.key) {
                            in Keypad.numbers -> updateDisplayTextForNumber(
                                currentDisplay = displayText,
                                digit = Keypad.Companion.number(intent.key)
                            )
                            
                            in Keypad.operators -> updateDisplayTextForOperator(
                                currentDisplay = displayText,
                                operator = Keypad.Companion.operator(intent.key)
                            )
                            
                            Keypad.DOT -> updateDisplayTextForDot(displayText)
                            
                            Keypad.CLEAR -> ""
                            
                            Keypad.DEL -> if (displayText.isNotEmpty()) {
                                displayText.dropLast(1)
                            } else {
                                ""
                            }
                            
                            else -> displayText
                        }
                        
                        dispatch(
                            AddNewTransactionStore.Message.UpdateAmount(
                                operator = newOperator,
                                leftNumber = newLeft,
                                rightNumber = newRight,
                                currency = currency,
                                amountDecimal = newLeft,
                                displayText = newDisplayText
                            )
                        )
                    }
                }
            }

            else -> Unit
        }
    }
    
    private fun handleNumberInput(
        key: Keypad,
        operator: String,
        leftDecimal: BigDecimal,
        rightDecimal: BigDecimal
    ): Triple<String, BigDecimal, BigDecimal> {
        val digit = Keypad.Companion.number(key)
        val isLeftActive = operator.isEmpty()
        
        return if (isLeftActive) {
            val newLeft = appendDigit(leftDecimal, digit)
            Triple(operator, newLeft, rightDecimal)
        } else {
            val newRight = appendDigit(rightDecimal, digit)
            Triple(operator, leftDecimal, newRight)
        }
    }
    
    private fun appendDigit(current: BigDecimal, digit: String): BigDecimal {
        val currentStr = current.toPlainString()
        
        // Replace "0" with new digit
        if (current == BigDecimal.ZERO && !currentStr.contains(DOT)) {
            return BigDecimal(digit)
        }
        
        // Check decimal limit
        val decimalIndex = currentStr.indexOf(DOT)
        if (decimalIndex != -1) {
            val digitsAfterDecimal = currentStr.length - decimalIndex - 1
            if (digitsAfterDecimal >= 2) {
                return current
            }
        }
        
        return BigDecimal(currentStr + digit)
    }
    
    private fun handleOperatorInput(
        key: Keypad,
        operator: String,
        leftDecimal: BigDecimal,
        rightDecimal: BigDecimal
    ): Triple<String, BigDecimal, BigDecimal> {
        val newOp = Keypad.Companion.operator(key)
        
        return if (operator.isEmpty()) {
            // Just set the operator
            Triple(newOp, leftDecimal, rightDecimal)
        } else {
            // Chained operation: evaluate first
            val result = calculate(leftDecimal, rightDecimal, operator)
            Triple(newOp, result, BigDecimal.ZERO)
        }
    }
    
    private fun handleDotInput(
        operator: String,
        leftDecimal: BigDecimal,
        rightDecimal: BigDecimal
    ): Triple<String, BigDecimal, BigDecimal> {
        val isLeftActive = operator.isEmpty()
        
        return if (isLeftActive) {
            val leftStr = leftDecimal.toPlainString()
            if (!leftStr.contains(DOT)) {
                val newLeft = BigDecimal("$leftStr.")
                Triple(operator, newLeft, rightDecimal)
            } else {
                Triple(operator, leftDecimal, rightDecimal)
            }
        } else {
            val rightStr = rightDecimal.toPlainString()
            if (!rightStr.contains(DOT)) {
                val newRight = BigDecimal("$rightStr.")
                Triple(operator, leftDecimal, newRight)
            } else {
                Triple(operator, leftDecimal, rightDecimal)
            }
        }
    }
    
    private fun handleDeleteInput(
        operator: String,
        leftDecimal: BigDecimal,
        rightDecimal: BigDecimal
    ): Triple<String, BigDecimal, BigDecimal> {
        val isLeftActive = operator.isEmpty()
        
        return if (isLeftActive) {
            val leftStr = leftDecimal.toPlainString()
            if (leftStr.isNotBlank() && leftStr != "0") {
                val newStr = leftStr.dropLast(1)
                val finalStr = if (newStr.isEmpty() || newStr == "-" || newStr == ".") "0" else newStr
                Triple(operator, BigDecimal(finalStr), rightDecimal)
            } else {
                Triple(operator, leftDecimal, rightDecimal)
            }
        } else {
            val rightStr = rightDecimal.toPlainString()
            if (rightStr.isNotBlank() && rightStr != "0") {
                val newStr = rightStr.dropLast(1)
                val finalStr = if (newStr.isEmpty() || newStr == "-" || newStr == ".") "0" else newStr
                Triple(operator, leftDecimal, BigDecimal(finalStr))
            } else {
                // Clear operator and switch to left
                Triple("", leftDecimal, BigDecimal.ZERO)
            }
        }
    }
    
    private fun calculate(left: BigDecimal, right: BigDecimal, operator: String): BigDecimal {
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
            else -> left
        }
    }
    
    private fun updateDisplayTextForNumber(currentDisplay: String, digit: String): String {
        // If display is empty or "0", replace with digit
        if (currentDisplay.isEmpty() || currentDisplay == "0") {
            return digit
        }
        return currentDisplay + digit
    }
    
    private fun updateDisplayTextForOperator(currentDisplay: String, operator: String): String {
        if (currentDisplay.isEmpty()) {
            return "0$operator"
        }
        
        // Check if last character is already an operator
        val lastChar = currentDisplay.lastOrNull()
        if (lastChar != null && lastChar in listOf('+', '-', '*', '/')) {
            // Replace the last operator
            return currentDisplay.dropLast(1) + operator
        }
        
        return currentDisplay + operator
    }
    
    private fun updateDisplayTextForDot(currentDisplay: String): String {
        if (currentDisplay.isEmpty()) {
            return "0."
        }
        
        // Find the last operator position to check the current number
        val lastOperatorIndex = currentDisplay.indexOfLast { it in listOf('+', '-', '*', '/') }
        val currentNumber = if (lastOperatorIndex == -1) {
            currentDisplay
        } else {
            currentDisplay.substring(lastOperatorIndex + 1)
        }
        
        // Only add dot if current number doesn't have one
        if (!currentNumber.contains('.')) {
            return currentDisplay + "."
        }
        
        return currentDisplay
    }
}
