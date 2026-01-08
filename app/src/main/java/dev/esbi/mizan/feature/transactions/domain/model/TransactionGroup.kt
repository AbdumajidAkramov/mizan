package dev.esbi.mizan.feature.transactions.domain.model

/**
 * Grouped transactions by date
 */
data class TransactionGroup(
    val dateLabel: String,
    val transactions: List<Transaction>
)
