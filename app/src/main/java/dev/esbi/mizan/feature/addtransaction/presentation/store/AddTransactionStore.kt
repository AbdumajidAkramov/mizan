package dev.esbi.mizan.feature.addtransaction.presentation.store

import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.utils.annotatedString
import java.time.LocalDate

internal interface AddTransactionStore :
    Store<AddTransactionStore.Intent, AddTransactionStore.State, AddTransactionStore.Label> {
    data class State(
        val flowState: FlowState = FlowState.Amount,
        val inputMode: InputMode = InputMode.Manual,
        val type: TransactionType = TransactionType.Expense,
        val currency: String = "UZS",

        val leftNumber: String = "",
        val rightNumber: String = "",
        val operator: String = "",

        val selectedCategory: String? = null,
        val fromAccountId: String? = null,
        val toAccountId: String? = null,

        val date: LocalDate = LocalDate.now(),
        val notes: String = "",
    ) {

        val isLeftNumberActive: Boolean = operator.isEmpty()
        val displayText: String
            get() {
                return if (operator.isEmpty()) {
                    leftNumber
                } else {
                    "$leftNumber $operator $rightNumber"
                }
            }

        val amountText: String
            get() {
                return if (operator.isEmpty()) {
                    leftNumber
                } else {
                    rightNumber
                }
            }

        val amount: Double get() = amountText.toDoubleOrNull() ?: 0.0

        val annotatedString: AnnotatedString get() = amountText.annotatedString(currency = currency)

        val isFormValid = amount > 0.0 &&
                (type != TransactionType.Transfer || (fromAccountId != null && toAccountId != null)) &&
                (type == TransactionType.Transfer || selectedCategory != null)

    }

    sealed interface Intent {
        class OnKeypadClick(val key: Keypad) : Intent
        data object BackToPrev : Intent
        data object OnKeypadNext : Intent

        data object OnNextTransfer : Intent

        class OnTransactionTypeChange(val type: TransactionType) : Intent
        class OnTransactionTypeSelect(val type: TransactionType) : Intent
        class OnInputModeChange(val inputMode: InputMode) : Intent
        class OnSelectFromAccount(val accountId: String? = null) : Intent
        class OnSelectToAccount(val accountId: String? = null) : Intent

        class OnCategorySelect(val category: String) : Intent

        class OnDateChange(val date: LocalDate) : Intent
        class OnNoteChange(val notes: String) : Intent
        data object OnSaveTransaction : Intent
    }

    sealed interface Label {
        data object Close : Label
    }

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Message {
        class UpdateFlowState(val flowState: FlowState) : Message
        class UpdateInputMode(val inputMode: InputMode) : Message
        class UpdateLeftText(val text: String) : Message
        class UpdateRightText(val text: String) : Message
        class UpdateOperator(val text: String) : Message
        class UpdateTransactionCategory(val category: String) : Message
        class UpdateTransactionType(val type: TransactionType) : Message
        class UpdateTransactionDate(val date: LocalDate) : Message
        class UpdateTransactionNotes(val notes: String) : Message
        data object ClearText : Message
    }
}
