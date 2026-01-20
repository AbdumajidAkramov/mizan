package dev.esbi.mizan.feature.addtransaction.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateFlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateInputMode
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateAmount

internal object AddTransactionReducer :
    Reducer<AddTransactionStore.State, AddTransactionStore.Message> {
    override fun AddTransactionStore.State.reduce(msg: AddTransactionStore.Message): AddTransactionStore.State =
        when (msg) {
            is UpdateFlowState -> copy(flowState = msg.flowState)
            is UpdateInputMode -> copy(inputMode = msg.inputMode)
            is UpdateAmount -> copy(amount = msg.amount)
        }
}
