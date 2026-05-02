package dev.esbi.mizan.presentation.feature.addtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import javax.inject.Inject

class AddNewTransactionExecutor @Inject constructor(

) : CoroutineExecutor<Intent,
        Action,
        State,
        Message,
        Label>() {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.CheckAndConfirm -> {
                validateAndProceedToConfirmation()
            }

            else -> Unit
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.SelectTransactionType -> {
                if (state().transactionType != intent.type) {
                    dispatch(
                        Message.UpdateTransactionType(type = intent.type)
                    )
                }
            }

            is Intent.ToggleTemplates -> {
                dispatch(
                    Message.UpdateTemplateVisible(!state().showTemplates)
                )
            }

            is Intent.NavigateToAccountSelector -> {
                publish(Label.NavigateToAccountSelector)
            }

            /*
                        is Intent.NavigateToCategorySelector -> {
                            publish(Label.NavigateToCategorySelector)
                        }
            */

            is Intent.OnAccountSelected -> {
                dispatch(Message.UpdateSelectedAccount(intent.account))
            }

            is Intent.OnCategorySelected -> {
                dispatch(Message.UpdateSelectedCategory(intent.category))
            }

            is Intent.OpenAccountsBottomSheet -> {
                dispatch(Message.UpdateSelectAccountsBottomSheet(true))
            }

            is Intent.CloseAccountsBottomSheet -> {
                dispatch(Message.UpdateSelectAccountsBottomSheet(false))
            }

            is Intent.OpenTargetAccountsBottomSheet -> {
                dispatch(Message.UpdateTargetAccountsBottomSheet(true))
            }

            is Intent.CloseTargetAccountsBottomSheet -> {
                dispatch(Message.UpdateTargetAccountsBottomSheet(false))
            }

            is Intent.OpenCategoriesBottomSheet -> {
                dispatch(Message.UpdateCategoriesBottomSheet(true))
            }

            is Intent.CloseCategoriesBottomSheet -> {
                dispatch(Message.UpdateCategoriesBottomSheet(false))
            }

            is Intent.CloseToast -> {
                dispatch(Message.CloseToast)
            }

            is Intent.Next -> {
                validateAndProceedToConfirmation()
            }

            is Intent.Back -> {
                // If in confirmation, go back to input
                if (state().step == State.Step.CONFIRMATION) {
                    dispatch(Message.UpdateStep(State.Step.INPUT))
                } else {
                    publish(Label.BackTo)
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
                publish(Label.ShowToast("Please enter an amount"))
            }

            state.selectedAccount == null -> {
                publish(Label.ShowToast("Please select an account"))
            }

            state.selectedCategory == null && state.transactionType != Transaction.Type.TRANSFER -> {
                publish(Label.ShowToast("Please select a category"))
            }

            state.transactionType == Transaction.Type.TRANSFER && state.targetAccount == null -> {
                publish(Label.ShowToast("Please select a target account for transfer"))
            }

            else -> {
                // All validations passed, proceed to confirmation (DO NOT SAVE YET)
                dispatch(Message.UpdateStep(State.Step.CONFIRMATION))
            }
        }
    }

    private fun checkPadState() {
        dispatch(Message.UpdateStep(State.Step.CONFIRMATION))
    }

}