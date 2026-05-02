package dev.esbi.mizan.presentation.feature.addtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import javax.inject.Inject


class AddNewTransactionPadExecutor @Inject constructor(

) : CoroutineExecutor<Intent,
        Action,
        State,
        Message,
        Label>() {

    override fun executeAction(action: Action) {
        when (action) {
            else -> Unit
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClosePad -> {
                dispatch(
                    Message.UpdatePad(pad = null)
                )
            }

            is Intent.ShowTypeSelector -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.TypeSelector)
                )
            }

            is Intent.ShowCategorySelector -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.CategorySelector)
                )
            }

            is Intent.ShowSelectAccountSelector -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.AccountSelector)
                )
            }

            is Intent.ShowTargetAccountSelector -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.TargetAccountSelector)
                )
            }

            is Intent.ShowAmountInputPad -> {
                dispatch(
                    Message.UpdatePad(pad = State.Pad.AmountInput)
                )
            }

            else -> Unit
        }
    }

}
