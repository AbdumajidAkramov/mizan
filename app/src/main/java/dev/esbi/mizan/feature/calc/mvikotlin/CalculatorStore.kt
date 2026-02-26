package dev.esbi.mizan.feature.calc.mvikotlin

import com.arkivanov.mvikotlin.core.store.Store

interface CalculatorStore :
    Store<CalculatorStore.Intent, CalculatorStore.State, CalculatorStore.Label> {
    sealed class Intent {
        data class Input(val value: String) : Intent()
        object Clear : Intent()
        object Delete : Intent()
        object Evaluate : Intent()
    }

    data class State(
        val expression: String = "",
        val currentValue: String = "0",
        val isResultShown: Boolean = false
    )

    sealed interface Label
    sealed interface Message {
        data class Update(
            val currentValue: String,
            val expression: String,
            val isResultShown: Boolean = false
        ) : Message
    }

    sealed interface Action
}