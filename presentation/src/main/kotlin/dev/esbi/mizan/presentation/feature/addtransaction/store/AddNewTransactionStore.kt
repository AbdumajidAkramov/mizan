package dev.esbi.mizan.presentation.feature.addtransaction.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Amount
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.presentation.feature.addtransaction.model.Keypad
import dev.esbi.mizan.presentation.feature.addtransaction.model.QuickTemplate
import dev.esbi.mizan.presentation.feature.addtransaction.model.TransactionType
import java.math.BigDecimal

interface AddNewTransactionStore :
    Store<AddNewTransactionStore.Intent, AddNewTransactionStore.State, AddNewTransactionStore.Label> {

    data class State(
        val showTemplates: Boolean = false,
        val templateList: List<QuickTemplate> = emptyList(),

        val transactionType: TransactionType = TransactionType.EXPENSE,
        val step: Step = Step.INPUT,
        val isEditMode: Boolean = false,
        val editingTransactionId: Long? = null,

        val expression: String = "",
        val currentValue: String = "0",
        val isResultShown: Boolean = false,

        val currencies: List<Currency> = emptyList(),
        val selectedCurrency: Currency? = null,
        val operator: String = "",
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

        val fee: BigDecimal? = null,

        val note: String = "",
        val description: String = "",
        val photoPaths: List<String> = emptyList(),
        val transactionDate: Long = System.currentTimeMillis(),
        val saveAsTemplate: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,

        val pad: Pad? = null,

        val isSelectAccountsBottomSheetVisible: Boolean = false,
        val isTargetAccountsBottomSheetVisible: Boolean = false,
        val isCategoriesBottomSheetVisible: Boolean = false,
        val isExchangeRateBottomSheetVisible: Boolean = false,
        
        val manualExchangeRate: BigDecimal? = null,
    ) {

        val categories: List<Category> get() = allCategories.filter { it.type == transactionType }

        val isLeftNumberActive: Boolean = operator.isEmpty()

        val amount: Amount
            get() = Amount(
                value = amountDecimal,
                currency = selectedCurrency?.symbol
            )

        /**
         * Real-time equivalent amount in main currency.
         * Uses manual exchange rate if set, otherwise uses currency's default rate.
         * Scale: 12 for precision, 2 for display.
         */
        val equivalentInMainCurrency: BigDecimal
            get() {
                if (selectedCurrency == null || selectedCurrency.isMainCurrency) {
                    return amountDecimal
                }
                
                val effectiveRate = manualExchangeRate ?: selectedCurrency.exchangeRate
                return amountDecimal.multiply(effectiveRate)
                    .setScale(12, java.math.RoundingMode.HALF_EVEN)
            }

        enum class Pad {
            TypeSelector, CategorySelector, AccountSelector, TargetAccountSelector, AmountInput
        }

        enum class Step {
            INPUT, CONFIRMATION
        }

    }

    sealed interface Intent {
        data class Input(val value: String) : Intent
        object Clear : Intent
        object Delete : Intent
        object Evaluate : Intent
        data object CloseToast : Intent
        class OnUpdateCurrency(val currency: Currency?) : Intent

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

        data object OpenAccountsBottomSheet : Intent
        data object CloseAccountsBottomSheet : Intent

        data object OpenTargetAccountsBottomSheet : Intent
        data object CloseTargetAccountsBottomSheet : Intent

        data object OpenCategoriesBottomSheet : Intent
        data object CloseCategoriesBottomSheet : Intent

        // Number pad actions
        class OnNumberClick(val key: Keypad) : Intent

        //        Confirm & Save
        class UpdateNote(val note: String) : Intent
        class UpdateDate(val date: Long) : Intent
        class UpdateTime(val hour: Int, val minute: Int) : Intent
        class UpdateSaveAsTemplate(val value: Boolean) : Intent
        data object Back : Intent
        data object OnCloseConfirmSave : Intent
        data object SaveTransaction : Intent
        data object DeleteTransaction : Intent

        // Pad actions
        data object ShowTypeSelector : Intent
        data object ShowCategorySelector : Intent
        data object ShowSelectAccountSelector : Intent
        data object ShowTargetAccountSelector : Intent
        data object ShowAmountInputPad : Intent

        data object ShowTransactionDetails : Intent

        data object Next : Intent

        // Selector navigation intents
        data object NavigateToAccountSelector : Intent
        data object NavigateToCategorySelector : Intent
        data class OnAccountSelected(val account: Account) : Intent
        data class OnCategorySelected(val category: Category) : Intent
        
        // Exchange Rate Bottom Sheet
        data object OpenExchangeRateBottomSheet : Intent
        data object CloseExchangeRateBottomSheet : Intent
        data class UpdateManualExchangeRate(val rate: BigDecimal) : Intent
        data object SyncExchangeRateFromCBU : Intent
    }

    sealed interface Label {
        data object OpenCategoryManageScreen : Label
        data object NavigateToAccountManage : Label
        data object NavigateToTemplateManage : Label
        data object BackTo : Label
        data object NavigateToAccountSelector : Label
        data object NavigateToCategorySelector : Label
        class ShowToast(val message: String) : Label

        object TransactionSaved : Label
        object TransactionDeleted : Label
    }

    sealed interface Message {
        class UpdatePad(val pad: State.Pad?) : Message
        class UpdateTemplateVisible(val isVisible: Boolean) : Message
        class UpdateTransactionType(val type: Transaction.Type) : Message
        class UpdateAccounts(val accounts: List<Account>) : Message
        class UpdateAllCategories(val categories: List<Category>) : Message
        class UpdateCurrencies(val currencies: List<Currency>) : Message
        class UpdateSelectedAccount(val account: Account?) : Message
        class UpdateTargetAccount(val account: Account?) : Message

        class UpdateSelectedCategory(val category: Category?) : Message
        class UpdateSelectedSubCategory(val subCategory: Category?) : Message

        class UpdateSelectAccountsBottomSheet(val isVisible: Boolean) : Message
        class UpdateTargetAccountsBottomSheet(val isVisible: Boolean) : Message
        class UpdateCategoriesBottomSheet(val isVisible: Boolean) : Message
        class UpdateExchangeRateBottomSheet(val isVisible: Boolean) : Message
        class UpdateManualExchangeRate(val rate: BigDecimal?) : Message

        data object CloseToast : Message

        class UpdateLoading(val loading: Boolean) : Message
        class UpdateError(val error: String?) : Message
        class UpdateCurrency(val currency: Currency? = null) : Message
        class UpdateAmount(
            val operator: String = "",
            val leftNumber: String = "0",
            val rightNumber: String = "",
            val isFinalResult: Boolean = false,
            val currency: Currency? = null,
            val amountDecimal: BigDecimal = BigDecimal.ZERO,
            val displayText: String = ""
        ) : Message

        data class UpdateDisplayText(
            val currentValue: String,
            val expression: String,
            val isResultShown: Boolean = false
        ) : Message

        class UpdateNote(val note: String) : Message
        class UpdateDescription(val description: String) : Message
        class UpdateTransactionDate(val date: Long) : Message
        class UpdateTransactionTime(val hour: Int, val minute: Int) : Message
        class UpdateSaveAsTemplate(val saveAsTemplate: Boolean) : Message

        class UpdateStep(val step: State.Step) : Message

        data class TransactionLoaded(
            val transaction: Transaction,
            val account: Account?,
            val category: Category?,
            val targetAccount: Account?
        ) : Message
    }

    sealed interface Action {
        data object InitAccounts : Action
        data object InitCategories : Action
        data object InitCurrencies : Action
        data object InitPad : Action
        data object CheckAndConfirm : Action
        data object LoadTransaction : Action
    }

    sealed interface SideEffect
}