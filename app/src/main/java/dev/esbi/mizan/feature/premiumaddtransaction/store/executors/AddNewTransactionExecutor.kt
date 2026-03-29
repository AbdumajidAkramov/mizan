package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import android.util.Log
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore
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

            is AddNewTransactionStore.Intent.Next -> {
                validateAndProceedToConfirmation()
            }

            is AddNewTransactionStore.Intent.Back -> {
                // If in confirmation, go back to input
                if (state().step == AddNewTransactionStore.State.Step.CONFIRMATION) {
                    dispatch(AddNewTransactionStore.Message.UpdateStep(AddNewTransactionStore.State.Step.INPUT))
                } else {
                    publish(AddNewTransactionStore.Label.BackTo)
                }
            }

            else -> Unit
        }
    }

    private fun validateAndProceedToConfirmation() {
        val state = state()

        // Validate required fields
        when {
            state.amountDecimal.toDouble() <= 0.0 -> {
                publish(AddNewTransactionStore.Label.ShowToast("Please enter an amount"))
            }

            state.selectedAccount == null -> {
                publish(AddNewTransactionStore.Label.ShowToast("Please select an account"))
            }

            state.selectedCategory == null -> {
                publish(AddNewTransactionStore.Label.ShowToast("Please select a category"))
            }

            state.transactionType == Transaction.Type.TRANSFER && state.targetAccount == null -> {
                publish(AddNewTransactionStore.Label.ShowToast("Please select a target account for transfer"))
            }

            else -> {
                // All validations passed, proceed to confirmation (DO NOT SAVE YET)
                dispatch(AddNewTransactionStore.Message.UpdateStep(AddNewTransactionStore.State.Step.CONFIRMATION))
            }
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
                dispatch(AddNewTransactionStore.Message.UpdateStep(AddNewTransactionStore.State.Step.CONFIRMATION))
            }
        }
    }

}