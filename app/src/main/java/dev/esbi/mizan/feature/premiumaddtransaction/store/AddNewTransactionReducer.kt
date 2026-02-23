package dev.esbi.mizan.feature.premiumaddtransaction.store

import com.arkivanov.mvikotlin.core.store.Reducer

object AddNewTransactionReducer :
    Reducer<AddNewTransactionStore.State, AddNewTransactionStore.Message> {

    override fun AddNewTransactionStore.State.reduce(msg: AddNewTransactionStore.Message) =
        when (msg) {
            is AddNewTransactionStore.Message.UpdateTransactionType -> copy(
                transactionType = msg.type,
                selectedCategory = null,
                selectedSubCategory = null
            )

            is AddNewTransactionStore.Message.UpdateTemplateVisible -> copy(showTemplates = msg.isVisible)
            is AddNewTransactionStore.Message.UpdatePad -> copy(pad = msg.pad)
            is AddNewTransactionStore.Message.UpdateAccounts -> copy(accounts = msg.accounts)
            is AddNewTransactionStore.Message.UpdateAllCategories -> copy(allCategories = msg.categories)
            is AddNewTransactionStore.Message.UpdateSelectedAccount -> copy(selectedAccount = msg.account)
            is AddNewTransactionStore.Message.UpdateTargetAccount -> copy(targetAccount = msg.account)
            is AddNewTransactionStore.Message.UpdateSelectedCategory -> copy(selectedCategory = msg.category)
            is AddNewTransactionStore.Message.UpdateSelectedSubCategory -> copy(selectedSubCategory = msg.subCategory)
            is AddNewTransactionStore.Message.UpdateAmount -> copy(
                operator = msg.operator,
                leftNumber = msg.leftNumber,
                rightNumber = msg.rightNumber,
                isFinalResult = msg.isFinalResult,
                currency = msg.currency,
                amountDecimal = msg.amountDecimal,
                displayText = msg.displayText
            )

            is AddNewTransactionStore.Message.UpdateNote -> copy(note = msg.note)
            is AddNewTransactionStore.Message.UpdateDescription -> copy(description = msg.description)
            is AddNewTransactionStore.Message.UpdateTransactionDate -> copy(transactionDate = msg.date)
            is AddNewTransactionStore.Message.UpdateSaveAsTemplate -> copy(saveAsTemplate = msg.saveAsTemplate)
            is AddNewTransactionStore.Message.UpdateIsConfirm -> copy(isConfirm = msg.isConfirm)
            is AddNewTransactionStore.Message.UpdateError -> copy(error = msg.error)
            is AddNewTransactionStore.Message.UpdateLoading -> copy(isLoading = msg.loading)
        }
}
