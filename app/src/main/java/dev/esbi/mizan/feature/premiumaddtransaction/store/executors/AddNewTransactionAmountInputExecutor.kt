package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

internal class AddNewTransactionAmountInputExecutor @Inject constructor() :
    CoroutineExecutor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>() {

    companion object {
        private const val MAX_DECIMAL_PLACES = 2
        private val OPERATORS = listOf('+', '-', '*', '/')
    }

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            is AddNewTransactionStore.Intent.OnNumberClick -> handleKeypadClick(intent.key)
            else -> Unit
        }
    }

    private fun handleKeypadClick(key: Keypad) {
        with(state()) {
            when {
                key in Keypad.numbers -> handleNumberKey(key)
                key == Keypad.DOT -> handleDotKey()
                key in Keypad.operators -> handleOperatorKey(key)
                key == Keypad.EQUAL -> handleEqualsKey()
                key == Keypad.CLEAR -> handleClearKey()
                key == Keypad.DELETE -> handleDeleteKey()
            }
        }
    }

    private fun AddNewTransactionStore.State.handleNumberKey(key: Keypad) {
        val digit = Keypad.number(key)
        val isLeftActive = operator.isEmpty()
        val activeOperand = if (isLeftActive) leftNumber else rightNumber

        // If showing final result and no operator, start fresh
        val shouldReplace = (isFinalResult && isLeftActive) || activeOperand == "0"

        // Precision guard: check if already has 2 decimal places
        if (hasMaxDecimalPlaces(activeOperand)) {
            return // Don't append more digits
        }

        val newOperand = if (shouldReplace) digit else activeOperand + digit

        val (newLeft, newRight, newIsFinal) = if (isLeftActive) {
            Triple(newOperand, rightNumber, false)
        } else {
            Triple(leftNumber, newOperand, false)
        }

        dispatchUpdate(newLeft, newRight, operator, newIsFinal)
    }

    private fun AddNewTransactionStore.State.handleDotKey() {
        val isLeftActive = operator.isEmpty()
        val activeOperand = if (isLeftActive) leftNumber else rightNumber

        // Only add dot if operand doesn't already have one
        if (activeOperand.contains('.')) {
            return
        }

        // If operand is empty, make it "0."
        val newOperand = if (activeOperand.isEmpty() || activeOperand == "0") {
            "0."
        } else {
            "$activeOperand."
        }

        val (newLeft, newRight) = if (isLeftActive) {
            Pair(newOperand, rightNumber)
        } else {
            Pair(leftNumber, newOperand)
        }

        dispatchUpdate(newLeft, newRight, operator, false)
    }

    private fun AddNewTransactionStore.State.handleOperatorKey(key: Keypad) {
        val newOp = Keypad.operator(key)

        // If both operands present, calculate first
        if (operator.isNotEmpty() && rightNumber.isNotEmpty()) {
            val result = calculate(leftNumber, rightNumber, operator)
            dispatchUpdate(result, "", newOp, false)
        } else if (operator.isNotEmpty() && rightNumber.isEmpty()) {
            // Replace existing operator
            dispatchUpdate(leftNumber, "", newOp, false)
        } else {
            // Just set the operator
            val left = if (leftNumber.isEmpty()) "0" else leftNumber
            dispatchUpdate(left, "", newOp, false)
        }
    }

    private fun AddNewTransactionStore.State.handleEqualsKey() {
        if (operator.isNotEmpty() && rightNumber.isNotEmpty()) {
            val result = calculate(leftNumber, rightNumber, operator)
            dispatchUpdate(result, "", "", true)
        }
        forward(AddNewTransactionStore.Action.CheckAndConfirm)
    }

    private fun AddNewTransactionStore.State.handleClearKey() {
        dispatchUpdate("0", "", "", false)
    }

    private fun AddNewTransactionStore.State.handleDeleteKey() {
        val isLeftActive = operator.isEmpty()

        when {
            // If right operand has content, delete from it
            !isLeftActive && rightNumber.isNotEmpty() -> {
                val newRight = rightNumber.dropLast(1)
                dispatchUpdate(leftNumber, newRight, operator, false)
            }
            // If right is empty but operator exists, clear operator
            !isLeftActive && rightNumber.isEmpty() -> {
                dispatchUpdate(leftNumber, "", "", false)
            }
            // Delete from left operand
            isLeftActive && leftNumber.isNotEmpty() && leftNumber != "0" -> {
                val newLeft = leftNumber.dropLast(1).ifEmpty { "0" }
                dispatchUpdate(newLeft, "", "", false)
            }
        }
    }

    private fun AddNewTransactionStore.State.dispatchUpdate(
        newLeft: String,
        newRight: String,
        newOperator: String,
        newIsFinalResult: Boolean
    ) {
        // Build display text: leftNumber + operator + rightNumber
        val displayText = buildDisplayText(newLeft, newOperator, newRight)

        // Parse amount for storage (use left number as the current amount)
        val amountDecimal = parseStringToBigDecimal(newLeft)

        dispatch(
            AddNewTransactionStore.Message.UpdateAmount(
                operator = newOperator,
                leftNumber = newLeft,
                rightNumber = newRight,
                isFinalResult = newIsFinalResult,
                currency = currency,
                amountDecimal = amountDecimal,
                displayText = displayText
            )
        )
    }

    private fun buildDisplayText(left: String, op: String, right: String): String {
        val leftDisplay = left.ifEmpty { "0" }
        return when {
            op.isEmpty() -> leftDisplay
            right.isEmpty() -> "$leftDisplay$op"
            else -> "$leftDisplay$op$right"
        }
    }

    private fun hasMaxDecimalPlaces(operand: String): Boolean {
        val dotIndex = operand.indexOf('.')
        if (dotIndex == -1) return false
        val decimalPlaces = operand.length - dotIndex - 1
        return decimalPlaces >= MAX_DECIMAL_PLACES
    }

    private fun calculate(leftStr: String, rightStr: String, op: String): String {
        val left = parseStringToBigDecimal(leftStr)
        val right = parseStringToBigDecimal(rightStr)

        val result = when (op) {
            "+" -> left.add(right)
            "-" -> left.subtract(right)
            "*" -> left.multiply(right).setScale(MAX_DECIMAL_PLACES, RoundingMode.HALF_EVEN)
            "/" -> {
                if (right != BigDecimal.ZERO) {
                    left.divide(right, MAX_DECIMAL_PLACES, RoundingMode.HALF_EVEN)
                } else {
                    BigDecimal.ZERO
                }
            }
            else -> left
        }

        // Round to 2 decimal places and format
        val rounded = result.setScale(MAX_DECIMAL_PLACES, RoundingMode.HALF_EVEN)
        return rounded.stripTrailingZeros().toPlainString()
    }

    private fun parseStringToBigDecimal(str: String): BigDecimal {
        if (str.isEmpty() || str == "0" || str == ".") {
            return BigDecimal.ZERO
        }

        // Handle trailing dot: "123." -> parse as "123"
        val cleanStr = if (str.endsWith(".")) str.dropLast(1) else str

        return try {
            if (cleanStr.isEmpty()) BigDecimal.ZERO else BigDecimal(cleanStr)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }
}
