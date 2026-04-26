package dev.esbi.mizan.feature.newtransaction2.input

sealed interface TransactionInputState {
    class TransactionTypeSelector() : TransactionInputState
    class TransactionAmountInput() : TransactionInputState
    class TransactionCategorySelector() : TransactionInputState
    class TransactionAccountSelector() : TransactionInputState
    data object TransactionEmpty : TransactionInputState
}
