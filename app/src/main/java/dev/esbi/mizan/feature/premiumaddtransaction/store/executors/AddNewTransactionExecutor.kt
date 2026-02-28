package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import android.util.Log
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
                if (state().transactionType != intent.type) {
                    dispatch(
                        AddNewTransactionStore.Message.UpdateTransactionType(type = intent.type)
                    )
                }
                forward(AddNewTransactionStore.Action.CheckAndConfirm)
            }

            is AddNewTransactionStore.Intent.ToggleTemplates -> {
                dispatch(
                    AddNewTransactionStore.Message.UpdateTemplateVisible(!state().showTemplates)
                )
            }

            is AddNewTransactionStore.Intent.NavigateToAccountSelector -> {
                publish(AddNewTransactionStore.Label.NavigateToAccountSelector)
            }

            is AddNewTransactionStore.Intent.NavigateToCategorySelector -> {
                publish(AddNewTransactionStore.Label.NavigateToCategorySelector)
            }

            is AddNewTransactionStore.Intent.OnAccountSelected -> {
                dispatch(AddNewTransactionStore.Message.UpdateSelectedAccount(intent.account))
            }

            is AddNewTransactionStore.Intent.OnCategorySelected -> {
                dispatch(AddNewTransactionStore.Message.UpdateSelectedCategory(intent.category))
            }
            is AddNewTransactionStore.Intent.OpenAccountsBottomSheet -> {
                dispatch(AddNewTransactionStore.Message.UpdateSelectAccountsBottomSheet(true))
            }
            is AddNewTransactionStore.Intent.CloseAccountsBottomSheet -> {
                dispatch(AddNewTransactionStore.Message.UpdateSelectAccountsBottomSheet(false))
            }
            is AddNewTransactionStore.Intent.OpenTargetAccountsBottomSheet -> {
                dispatch(AddNewTransactionStore.Message.UpdateTargetAccountsBottomSheet(true))
            }
            is AddNewTransactionStore.Intent.CloseTargetAccountsBottomSheet -> {
                dispatch(AddNewTransactionStore.Message.UpdateTargetAccountsBottomSheet(false))
            }
            is AddNewTransactionStore.Intent.OpenCategoriesBottomSheet -> {
                dispatch(AddNewTransactionStore.Message.UpdateCategoriesBottomSheet(true))
            }
            is AddNewTransactionStore.Intent.CloseCategoriesBottomSheet -> {
                dispatch(AddNewTransactionStore.Message.UpdateCategoriesBottomSheet(false))
            }

            is AddNewTransactionStore.Intent.CloseToast -> {
                dispatch(AddNewTransactionStore.Message.CloseToast)
            }

            else -> Unit
        }
    }

    private fun checkPadState() {
        val state = state()
        when {
            state.amount.value.toDouble() == 0.0 -> {
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

            else -> {
                Log.d("TTT", "Confirm screen open")
                dispatch(AddNewTransactionStore.Message.UpdateIsConfirm(true))
            }
        }
    }

}