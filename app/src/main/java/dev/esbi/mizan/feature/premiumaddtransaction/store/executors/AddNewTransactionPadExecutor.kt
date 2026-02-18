package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import javax.inject.Inject

internal class AddNewTransactionPadExecutor @Inject constructor(

) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeAction(action: AddNewTransactionStore.Action) {
        when (action) {
            else -> Unit
        }
    }

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            is AddNewTransactionStore.Intent.OnClosePad -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = null)
                )
            }
            is AddNewTransactionStore.Intent.ShowTypeSelector -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.TypeSelector)
                )
            }

            is AddNewTransactionStore.Intent.ShowCategorySelector -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.CategorySelector)
                )
            }

            is AddNewTransactionStore.Intent.ShowSelectAccountSelector -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.AccountSelector)
                )
            }

            is AddNewTransactionStore.Intent.ShowTargetAccountSelector -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.TargetAccountSelector)
                )
            }

            is AddNewTransactionStore.Intent.ShowAmountInputPad -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.AmountInput)
                )
            }

            else -> Unit
        }
    }

}
