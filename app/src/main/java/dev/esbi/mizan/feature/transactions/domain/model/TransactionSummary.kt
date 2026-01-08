package dev.esbi.mizan.feature.transactions.domain.model

/**
 * Transaction summary with totals and grouped transactions
 */
data class TransactionSummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val groupedTransactions: List<TransactionGroup>,
    val totalTransactions: Int
)
