package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import javax.inject.Inject

internal class AddNewTransactionExecutor @Inject constructor(

) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeAction(action: AddNewTransactionStore.Action) {
        when (action) {
            is AddNewTransactionStore.Action.CheckAndConfirm -> {
                checkPadState()
            }

            else -> Unit
        }
    }

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            is AddNewTransactionStore.Intent.SelectTransactionType -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdateTransactionType(type = intent.type)
                )
                forward(AddNewTransactionStore.Action.CheckAndConfirm)
            }

            else -> Unit
        }
    }

    private fun checkPadState() {
        val state = state()
        when {
            state.amount == 0.0 -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.AmountInput)
                )
            }

            state.selectedCategory == null -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.CategorySelector)
                )
            }

            state.selectedAccount == null -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.AccountSelector)
                )
            }

            state.transactionType == Transaction.Type.TRANSFER && state.targetAccount == null -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdatePad(pad = AddNewTransactionStore.State.Pad.TargetAccountSelector)
                )
            }
        }
    }

}