package dev.esbi.mizan.feature.addtransaction.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.addtransaction.domain.models.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnInputModeChange
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnKeypadClick
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Intent.OnNext
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateFlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateInputMode
import kotlinx.coroutines.CoroutineDispatcher

internal class AddTransactionExecutor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : CoroutineExecutor<
        AddTransactionStore.Intent,
        AddTransactionStore.Action,
        AddTransactionStore.State,
        AddTransactionStore.Message,
        AddTransactionStore.Label>(
    mainContext = mainDispatcher
) {
    override fun executeAction(action: AddTransactionStore.Action) {
        super.executeAction(action)
    }

    override fun executeIntent(intent: AddTransactionStore.Intent) {
        super.executeIntent(intent)
        when (intent) {
            is OnNext -> {
                val flowState = intent.flowState
                    ?: when (state().flowState) {
                        FlowState.Type -> FlowState.Amount
                        FlowState.Details -> FlowState.Type
                        FlowState.Confirm -> FlowState.Details
                        else -> FlowState.Amount
                    }
                dispatch(UpdateFlowState(flowState))
            }

            is OnKeypadClick -> onKeypadClick(intent.key)
            is OnInputModeChange -> dispatch(UpdateInputMode(intent.inputMode))

            else -> {}

        }
    }

    private var left: Double = 0.0
    private var right: Double = 0.0
    private var operator: String = ""
    private fun clear() {
        left = 0.0
        right = 0.0
        operator = ""
        dispatch(AddTransactionStore.Message.UpdateAmount(0.0))
    }

    private fun calculate() {
        when (operator) {
            "+" -> left += right
            "-" -> left -= right
            "*" -> left *= right
            "/" -> {
                if (right != 0.0) {
                    left /= right
                } else {
                    left = 0.0
                }
            }
        }
        right = 0.0
        operator = ""
    }

    private fun onKeypadClick(key: Keypad) {
        val amount = StringBuilder(state().amount.toString())
        amount.append(Keypad.numberChar(key))
        when (key) {
            Keypad.CLEAR -> {
                amount.clear()
            }

            Keypad.DEL -> {
                if (operator.isEmpty()) {
                    left = left *  10 + 1
                } else {
                    right = right * 10 + 1
                }
            }

            Keypad.EQUALS -> calculate()
            Keypad.DIVIDE -> {
                if (operator.isNotEmpty()) {
                    calculate()
                }
                operator = "/"
            }

            Keypad.MULTIPLY -> {
                if (operator.isNotEmpty()) {
                    calculate()
                }
                operator = "*"
            }

            Keypad.MINUS -> {
                if (operator.isNotEmpty()) {
                    calculate()
                }
                operator = "-"
            }

            Keypad.PLUS -> {
                if (operator.isNotEmpty()) {
                    calculate()
                }
                operator = "+"
            }

            Keypad.ONE -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 1
                } else {
                    right = right * 10 + 1
                }
            }

            Keypad.TWO -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 2
                } else {
                    right = right * 10 + 2
                }
            }

            Keypad.THREE -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 3
                } else {
                    right = right * 10 + 3
                }
            }

            Keypad.FOUR -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 4
                } else {
                    right = right * 10 + 4
                }
            }

            Keypad.FIVE -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 5
                } else {
                    right = right * 10 + 5
                }
            }

            Keypad.SIX -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 6
                } else {
                    right = right * 10 + 6
                }
            }

            Keypad.SEVEN -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 7
                } else {
                    right = right * 10 + 7
                }
            }

            Keypad.EIGHT -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 8
                } else {
                    right = right * 10 + 8
                }
            }

            Keypad.NINE -> {
                if (operator.isEmpty()) {
                    left = left * 10 + 9
                } else {
                    right = right * 10 + 9
                }
            }

            Keypad.ZERO -> {
                if (operator.isEmpty()) {
                    left *= 10
                } else {
                    right *= 10
                }
            }

            Keypad.ZERO_ZERO -> {
                if (operator.isEmpty()) {
                    left *= 100
                } else {
                    right *= 100
                }
            }

            Keypad.ZERO_ZERO_ZERO -> {
                if (operator.isEmpty()) {
                    left *= 1000
                } else {
                    right *= 1000
                }
            }

            else -> {

            }
        }
        dispatch(
            AddTransactionStore.Message.UpdateAmount(
                amount.toString().toDoubleOrNull() ?: 0.0
            )
        )
    }
}
