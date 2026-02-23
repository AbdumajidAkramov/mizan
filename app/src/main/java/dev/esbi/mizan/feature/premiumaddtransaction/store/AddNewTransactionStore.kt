package dev.esbi.mizan.feature.premiumaddtransaction.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Amount
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.amountinput.QuickTemplate
import java.math.BigDecimal

interface AddNewTransactionStore :
    Store<AddNewTransactionStore.Intent, AddNewTransactionStore.State, AddNewTransactionStore.Label> {

    data class State(
        val showTemplates: Boolean = false,

        val transactionType: TransactionType = TransactionType.EXPENSE,
        val isConfirm: Boolean = false,

        val operator: String = "",
        val currency: String = "UZS",
        val displayText: String = "",

        val leftNumber: String = "0",
        val rightNumber: String = "",
        val isFinalResult: Boolean = false,
        val amountDecimal: BigDecimal = BigDecimal.ZERO,


        val allCategories: List<Category> = emptyList(),
        val selectedCategory: Category? = null,
        val selectedSubCategory: Category? = null,

        val isTargetAccount: Boolean = false,
        val accounts: List<Account> = emptyList(),
        val selectedAccount: Account? = null,
        val targetAccount: Account? = null,

        val isBookmarked: Boolean = false,

        val fee: Double? = null,

        val note: String = "",
        val description: String = "",
        val photoPaths: List<String> = emptyList(),
        val transactionDate: Long = System.currentTimeMillis(),
        val saveAsTemplate: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,

        val pad: Pad? = null,
    ) {

        val categories: List<Category> get() = allCategories.filter { it.type == transactionType }

        val isLeftNumberActive: Boolean = operator.isEmpty()

        val amount: Amount
            get() = Amount(
                value = amountDecimal,
                currency = currency
            )

        enum class Pad {
            TypeSelector, CategorySelector, AccountSelector, TargetAccountSelector, AmountInput
        }

    }

    sealed interface Intent {
        data object OnClosePad : Intent
        data object ToggleTemplates : Intent
        class OnSelectedTemplate(val template: QuickTemplate) : Intent
        data object OpenTemplateManage : Intent

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

        //        Confirm & Save
        class UpdateNote(val note: String) : Intent
        class UpdateDate(val date: Long) : Intent
        class UpdateSaveAsTemplate(val value: Boolean) : Intent
        data object Back : Intent
        data object ConfirmSave : Intent

        // Pad actions
        data object ShowTypeSelector : Intent
        data object ShowCategorySelector : Intent
        data object ShowSelectAccountSelector : Intent
        data object ShowTargetAccountSelector : Intent
        data object ShowAmountInputPad : Intent

        data object ShowTransactionDetails : Intent

        data object Next : Intent
    }

    sealed interface Label {
        data object OpenCategoryManageScreen : Label
        data object NavigateToAccountManage : Label
        data object NavigateToTemplateManage : Label
        data object BackTo : Label

        object TransactionSaved : Label
    }

    sealed interface Message {
        class UpdatePad(val pad: State.Pad?) : Message
        class UpdateTemplateVisible(val isVisible: Boolean) : Message
        class UpdateTransactionType(val type: Transaction.Type) : Message
        class UpdateAccounts(val accounts: List<Account>) : Message
        class UpdateAllCategories(val categories: List<Category>) : Message
        class UpdateSelectedAccount(val account: Account?) : Message
        class UpdateTargetAccount(val account: Account?) : Message

        class UpdateSelectedCategory(val category: Category?) : Message
        class UpdateSelectedSubCategory(val subCategory: Category?) : Message

        class UpdateLoading(val loading: Boolean) : Message
        class UpdateError(val error: String?) : Message

        class UpdateAmount(
            val operator: String = "",
            val leftNumber: String = "0",
            val rightNumber: String = "",
            val isFinalResult: Boolean = false,
            val currency: String = "UZS",
            val amountDecimal: BigDecimal = BigDecimal.ZERO,
            val displayText: String = ""
        ) : Message

        class UpdateNote(val note: String) : Message
        class UpdateDescription(val description: String) : Message
        class UpdateTransactionDate(val date: Long) : Message
        class UpdateSaveAsTemplate(val saveAsTemplate: Boolean) : Message

        class UpdateIsConfirm(val isConfirm: Boolean) : Message
    }

    sealed interface Action {
        data object InitAccounts : Action
        data object InitCategories : Action
        data object InitPad : Action
        data object CheckAndConfirm : Action
    }

    sealed interface SideEffect
}
