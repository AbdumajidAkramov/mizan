package dev.esbi.mizan.feature.newtransaction

sealed class TransactionStep(
    val title: String,
) {
    class AmountInput : TransactionStep("Amount Input")
    class TypeSelector : TransactionStep("Transaction Type")
    class CategoryChooser : TransactionStep("Choose Category")
    class Transfer : TransactionStep("Transfer")
    class ConfirmSave : TransactionStep("Confirm & Save")
}