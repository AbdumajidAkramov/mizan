package dev.esbi.mizan.transactions.add.mvikotlin

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.transactions.add.mvikotlin.AddNewTransactionStore.Message
import dev.esbi.mizan.transactions.add.mvikotlin.AddNewTransactionStore.State
import java.util.Calendar

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
            is Message.UpdateAccounts -> copy(accounts = msg.accounts)
            is Message.UpdateAllCategories -> copy(allCategories = msg.categories)
            is Message.UpdateCurrencies -> copy(
                currencies = msg.currencies,
                selectedCurrency = msg.currencies.firstOrNull { it.isMainCurrency }
                    ?: msg.currencies.firstOrNull()
            )

            is Message.UpdateSelectedAccount -> copy(selectedAccount = msg.account)
            is Message.UpdateTargetAccount -> copy(targetAccount = msg.account)
            is Message.UpdateSelectedCategory -> copy(selectedCategory = msg.category)
            is Message.UpdateSelectedSubCategory -> copy(selectedSubCategory = msg.subCategory)
            is Message.UpdateAmount -> copy(
                operator = msg.operator,
                leftNumber = msg.leftNumber,
                rightNumber = msg.rightNumber,
                isFinalResult = msg.isFinalResult,
                selectedCurrency = msg.currency,
                amountDecimal = msg.amountDecimal,
                displayText = msg.displayText
            )

            is Message.UpdateNote -> copy(note = msg.note)
            is Message.UpdateDescription -> copy(description = msg.description)
            is Message.UpdateTransactionDate -> copy(transactionDate = msg.date)
            is Message.UpdateTransactionTime -> {
                // Merge time with existing date
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = transactionDate
                    set(Calendar.HOUR_OF_DAY, msg.hour)
                    set(Calendar.MINUTE, msg.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                copy(transactionDate = calendar.timeInMillis)
            }

            is Message.UpdateSaveAsTemplate -> copy(saveAsTemplate = msg.saveAsTemplate)
            is Message.UpdateStep -> copy(step = msg.step)
            is Message.TransactionLoaded -> copy(
                transactionType = msg.transaction.type,
                amountDecimal = msg.transaction.amount,
                leftNumber = msg.transaction.amount.toPlainString(),
                displayText = msg.transaction.amount.toPlainString(),
                selectedAccount = msg.account,
                targetAccount = msg.targetAccount,
                selectedCategory = msg.category,
                transactionDate = msg.transaction.date,
                note = msg.transaction.note ?: "",
                description = msg.transaction.description ?: "",
                selectedCurrency = msg.transaction.currency,
                isEditMode = true,
                editingTransactionId = msg.transaction.id
            )

            is Message.UpdateError -> copy(error = msg.error)
            is Message.UpdateLoading -> copy(isLoading = msg.loading)
            is Message.UpdateCurrency -> copy(selectedCurrency = msg.currency)
            is Message.UpdateSelectAccountsBottomSheet -> copy(isSelectAccountsBottomSheetVisible = msg.isVisible)
            is Message.UpdateTargetAccountsBottomSheet -> copy(isTargetAccountsBottomSheetVisible = msg.isVisible)
            is Message.UpdateCategoriesBottomSheet -> copy(isCategoriesBottomSheetVisible = msg.isVisible)
            is Message.UpdateExchangeRateBottomSheet -> copy(isExchangeRateBottomSheetVisible = msg.isVisible)
            is Message.UpdateManualExchangeRate -> copy(manualExchangeRate = msg.rate)
            is Message.CloseToast -> copy(error = null)
            is Message.UpdateEditMode -> copy(isEditMode = msg.isEdit)
        }
}
