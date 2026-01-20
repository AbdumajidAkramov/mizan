package dev.esbi.mizan.feature.addtransaction.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.ClearText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateFlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateInputMode
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateLeftText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateOperator
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateRightText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionCategory
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionDate
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionNotes
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionType

internal object AddTransactionReducer :
    Reducer<AddTransactionStore.State, AddTransactionStore.Message> {
    override fun AddTransactionStore.State.reduce(msg: AddTransactionStore.Message): AddTransactionStore.State =
        when (msg) {
            is UpdateFlowState -> copy(flowState = msg.flowState)
            is UpdateInputMode -> copy(inputMode = msg.inputMode)
            is UpdateOperator -> copy(operator = msg.text)
            is UpdateLeftText -> copy(leftNumber = msg.text)
            is UpdateRightText -> copy(rightNumber = msg.text)
            is ClearText -> copy(
                leftNumber = "",
                rightNumber = "",
                operator = ""
            )

            is UpdateTransactionCategory -> copy(
                selectedCategory = msg.category,
                flowState = FlowState.Confirm
            )

            is UpdateTransactionType -> copy(type = msg.type)
            is UpdateTransactionDate -> copy(date = msg.date)
            is UpdateTransactionNotes -> copy(notes = msg.notes)
        }
}
