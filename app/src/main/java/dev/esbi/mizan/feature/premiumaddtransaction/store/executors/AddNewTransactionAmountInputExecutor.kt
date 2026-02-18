package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.store.state.KeypadState
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import javax.inject.Inject

internal class AddNewTransactionAmountInputExecutor @Inject constructor(
    private val manualInputHandler: ManualInputHandler,
) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            is AddNewTransactionStore.Intent.OnNumberClick -> {
                with(state()) {
                    if (intent.key == Keypad.EQUALS) {
                        forward(AddNewTransactionStore.Action.CheckAndConfirm)
                    } else {
                        val newKeypadState = manualInputHandler.handleNumberClick(
                            intent.key,
                            KeypadState(
                                operator = operator,
                                leftNumber = leftNumber,
                                rightNumber = rightNumber,
                                currency = currency,
                            )
                        )
                        dispatch(
                            AddNewTransactionStore.Message.UpdateAmount(
                                operator = newKeypadState.operator,
                                leftNumber = newKeypadState.leftNumber,
                                rightNumber = newKeypadState.rightNumber,
                                currency = newKeypadState.currency,
                            )
                        )
                    }
                }
            }

            else -> Unit
        }
    }
}
