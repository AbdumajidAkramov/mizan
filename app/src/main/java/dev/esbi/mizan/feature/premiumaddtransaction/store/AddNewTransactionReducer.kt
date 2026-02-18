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

            is AddNewTransactionStore.Message.UpdatePad -> copy(pad = msg.pad)
            is AddNewTransactionStore.Message.UpdateAccounts -> copy(accounts = msg.accounts)
            is AddNewTransactionStore.Message.UpdateAllCategories -> copy(allCategories = msg.categories)
            is AddNewTransactionStore.Message.UpdateSelectedAccount -> copy(selectedAccount = msg.account)
            is AddNewTransactionStore.Message.UpdateTargetAccount -> copy(targetAccount = msg.account)
            is AddNewTransactionStore.Message.UpdateSelectedCategory -> copy(selectedCategory = msg.category)
            is AddNewTransactionStore.Message.UpdateSelectedSubCategory -> copy(selectedSubCategory = msg.subCategory)
        }
}
