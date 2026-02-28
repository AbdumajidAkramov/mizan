package dev.esbi.mizan.feature.premiumaddtransaction.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.State

object AddNewTransactionReducer :
    Reducer<State, Message> {

    override fun State.reduce(msg: Message) =
        when (msg) {
            is Message.UpdateTransactionType -> copy(
                transactionType = msg.type,
                selectedCategory = null,
                selectedSubCategory = null
            )

            is Message.UpdateDisplayText -> copy(
                currentValue = msg.currentValue,
                expression = msg.expression,
                isResultShown = msg.isResultShown
            )

            is Message.UpdateTemplateVisible -> copy(showTemplates = msg.isVisible)
            is Message.UpdatePad -> copy(pad = msg.pad)
            is Message.UpdateAccounts -> copy(accounts = msg.accounts)
            is Message.UpdateAllCategories -> copy(allCategories = msg.categories)
            is Message.UpdateSelectedAccount -> copy(selectedAccount = msg.account)
            is Message.UpdateTargetAccount -> copy(targetAccount = msg.account)
            is Message.UpdateSelectedCategory -> copy(selectedCategory = msg.category)
            is Message.UpdateSelectedSubCategory -> copy(selectedSubCategory = msg.subCategory)
            is Message.UpdateAmount -> copy(
                operator = msg.operator,
                leftNumber = msg.leftNumber,
                rightNumber = msg.rightNumber,
                isFinalResult = msg.isFinalResult,
                currency = msg.currency,
                amountDecimal = msg.amountDecimal,
                displayText = msg.displayText
            )

            is Message.UpdateNote -> copy(note = msg.note)
            is Message.UpdateDescription -> copy(description = msg.description)
            is Message.UpdateTransactionDate -> copy(transactionDate = msg.date)
            is Message.UpdateSaveAsTemplate -> copy(saveAsTemplate = msg.saveAsTemplate)
            is Message.UpdateIsConfirm -> copy(isConfirm = msg.isConfirm)
            is Message.UpdateError -> copy(error = msg.error)
            is Message.UpdateLoading -> copy(isLoading = msg.loading)
            is Message.UpdateCurrency -> copy(currency = msg.currency)
            is Message.UpdateSelectAccountsBottomSheet -> copy(isSelectAccountsBottomSheetVisible = msg.isVisible)
            is Message.UpdateTargetAccountsBottomSheet -> copy(isTargetAccountsBottomSheetVisible = msg.isVisible)
            is Message.UpdateCategoriesBottomSheet -> copy(isCategoriesBottomSheetVisible = msg.isVisible)
            is Message.CloseToast -> copy(error = null)
        }
}
