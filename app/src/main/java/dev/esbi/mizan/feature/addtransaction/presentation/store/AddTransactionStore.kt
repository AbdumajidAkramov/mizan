package dev.esbi.mizan.feature.addtransaction.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.addtransaction.domain.models.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionResult

internal interface AddTransactionStore :
    Store<AddTransactionStore.Intent, AddTransactionStore.State, AddTransactionStore.Label> {
    data class State(
        val title: String = "",
        val amount: Double = 0.0,
        val flowState: FlowState = FlowState.Amount,
        val inputMode: InputMode = InputMode.Manual,
    )

    sealed interface Intent {
        class OnKeypadClick(val key: Keypad) : Intent
        class OnSaveTransaction(val value: TransactionResult) : Intent
        class OnNext(val flowState: FlowState? = null) : Intent
        class OnInputModeChange(val inputMode: InputMode) : Intent
    }

    sealed interface Label
    sealed interface Action {
        data object Init : Action
    }

    sealed interface Message {
        class UpdateFlowState(val flowState: FlowState) : Message
        class UpdateInputMode(val inputMode: InputMode) : Message
        class UpdateAmount(val amount: Double) : Message
    }
}
