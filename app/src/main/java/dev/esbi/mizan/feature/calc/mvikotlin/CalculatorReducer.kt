package dev.esbi.mizan.feature.calc.mvikotlin

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Message
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.State

internal object CalculatorReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.Update -> copy(
                currentValue = msg.currentValue,
                expression = msg.expression,
                isResultShown = msg.isResultShown
            )
        }
}
