package dev.esbi.mizan.transactions.add.mvikotlin.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.model.Keypad
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.presentation.feature.calc.evaluateSimpleExpression
import javax.inject.Inject

class AddNewTransactionCalculatorExecutor @Inject constructor() :
    CoroutineExecutor<Intent, Action, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Input -> handleInput(intent.value)
            is Intent.Clear -> dispatch(Message.UpdateDisplayText("0", ""))
            is Intent.Delete -> handleDelete()
            is Intent.Evaluate -> handleEvaluate()
            is Intent.OnNumberClick -> handleNumberPad(intent.key)
            else -> Unit
        }
    }

    private fun handleNumberPad(key: Keypad) {
        val state = state()
        val current = state.currentValue
        when (key) {
            Keypad.CLEAR -> dispatch(Message.UpdateDisplayText("0", ""))
            Keypad.DELETE -> handleDelete()
            Keypad.EQUAL -> handleEvaluate()
            Keypad.DOT -> {
                if (!current.contains(".") && !state.isResultShown) {
                    dispatch(Message.UpdateDisplayText("$current.", state.expression))
                }
            }

            Keypad.ZERO_ZERO -> {
                if (state.currentValue != "0") {
                    dispatch(
                        Message.UpdateDisplayText(
                            state.currentValue + "00",
                            state.expression
                        )
                    )
                }

            }

            Keypad.ZERO_ZERO_ZERO,
                -> {
                if (state.currentValue != "0") {
                    dispatch(
                        Message.UpdateDisplayText(
                            state.currentValue + "000",
                            state.expression
                        )
                    )
                }
            }

            in Keypad.operators -> handleOperator(Keypad.operator(key))
            in Keypad.numbers -> handleInput(Keypad.number(key))
            else -> {
                val input = Keypad.number(key)
                if (state.isResultShown) {
                    dispatch(Message.UpdateDisplayText(input, "", isResultShown = false))
                } else if (canAppendDigit(current, 1)) {
                    val next = if (current == "0") input else current + input
                    dispatch(Message.UpdateDisplayText(next, state.expression))
                }
            }
        }
    }

    private fun handleOperator(input: String) {
        val state = state()
        val op = input.replace("×", "*").replace("÷", "/")
        // handleInput ichida operator tekshiruvi boshida:
        if (state.currentValue == "0" && state.expression.isNotEmpty()) {
            val newExp = state.expression.dropLast(1) + op
            dispatch(Message.UpdateDisplayText("0", newExp))
            return
        }
        // Agar tepada (expression) allaqachon son bo'lsa, hisoblaymiz
        if (state.expression.isNotEmpty()) {
            val fullExp = state.expression + state.currentValue
            val result = evaluateSimpleExpression(fullExp)
            // Natijani tepaga o'tkazamiz va yangi operatorni qo'shamiz
            dispatch(Message.UpdateDisplayText(currentValue = "0", expression = "$result$input"))
        } else {
            // Birinchi marta operator bosilganda
            dispatch(
                Message.UpdateDisplayText(
                    currentValue = "0",
                    expression = state.currentValue + op
                )
            )
        }
    }

    private fun handleInput(input: String) {
        val state = state()
        val current = state.currentValue
        when (input) {
            "+", "-", "×", "÷" -> {

                val op = input.replace("×", "*").replace("÷", "/")
                // handleInput ichida operator tekshiruvi boshida:
                if (state.currentValue == "0" && state.expression.isNotEmpty()) {
                    val newExp = state.expression.dropLast(1) + op
                    dispatch(Message.UpdateDisplayText("0", newExp))
                    return
                }
                // Agar tepada (expression) allaqachon son bo'lsa, hisoblaymiz
                if (state.expression.isNotEmpty()) {
                    val fullExp = state.expression + state.currentValue
                    val result = evaluateSimpleExpression(fullExp)
                    // Natijani tepaga o'tkazamiz va yangi operatorni qo'shamiz
                    dispatch(
                        Message.UpdateDisplayText(
                            currentValue = "0",
                            expression = "$result$op"
                        )
                    )
                } else {
                    // Birinchi marta operator bosilganda
                    dispatch(
                        Message.UpdateDisplayText(
                            currentValue = "0",
                            expression = state.currentValue + op
                        )
                    )
                }
            }

            "00", "000" -> {
                if (state.currentValue != "0") {
                    dispatch(
                        Message.UpdateDisplayText(
                            state.currentValue + input,
                            state.expression
                        )
                    )
                }
            }

            "." -> {
                if (!current.contains(".") && !state.isResultShown) {
                    dispatch(Message.UpdateDisplayText("$current.", state.expression))
                }
            }

            else -> { // Raqamlar 0-9
                if (state.isResultShown) {
                    dispatch(Message.UpdateDisplayText(input, "", isResultShown = false))
                } else if (canAppendDigit(current, 1)) {
                    val next = if (current == "0") input else current + input
                    dispatch(Message.UpdateDisplayText(next, state.expression))
                }
            }
        }
    }

    /**
     * Nuqtadan keyin 2 tadan ko'p raqam bo'lmasligini tekshiruvchi yordamchi funksiya
     * Butun qismi 12 xonadan, kasr qismi 2 xonadan oshmasligini tekshiradi
     */
    private fun canAppendDigit(current: String, incomingLength: Int): Boolean {
        val parts = current.split(".")
        val integerPart = parts[0]
        val hasDecimal = parts.size > 1

        return if (hasDecimal) {
            // Nuqtadan keyin bo'lsa: tiyinlar soni 2 tadan oshmasligi kerak
            val fractionalPart = parts[1]
            fractionalPart.length + incomingLength <= 2
        } else {
            // Nuqtadan oldin bo'lsa: butun qismi 12 tadan oshmasligi kerak
            integerPart.length + incomingLength <= 12
        }
    }

    private fun handleEvaluate() {
        val state = state()
        val result = evaluateSimpleExpression(state.expression + state.currentValue)
        dispatch(Message.UpdateDisplayText(result, "", isResultShown = true))
    }

    private fun handleDelete() {
        val current = state().currentValue
        val next = if (current.length <= 1) "0" else current.dropLast(1)
        dispatch(Message.UpdateDisplayText(next, state().expression))
    }
}
