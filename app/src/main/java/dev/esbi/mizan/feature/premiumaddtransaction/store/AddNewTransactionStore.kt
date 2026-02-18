package dev.esbi.mizan.feature.premiumaddtransaction.store

import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.utils.annotatedString

interface AddNewTransactionStore :
    Store<AddNewTransactionStore.Intent, AddNewTransactionStore.State, AddNewTransactionStore.Label> {

    data class State(
        val showTemplates: Boolean = false,

        val transactionType: TransactionType = TransactionType.EXPENSE,
        val isConfirm: Boolean = false,

        val operator: String = "",
        val leftNumber: String = "",
        val rightNumber: String = "",
        val currency: String = "UZS",

        val allCategories: List<Category> = emptyList(),
        val selectedCategory: Category? = null,
        val selectedSubCategory: Category? = null,

        val isTargetAccount: Boolean = false,
        val accounts: List<Account> = emptyList(),
        val selectedAccount: Account? = null,
        val targetAccount: Account? = null,

        val note: String = "",
        val description: String = "",
        val transactionDate: Long = System.currentTimeMillis(),

        val pad: Pad? = null,
    ) {

        val categories: List<Category> get() = allCategories.filter { it.type == transactionType }

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

        val amount: Double
            get() = leftNumber.toDoubleOrNull() ?: 0.0

        val canSubmit: Boolean get() = amount > 0.0

        val annotatedString: AnnotatedString get() = amountText.annotatedString(currency = currency)


        enum class Pad {
            TypeSelector, CategorySelector, AccountSelector, TargetAccountSelector, AmountInput
        }

    }

    sealed interface Intent {
        data object OnClosePad : Intent

        // Update transaction type
        class SelectTransactionType(val type: Transaction.Type) : Intent

        //Category selector
        class OnCategorySelect(val category: Category) : Intent
        class OnSubCategorySelect(val subCategory: Category?) : Intent
        data object OpenCategoryManageScreen : Intent

        // Account selector
        class UpdateTargetAccount(val account: Account?) : Intent
        class UpdateSelectedAccount(val account: Account?) : Intent
        data object OpenAccountManageScreen : Intent

        // Number pad actions
        class OnNumberClick(val key: Keypad) : Intent


        // Pad actions
        data object ShowTypeSelector : Intent
        data object ShowCategorySelector : Intent
        data object ShowSelectAccountSelector : Intent
        data object ShowTargetAccountSelector : Intent
        data object ShowAmountInputPad : Intent

        data object Next : Intent
    }

    sealed interface Label {
        data object OpenCategoryManageScreen : Label
        data object NavigateToAccountManage : Label
    }

    sealed interface Message {
        class UpdatePad(val pad: State.Pad) : Message
        class UpdateTransactionType(val type: Transaction.Type) : Message
        class UpdateAccounts(val accounts: List<Account>) : Message
        class UpdateAllCategories(val categories: List<Category>) : Message
        class UpdateSelectedAccount(val account: Account?) : Message
        class UpdateTargetAccount(val account: Account?) : Message

        class UpdateSelectedCategory(val category: Category?) : Message
        class UpdateSelectedSubCategory(val subCategory: Category?) : Message

        class UpdateAmount(
            val operator: String = "",
            val leftNumber: String = "",
            val rightNumber: String = "",
            val currency: String = "UZS"
        ) : Message

    }

    sealed interface Action {
        data object InitAccounts : Action
        data object InitCategories : Action
        data object InitPad : Action
        data object CheckAndConfirm : Action
    }

    sealed interface SideEffect
}
