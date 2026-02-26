package dev.esbi.mizan.feature.calc.mvikotlin

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.calc.evaluateSimpleExpression
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Action
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Intent
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Label
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Message
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.State

internal class CalculatorExecutor : CoroutineExecutor<Intent, Action, State, Message, Label>() {
    override fun executeIntent(intent: CalculatorStore.Intent) {
        when (intent) {
            is Intent.Input -> handleInput(intent.value)
            is Intent.Clear -> dispatch(Message.Update("0", ""))
            is Intent.Delete -> handleDelete()
            is Intent.Evaluate -> handleEvaluate()
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
                    dispatch(Message.Update("0", newExp))
                    return
                }
                // Agar tepada (expression) allaqachon son bo'lsa, hisoblaymiz
                if (state.expression.isNotEmpty()) {
                    val fullExp = state.expression + state.currentValue
                    val result = evaluateSimpleExpression(fullExp)
                    // Natijani tepaga o'tkazamiz va yangi operatorni qo'shamiz
                    dispatch(Message.Update(currentValue = "0", expression = "$result$op"))
                } else {
                    // Birinchi marta operator bosilganda
                    dispatch(
                        Message.Update(
                            currentValue = "0",
                            expression = state.currentValue + op
                        )
                    )
                }
            }

            "00", "000" -> {
                if (state.currentValue != "0") {
                    dispatch(Message.Update(state.currentValue + input, state.expression))
                }
            }

            "." -> {
                if (!current.contains(".") && !state.isResultShown) {
                    dispatch(Message.Update("$current.", state.expression))
                }
            }

            else -> { // Raqamlar 0-9
                if (state.isResultShown) {
                    dispatch(Message.Update(input, "", isResultShown = false))
                } else if (canAppendDigit(current, 1)) {
                    val next = if (current == "0") input else current + input
                    dispatch(Message.Update(next, state.expression))
                }
            }
        }
    }

    /**
     * Nuqtadan keyin 2 tadan ko'p raqam bo'lmasligini tekshiruvchi yordamchi funksiya
     */
    private fun canAppendDigit(current: String, incomingLength: Int): Boolean {
        if (!current.contains(".")) return true // Nuqta yo'q bo'lsa, xohlagancha raqam yozish mumkin

        val fractionalPart = current.substringAfter(".")
        // Hozirgi tiyinlar soni + yangi kelayotgan raqamlar soni 2 tadan oshmasligi kerak
        return (fractionalPart.length + incomingLength) <= 2
    }

    private fun handleEvaluate() {
        val state = state()
        val result = evaluateSimpleExpression(state.expression + state.currentValue)
        dispatch(Message.Update(result, "", isResultShown = true))
    }

    private fun handleDelete() {
        val current = state().currentValue
        val next = if (current.length <= 1) "0" else current.dropLast(1)
        dispatch(Message.Update(next, state().expression))
    }
}
